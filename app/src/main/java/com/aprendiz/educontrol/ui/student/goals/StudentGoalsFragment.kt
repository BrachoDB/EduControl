package com.aprendiz.educontrol.ui.student.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.repository.StudentRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentStudentGoalsBinding
import kotlinx.coroutines.launch

class StudentGoalsFragment : Fragment() {

    private var _binding: FragmentStudentGoalsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository
    private lateinit var goalAdapter: StudentGoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        studentRepository = StudentRepository(requireContext())

        setupAdapter()
        loadGoalsData()
    }

    private fun setupAdapter() {
        goalAdapter = StudentGoalAdapter()
        binding.rvGoalProjections.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGoalProjections.adapter = goalAdapter
    }

    private fun loadGoalsData() {
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val dashboardData = studentRepository.getStudentDashboardData(studentId)
            goalAdapter.submitList(dashboardData.classes)
        }
    }

    override fun onResume() {
        super.onResume()
        loadGoalsData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
