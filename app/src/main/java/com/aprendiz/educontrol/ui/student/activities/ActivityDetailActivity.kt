package com.aprendiz.educontrol.ui.student.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.entity.ActividadEntity
import com.aprendiz.educontrol.data.entity.EntregaEntity
import com.aprendiz.educontrol.data.entity.PreguntaQuizEntity
import com.aprendiz.educontrol.data.repository.StudentActivityItemModel
import com.aprendiz.educontrol.data.repository.StudentRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.ActivityDetailBinding
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch
import java.util.Locale

class ActivityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository

    private var actividadId: Long = -1L
    private var currentActivityItem: StudentActivityItemModel? = null
    private var quizQuestions = listOf<PreguntaQuizEntity>()
    private val selectedQuizAnswers = mutableMapOf<Long, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actividadId = intent.getLongExtra("ACTIVIDAD_ID", -1L)
        if (actividadId == -1L) {
            finish()
            return
        }

        sessionManager = SessionManager(this)
        studentRepository = StudentRepository(this)

        loadActivityDetails()
    }

    private fun loadActivityDetails() {
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val activities = studentRepository.getStudentActivitiesList(studentId)
            currentActivityItem = activities.firstOrNull { it.actividadId == actividadId }

            currentActivityItem?.let { item ->
                binding.tvActivityTitle.text = item.titulo
                binding.tvClassNameAndDate.text = "${item.nombreClase} • ${item.fechaEntrega}"
                binding.tvWeightBadge.text = "Peso: ${item.porcentaje.toInt()}%"
                binding.tvDescription.text = item.descripcion

                binding.tvActivityIcon.text = when (item.tipo) {
                    ActividadEntity.TYPE_QUIZ -> "🧪"
                    ActividadEntity.TYPE_EXAM -> "📚"
                    else -> "📝"
                }

                if (item.nota != null) {
                    binding.tvGradeOrStatusBadge.text = String.format(Locale.getDefault(), "%.2f", item.nota)
                    binding.tvGradeOrStatusBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                    binding.tvGradeOrStatusBadge.setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.badge_approved_text))

                    binding.cardFeedback.visibility = View.VISIBLE
                    binding.tvFeedbackTitle.text = "✔ Calificación: ${String.format(Locale.getDefault(), "%.2f / 5.0", item.nota)}"
                    binding.tvFeedbackText.text = item.retroalimentacion ?: "Excelente trabajo."
                } else if (item.estado == EntregaEntity.STATUS_SUBMITTED) {
                    binding.tvGradeOrStatusBadge.text = "Entregado"
                    binding.tvGradeOrStatusBadge.setBackgroundResource(R.drawable.bg_chip_completed)
                    binding.tvGradeOrStatusBadge.setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.badge_completed_text))
                    binding.cardFeedback.visibility = View.GONE
                } else {
                    binding.tvGradeOrStatusBadge.text = "Pendiente"
                    binding.tvGradeOrStatusBadge.setBackgroundResource(R.drawable.bg_chip_pending)
                    binding.tvGradeOrStatusBadge.setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.badge_pending_text))
                    binding.cardFeedback.visibility = View.GONE
                }

                if (item.tipo == ActividadEntity.TYPE_QUIZ) {
                    binding.layoutTaskSubmission.visibility = View.GONE
                    binding.layoutQuizSection.visibility = View.VISIBLE
                    setupQuizView(studentId, item)
                } else {
                    binding.layoutTaskSubmission.visibility = View.VISIBLE
                    binding.layoutQuizSection.visibility = View.GONE
                    setupTaskView(studentId, item)
                }
            }
        }
    }

    private fun setupTaskView(studentId: Long, item: StudentActivityItemModel) {
        if (!item.contenidoRespuesta.isNullOrEmpty()) {
            binding.etTaskAnswer.setText(item.contenidoRespuesta)
            if (item.estado == EntregaEntity.STATUS_GRADED || item.estado == EntregaEntity.STATUS_SUBMITTED) {
                binding.etTaskAnswer.isEnabled = false
                binding.btnSubmitTask.visibility = View.GONE
            }
        }

        binding.btnSubmitTask.setOnClickListener {
            val answerText = binding.etTaskAnswer.text.toString().trim()
            if (answerText.isEmpty()) {
                Toast.makeText(this, "Escribe tu solución antes de enviar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                studentRepository.submitTask(studentId, actividadId, answerText)
                Toast.makeText(this@ActivityDetailActivity, "Tarea enviada correctamente", Toast.LENGTH_SHORT).show()
                loadActivityDetails()
            }
        }
    }

    private fun setupQuizView(studentId: Long, item: StudentActivityItemModel) {
        lifecycleScope.launch {
            quizQuestions = studentRepository.getQuestionsForQuiz(actividadId)
            binding.containerQuizQuestions.removeAllViews()

            if (quizQuestions.isEmpty()) {
                val tvEmpty = TextView(this@ActivityDetailActivity).apply {
                    text = "Este cuestionario no tiene preguntas registradas."
                    setPadding(16, 16, 16, 16)
                }
                binding.containerQuizQuestions.addView(tvEmpty)
                binding.btnSubmitQuiz.visibility = View.GONE
                return@launch
            }

            if (item.estado == EntregaEntity.STATUS_GRADED) {
                binding.btnSubmitQuiz.visibility = View.GONE
            } else {
                binding.btnSubmitQuiz.visibility = View.VISIBLE
            }

            for ((index, q) in quizQuestions.withIndex()) {
                val cardQuestion = MaterialCardView(this@ActivityDetailActivity).apply {
                    radius = 32f
                    strokeWidth = 2
                    strokeColor = ContextCompat.getColor(this@ActivityDetailActivity, R.color.card_stroke)
                    setCardBackgroundColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.card_surface))
                    val lp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.setMargins(0, 0, 0, 24)
                    layoutParams = lp
                }

                val layoutInner = LinearLayout(this@ActivityDetailActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(24, 24, 24, 24)
                }

                val tvQuestionTitle = TextView(this@ActivityDetailActivity).apply {
                    text = "Pregunta ${index + 1}: ${q.enunciado}"
                    textSize = 15f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.navy_hero))
                }
                layoutInner.addView(tvQuestionTitle)

                val radioGroup = RadioGroup(this@ActivityDetailActivity).apply {
                    orientation = RadioGroup.VERTICAL
                    setPadding(0, 12, 0, 0)
                }

                val options = listOf(q.opcionA, q.opcionB, q.opcionC, q.opcionD)
                for ((opIndex, optionText) in options.withIndex()) {
                    val rb = RadioButton(this@ActivityDetailActivity).apply {
                        id = View.generateViewId()
                        text = optionText
                        textSize = 14f
                        setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.text_primary))
                        if (item.estado == EntregaEntity.STATUS_GRADED) {
                            isEnabled = false
                            if (opIndex == q.opcionCorrecta) {
                                text = "$optionText ✔ (Correcta)"
                                setTextColor(ContextCompat.getColor(this@ActivityDetailActivity, R.color.badge_approved_text))
                            }
                        }
                    }
                    radioGroup.addView(rb)
                }

                radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    val selectedRb = radioGroup.findViewById<RadioButton>(checkedId)
                    val selectedIndex = radioGroup.indexOfChild(selectedRb)
                    if (selectedIndex != -1) {
                        selectedQuizAnswers[q.id] = selectedIndex
                    }
                }

                layoutInner.addView(radioGroup)
                cardQuestion.addView(layoutInner)
                binding.containerQuizQuestions.addView(cardQuestion)
            }

            binding.btnSubmitQuiz.setOnClickListener {
                if (selectedQuizAnswers.size < quizQuestions.size) {
                    Toast.makeText(this@ActivityDetailActivity, "Por favor responde todas las preguntas del quiz", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val finalGrade = studentRepository.submitQuiz(studentId, actividadId, selectedQuizAnswers)
                    AlertDialog.Builder(this@ActivityDetailActivity)
                        .setTitle("🎉 Quiz Autocorregido")
                        .setMessage("Obtuviste una calificación de ${String.format(Locale.getDefault(), "%.2f", finalGrade)} / 5.0")
                        .setPositiveButton("Aceptar") { _, _ ->
                            loadActivityDetails()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }
}
