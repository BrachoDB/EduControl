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
import com.aprendiz.educontrol.data.repository.StudentRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.ActivityDetalleMateriaBinding
import com.aprendiz.educontrol.ui.student.classes.TimelineAdapter
import kotlinx.coroutines.launch
import java.util.Locale

class DetalleMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleMateriaBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository
    private lateinit var timelineAdapter: TimelineAdapter

    private var materiaId: Long = -1L
    private var porcentajeAcumulado = 0.0
    private var promedioPonderadoAcumulado = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getLongExtra("MATERIA_ID", -1L)
        if (materiaId == -1L) {
            materiaId = intent.getLongExtra("CLASE_ID", -1L)
        }

        if (materiaId == -1L) {
            finish()
            return
        }

        sessionManager = SessionManager(this)
        studentRepository = StudentRepository(this)

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
        timelineAdapter = TimelineAdapter()
        binding.rvTimeline.layoutManager = LinearLayoutManager(this)
        binding.rvTimeline.adapter = timelineAdapter
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
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val detail = studentRepository.getStudentClassDetail(studentId, materiaId)
            if (detail != null) {
                binding.tvMateriaTitle.text = detail.nombreClase
                binding.tvProfesor.text = detail.profesor

                promedioPonderadoAcumulado = detail.promedioActual
                porcentajeAcumulado = detail.porcentajeEvaluado

                binding.tvPromedioActual.text = String.format(Locale.getDefault(), "%.2f", promedioPonderadoAcumulado)
                binding.tvPorcentajeEvaluado.text = "${porcentajeAcumulado.toInt()}% Evaluado"
                binding.progressAvance.progress = porcentajeAcumulado.toInt()

                timelineAdapter.submitList(detail.timelineItems)
                calcularNotaNecesaria()
            }
        }
    }
}
