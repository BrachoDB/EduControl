package com.aprendiz.educontrol.ui.teacher.performance

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.repository.TeacherRepository
import com.aprendiz.educontrol.data.repository.TeacherStudentSummaryModel
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentTeacherPerformanceBinding
import com.aprendiz.educontrol.ui.teacher.classes.TeacherStudentAdapter
import kotlinx.coroutines.launch

class TeacherPerformanceFragment : Fragment() {

    private var _binding: FragmentTeacherPerformanceBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var studentAdapter: TeacherStudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherPerformanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        teacherRepository = TeacherRepository(requireContext())

        setupAdapter()
        loadPerformanceData()
    }

    private fun setupAdapter() {
        studentAdapter = TeacherStudentAdapter { student ->
            val intent = Intent(requireContext(), TeacherStudentProfileActivity::class.java)
            intent.putExtra("STUDENT_ID", student.studentId)
            startActivity(intent)
        }
        binding.rvPerformanceStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPerformanceStudents.adapter = studentAdapter
    }

    private fun loadPerformanceData() {
        val teacherId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val dashboardData = teacherRepository.getTeacherDashboardData(teacherId)
            val allStudentsMap = mutableMapOf<Long, TeacherStudentSummaryModel>()

            for (clase in dashboardData.clases) {
                val detail = teacherRepository.getTeacherClassDetail(clase.claseId)
                if (detail != null) {
                    for (student in detail.students) {
                        allStudentsMap[student.studentId] = student
                    }
                }
            }

            studentAdapter.submitList(allStudentsMap.values.toList())
        }
    }

    override fun onResume() {
        super.onResume()
        loadPerformanceData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
