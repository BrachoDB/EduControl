package com.aprendiz.educontrol.ui.materia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
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
    private var promedioPonderadoAcumulado = 0.0

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
        setupSimulador()

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

    private fun setupSimulador() {
        binding.etNotaObjetivo.doOnTextChanged { _, _, _, _ ->
            calcularNotaNecesaria()
        }
    }

    private fun calcularNotaNecesaria() {
        val porcentajeRestante = 100.0 - porcentajeAcumulado

        if (porcentajeRestante <= 0.001) {
            binding.tvResultadoSimulador.visibility = View.VISIBLE
            binding.tvResultadoSimulador.text = getString(R.string.simulador_materia_cerrada)
            binding.tvResultadoSimulador.setBackgroundResource(R.drawable.bg_chip_risk)
            binding.tvResultadoSimulador.setTextColor(ContextCompat.getColor(this, R.color.badge_risk_text))
            binding.tilNotaObjetivo.isEnabled = false
            return
        } else {
            binding.tilNotaObjetivo.isEnabled = true
        }

        val input = binding.etNotaObjetivo.text?.toString()?.trim()
        if (input.isNullOrEmpty()) {
            binding.tvResultadoSimulador.visibility = View.GONE
            return
        }

        val targetGrade = input.toDoubleOrNull()
        if (targetGrade == null || targetGrade < 0.0 || targetGrade > 5.0) {
            binding.tvResultadoSimulador.visibility = View.GONE
            return
        }

        val puntosNecesarios = targetGrade - promedioPonderadoAcumulado
        val porcentajeRestanteTexto = String.format(Locale.getDefault(), "%.0f", porcentajeRestante)

        binding.tvResultadoSimulador.visibility = View.VISIBLE

        if (puntosNecesarios <= 0) {
            val acumuladoTexto = String.format(Locale.getDefault(), "%.2f", promedioPonderadoAcumulado)
            val metaTexto = String.format(Locale.getDefault(), "%.2f", targetGrade)
            binding.tvResultadoSimulador.text = getString(R.string.simulador_meta_ya_alcanzada, acumuladoTexto, metaTexto)
            binding.tvResultadoSimulador.setBackgroundResource(R.drawable.bg_chip_approved)
            binding.tvResultadoSimulador.setTextColor(ContextCompat.getColor(this, R.color.badge_approved_text))
        } else {
            val notaRequerida = (puntosNecesarios * 100.0) / porcentajeRestante
            val notaRequeridaTexto = String.format(Locale.getDefault(), "%.2f", notaRequerida)

            if (notaRequerida <= 5.0) {
                binding.tvResultadoSimulador.text = getString(R.string.simulador_resultado_exito, notaRequeridaTexto, porcentajeRestanteTexto)
                binding.tvResultadoSimulador.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.tvResultadoSimulador.setTextColor(ContextCompat.getColor(this, R.color.badge_approved_text))
            } else {
                binding.tvResultadoSimulador.text = getString(R.string.simulador_meta_imposible, notaRequeridaTexto, porcentajeRestanteTexto)
                binding.tvResultadoSimulador.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.tvResultadoSimulador.setTextColor(ContextCompat.getColor(this, R.color.badge_risk_text))
            }
        }
    }

    private fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            currentMateria = database.materiaDao().getMateriaById(materiaId)
            val notas = database.notaDao().getNotasByMateria(materiaId)
            
            promedioPonderadoAcumulado = 0.0
            porcentajeAcumulado = 0.0
            
            for (nota in notas) {
                promedioPonderadoAcumulado += nota.calificacion * (nota.porcentaje / 100.0)
                porcentajeAcumulado += nota.porcentaje
            }

            withContext(Dispatchers.Main) {
                currentMateria?.let { materia ->
                    binding.tvMateriaTitle.text = "${materia.nombreMateria} - ${materia.profesor}"
                }
                
                binding.tvPromedioActual.text = getString(R.string.promedio_actual, String.format(Locale.getDefault(), "%.2f", promedioPonderadoAcumulado))
                if (promedioPonderadoAcumulado >= 3.0) {
                    binding.tvPromedioActual.setTextColor(ContextCompat.getColor(this@DetalleMateriaActivity, R.color.primary_emerald))
                } else {
                    binding.tvPromedioActual.setTextColor(ContextCompat.getColor(this@DetalleMateriaActivity, R.color.badge_risk_text))
                }
                
                binding.tvPorcentajeEvaluado.text = getString(R.string.porcentaje_evaluado, String.format(Locale.getDefault(), "%.0f", porcentajeAcumulado))
                binding.progressAvance.progress = porcentajeAcumulado.toInt()
                
                adapter.submitList(notas)
                calcularNotaNecesaria()
            }
        }
    }
}
