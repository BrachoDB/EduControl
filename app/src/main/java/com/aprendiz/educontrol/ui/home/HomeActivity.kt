package com.aprendiz.educontrol.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.databinding.ActivityHomeBinding
import com.aprendiz.educontrol.ui.materia.DetalleMateriaActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: HomeAdapter
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", -1L)
        if (userId == -1L) {
            finish()
            return
        }

        database = AppDatabase.getDatabase(this)
        
        setupRecyclerView()

        binding.fabNuevaMateria.setOnClickListener {
            // Ir a la actividad para agregar materia
            val intent = Intent(this, AddMateriaActivity::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = HomeAdapter { materia ->
            val intent = Intent(this, DetalleMateriaActivity::class.java)
            intent.putExtra("MATERIA_ID", materia.id)
            startActivity(intent)
        }
        binding.rvMaterias.layoutManager = LinearLayoutManager(this)
        binding.rvMaterias.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val materias = database.materiaDao().getMateriasByUser(userId)
            val mapPromedios = mutableMapOf<Long, Double>()
            var sumaPromediosGlobal = 0.0
            
            for (materia in materias) {
                val notas = database.notaDao().getNotasByMateria(materia.id)
                var promedioMateria = 0.0
                for (nota in notas) {
                    // Ponderación: nota.calificacion * (nota.porcentaje / 100.0)
                    promedioMateria += nota.calificacion * (nota.porcentaje / 100.0)
                }
                mapPromedios[materia.id] = promedioMateria
                sumaPromediosGlobal += promedioMateria
            }
            
            val promedioGeneral = if (materias.isNotEmpty()) {
                sumaPromediosGlobal / materias.size
            } else {
                0.0
            }

            withContext(Dispatchers.Main) {
                adapter.submitList(materias, mapPromedios)
                
                binding.tvMateriasMatriculadas.text = getString(R.string.materias_matriculadas, materias.size)
                binding.tvPromedioGeneral.text = String.format(Locale.getDefault(), "%.2f / 5.0", promedioGeneral)
            }
        }
    }
}
