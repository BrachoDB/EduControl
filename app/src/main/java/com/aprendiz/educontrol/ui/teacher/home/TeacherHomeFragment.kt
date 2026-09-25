package com.aprendiz.educontrol.ui.teacher.home

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
import com.aprendiz.educontrol.databinding.FragmentTeacherHomeBinding
import com.aprendiz.educontrol.ui.teacher.classes.TeacherClassDetailActivity
import kotlinx.coroutines.launch

class TeacherHomeFragment : Fragment() {

    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var teacherRepository: TeacherRepository
    private lateinit var teacherClassAdapter: TeacherClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        teacherRepository = TeacherRepository(requireContext())

        setupAdapter()
        loadTeacherDashboardData()
    }

    private fun setupAdapter() {
        teacherClassAdapter = TeacherClassAdapter { selectedClass ->
            val intent = Intent(requireContext(), TeacherClassDetailActivity::class.java)
            intent.putExtra("CLASE_ID", selectedClass.claseId)
            startActivity(intent)
        }
        binding.rvTeacherClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTeacherClasses.adapter = teacherClassAdapter
    }

    private fun loadTeacherDashboardData() {
        val teacherId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val data = teacherRepository.getTeacherDashboardData(teacherId)

            binding.tvTeacherGreeting.text = "Buenos días, ${data.teacherName} 👋"
            binding.tvClassesCount.text = "📚 ${data.totalClases} Clases"
            binding.tvStudentsCount.text = "👥 ${data.totalEstudiantes} Alumnos"
            binding.tvActivitiesCount.text = "📝 ${data.totalActividades} Tareas"

            if (data.pendingGradingCount > 0) {
                binding.cardPendingGradingAlert.visibility = View.VISIBLE
                binding.tvPendingGradingText.text = "Tienes ${data.pendingGradingCount} entregas pendientes por calificar."
            } else {
                binding.cardPendingGradingAlert.visibility = View.GONE
            }

            teacherClassAdapter.submitList(data.clases)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTeacherDashboardData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
