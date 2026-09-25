package com.aprendiz.educontrol.ui.student.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.repository.StudentRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentStudentActivitiesBinding
import kotlinx.coroutines.launch

class StudentActivitiesFragment : Fragment() {

    private var _binding: FragmentStudentActivitiesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository
    private lateinit var activityAdapter: StudentActivityListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        studentRepository = StudentRepository(requireContext())

        setupAdapter()
        loadActivitiesData()
    }

    private fun setupAdapter() {
        activityAdapter = StudentActivityListAdapter { selectedActivity ->
            val intent = Intent(requireContext(), ActivityDetailActivity::class.java)
            intent.putExtra("ACTIVIDAD_ID", selectedActivity.actividadId)
            startActivity(intent)
        }
        binding.rvStudentActivities.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudentActivities.adapter = activityAdapter
    }

    private fun loadActivitiesData() {
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val activities = studentRepository.getStudentActivitiesList(studentId)
            activityAdapter.submitList(activities)
        }
    }

    override fun onResume() {
        super.onResume()
        loadActivitiesData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
