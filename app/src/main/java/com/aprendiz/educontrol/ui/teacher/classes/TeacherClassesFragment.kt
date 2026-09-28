package com.aprendiz.educontrol.ui.teacher.classes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.data.repository.TeacherRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentTeacherClassesBinding
import com.aprendiz.educontrol.ui.teacher.home.TeacherClassAdapter
import kotlinx.coroutines.launch

class TeacherClassesFragment : Fragment() {

    private var _binding: FragmentTeacherClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var classAdapter: TeacherClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        teacherRepository = TeacherRepository(requireContext())

        setupAdapter()
        loadClassesData()
    }

    private fun setupAdapter() {
        classAdapter = TeacherClassAdapter { selectedClass ->
            val intent = Intent(requireContext(), TeacherClassDetailActivity::class.java)
            intent.putExtra("CLASE_ID", selectedClass.claseId)
            startActivity(intent)
        }
        binding.rvTeacherClassesTab.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTeacherClassesTab.adapter = classAdapter
    }

    private fun loadClassesData() {
        val teacherId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val dashboardData = teacherRepository.getTeacherDashboardData(teacherId)
            classAdapter.submitList(dashboardData.clases)
        }
    }

    override fun onResume() {
        super.onResume()
        loadClassesData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
