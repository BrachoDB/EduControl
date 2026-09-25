package com.aprendiz.educontrol.ui.teacher.performance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.repository.TeacherRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.ActivityTeacherStudentProfileBinding
import kotlinx.coroutines.launch
import java.util.Locale

class TeacherStudentProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherStudentProfileBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var breakdownAdapter: TeacherStudentBreakdownAdapter

    private var studentId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentId = intent.getLongExtra("STUDENT_ID", -1L)
        if (studentId == -1L) {
            finish()
            return
        }

        sessionManager = SessionManager(this)
        teacherRepository = TeacherRepository(this)

        setupAdapter()
        loadStudentProfile()
    }

    private fun setupAdapter() {
        breakdownAdapter = TeacherStudentBreakdownAdapter()
        binding.rvBreakdown.layoutManager = LinearLayoutManager(this)
        binding.rvBreakdown.adapter = breakdownAdapter
    }

    private fun loadStudentProfile() {
        val teacherId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val profile = teacherRepository.getTeacherStudentProfile(teacherId, studentId)
            if (profile != null) {
                binding.tvStudentName.text = profile.studentName
                binding.tvStudentEmail.text = profile.studentEmail
                binding.tvStudentAvatar.text = profile.avatarEmoji
                binding.tvOverallGPA.text = String.format(Locale.getDefault(), "%.2f", profile.promedioGeneralDocente)
                binding.tvTotalClassesBadge.text = "${profile.totalClasesMatriculadas} Materias"

                breakdownAdapter.submitList(profile.clasesDesglose)
            }
        }
    }
}
