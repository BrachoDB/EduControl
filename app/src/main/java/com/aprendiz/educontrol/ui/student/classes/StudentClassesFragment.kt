package com.aprendiz.educontrol.ui.student.classes

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
import com.aprendiz.educontrol.databinding.FragmentStudentClassesBinding
import com.aprendiz.educontrol.ui.materia.DetalleMateriaActivity
import com.aprendiz.educontrol.ui.student.home.StudentClassAdapter
import kotlinx.coroutines.launch

class StudentClassesFragment : Fragment() {

    private var _binding: FragmentStudentClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository
    private lateinit var classAdapter: StudentClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        studentRepository = StudentRepository(requireContext())

        setupAdapter()
        loadClassesData()
    }

    private fun setupAdapter() {
        classAdapter = StudentClassAdapter { selectedClass ->
            val intent = Intent(requireContext(), DetalleMateriaActivity::class.java)
            intent.putExtra("CLASE_ID", selectedClass.claseId)
            intent.putExtra("MATERIA_ID", selectedClass.claseId)
            startActivity(intent)
        }
        binding.rvStudentClassesTab.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudentClassesTab.adapter = classAdapter
    }

    private fun loadClassesData() {
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val dashboardData = studentRepository.getStudentDashboardData(studentId)
            classAdapter.submitList(dashboardData.classes)
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
