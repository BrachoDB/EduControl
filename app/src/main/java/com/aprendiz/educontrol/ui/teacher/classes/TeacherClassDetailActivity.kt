package com.aprendiz.educontrol.ui.teacher.classes

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.entity.ActividadEntity
import com.aprendiz.educontrol.data.repository.TeacherActivityGradeModel
import com.aprendiz.educontrol.data.repository.TeacherRepository
import com.aprendiz.educontrol.databinding.ActivityTeacherClassDetailBinding
import kotlinx.coroutines.launch
import java.util.Locale

class TeacherClassDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherClassDetailBinding
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var studentAdapter: TeacherStudentAdapter
    private lateinit var gradebookAdapter: TeacherGradebookAdapter

    private var claseId: Long = -1L
    private var actividadesList = listOf<ActividadEntity>()
    private var selectedActividadId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherClassDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        claseId = intent.getLongExtra("CLASE_ID", -1L)
        if (claseId == -1L) {
            finish()
            return
        }

        teacherRepository = TeacherRepository(this)

        setupAdapters()
        loadClassDetail()
    }

    private fun setupAdapters() {
        studentAdapter = TeacherStudentAdapter { _ -> }
        binding.rvStudents.layoutManager = LinearLayoutManager(this)
        binding.rvStudents.adapter = studentAdapter

        gradebookAdapter = TeacherGradebookAdapter { item ->
            showGradeDialog(item)
        }
        binding.rvGradebook.layoutManager = LinearLayoutManager(this)
        binding.rvGradebook.adapter = gradebookAdapter
    }

    private fun loadClassDetail() {
        lifecycleScope.launch {
            val detail = teacherRepository.getTeacherClassDetail(claseId)
            if (detail != null) {
                binding.tvClassName.text = detail.nombreClase
                binding.tvClassCodeAndStudents.text = "Código: ${detail.codigoClase} • 👥 ${detail.studentCount} Estudiantes"
                binding.tvCourseAvg.text = String.format(Locale.getDefault(), "%.2f", detail.promedioCurso)
                binding.tvEvaluatedPercent.text = "${detail.porcentajeEvaluado.toInt()}% Evaluado"
                binding.progressAvance.progress = detail.porcentajeEvaluado.toInt()

                studentAdapter.submitList(detail.students)
                actividadesList = detail.actividades

                setupActivitySpinner()
            }
        }
    }

    private fun setupActivitySpinner() {
        if (actividadesList.isEmpty()) return

        val spinnerTitles = actividadesList.map { "${it.titulo} (${it.porcentaje.toInt()}%)" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerTitles)
        binding.spinnerActivities.adapter = adapter

        binding.spinnerActivities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedActividadId = actividadesList[position].id
                loadGradebookForActivity(selectedActividadId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (selectedActividadId == -1L && actividadesList.isNotEmpty()) {
            selectedActividadId = actividadesList[0].id
            loadGradebookForActivity(selectedActividadId)
        }
    }

    private fun loadGradebookForActivity(actividadId: Long) {
        lifecycleScope.launch {
            val grades = teacherRepository.getActivityGradesForClass(claseId, actividadId)
            gradebookAdapter.submitList(grades)
        }
    }

    private fun showGradeDialog(item: TeacherActivityGradeModel) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 24)
        }

        val etGrade = EditText(this).apply {
            hint = "Calificación (0.0 - 5.0)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            item.nota?.let { setText(it.toString()) }
        }

        val etFeedback = EditText(this).apply {
            hint = "Retroalimentación para ${item.studentName}"
            item.retroalimentacion?.let { setText(it) }
        }

        container.addView(etGrade)
        container.addView(etFeedback)

        AlertDialog.Builder(this)
            .setTitle("Calificar a ${item.studentName}")
            .setView(container)
            .setPositiveButton("Guardar") { _, _ ->
                val gradeStr = etGrade.text.toString().trim()
                val gradeVal = gradeStr.toDoubleOrNull()
                val feedbackStr = etFeedback.text.toString().trim()

                if (gradeVal == null || gradeVal < 0.0 || gradeVal > 5.0) {
                    Toast.makeText(this, "Debe ingresar una nota entre 0.0 y 5.0", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    teacherRepository.saveGrade(
                        actividadId = item.actividadId,
                        studentId = item.studentId,
                        nota = gradeVal,
                        retroalimentacion = feedbackStr
                    )
                    Toast.makeText(this@TeacherClassDetailActivity, "Calificación guardada", Toast.LENGTH_SHORT).show()
                    loadClassDetail()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
