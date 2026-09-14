package com.aprendiz.educontrol.ui.home

import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
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
        adapter = HomeAdapter(
            onItemClick = { materia ->
                val intent = Intent(this, DetalleMateriaActivity::class.java)
                intent.putExtra("MATERIA_ID", materia.id)
                startActivity(intent)
            },
            onOptionsClick = { materia, view ->
                showMateriaOptions(materia, view)
            }
        )
        binding.rvMaterias.layoutManager = LinearLayoutManager(this)
        binding.rvMaterias.adapter = adapter
    }

    private fun showMateriaOptions(materia: com.aprendiz.educontrol.data.entity.MateriaEntity, view: android.view.View) {
        val popup = PopupMenu(this, view)
        popup.menu.add(0, 1, 0, getString(R.string.editar))
        popup.menu.add(0, 2, 0, getString(R.string.eliminar))
        
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> {
                    // Editar
                    val intent = Intent(this, AddMateriaActivity::class.java)
                    intent.putExtra("USER_ID", userId)
                    intent.putExtra("MATERIA_ID", materia.id)
                    startActivity(intent)
                    true
                }
                2 -> {
                    // Eliminar
                    confirmarEliminarMateria(materia)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
    
    private fun confirmarEliminarMateria(materia: com.aprendiz.educontrol.data.entity.MateriaEntity) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.eliminar))
            .setMessage(getString(R.string.confirmar_eliminar_materia))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                eliminarMateria(materia)
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .show()
    }
    
    private fun eliminarMateria(materia: com.aprendiz.educontrol.data.entity.MateriaEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            database.materiaDao().deleteMateria(materia)
            withContext(Dispatchers.Main) {
                loadData()
            }
        }
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
