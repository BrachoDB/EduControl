package com.aprendiz.educontrol.ui.materia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.MateriaEntity
import com.aprendiz.educontrol.databinding.ActivityDetalleMateriaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class DetalleMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleMateriaBinding
    private lateinit var database: AppDatabase
    private lateinit var adapter: NotasAdapter
    private var materiaId: Long = -1L
    private var currentMateria: MateriaEntity? = null
    private var porcentajeAcumulado = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getLongExtra("MATERIA_ID", -1L)
        if (materiaId == -1L) {
            finish()
            return
        }

        database = AppDatabase.getDatabase(this)
        
        setupRecyclerView()

        binding.fabNuevaNota.setOnClickListener {
            val intent = Intent(this, AddNotaActivity::class.java)
            intent.putExtra("MATERIA_ID", materiaId)
            intent.putExtra("PORCENTAJE_ACUMULADO", porcentajeAcumulado)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun setupRecyclerView() {
        adapter = NotasAdapter()
        binding.rvNotas.layoutManager = LinearLayoutManager(this)
        binding.rvNotas.adapter = adapter
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            currentMateria = database.materiaDao().getMateriaById(materiaId)
            val notas = database.notaDao().getNotasByMateria(materiaId)
            
            var promedioAcumulado = 0.0
            porcentajeAcumulado = 0.0
            
            for (nota in notas) {
                promedioAcumulado += nota.calificacion * (nota.porcentaje / 100.0)
                porcentajeAcumulado += nota.porcentaje
            }

            withContext(Dispatchers.Main) {
                currentMateria?.let { materia ->
                    binding.tvMateriaTitle.text = "${materia.nombreMateria} - ${materia.profesor}"
                }
                
                binding.tvPromedioActual.text = getString(R.string.promedio_actual, String.format(Locale.getDefault(), "%.2f", promedioAcumulado))
                if (promedioAcumulado >= 3.0) {
                    binding.tvPromedioActual.setTextColor(ContextCompat.getColor(this@DetalleMateriaActivity, R.color.primary_emerald))
                } else {
                    binding.tvPromedioActual.setTextColor(ContextCompat.getColor(this@DetalleMateriaActivity, R.color.badge_risk_text))
                }
                
                binding.tvPorcentajeEvaluado.text = getString(R.string.porcentaje_evaluado, String.format(Locale.getDefault(), "%.0f", porcentajeAcumulado))
                binding.progressAvance.progress = porcentajeAcumulado.toInt()
                
                adapter.submitList(notas)
            }
        }
    }
}
