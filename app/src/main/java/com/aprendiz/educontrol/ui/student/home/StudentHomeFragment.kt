package com.aprendiz.educontrol.ui.student.home

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
import com.aprendiz.educontrol.databinding.FragmentStudentHomeBinding
import com.aprendiz.educontrol.ui.materia.DetalleMateriaActivity
import kotlinx.coroutines.launch
import java.util.Locale

class StudentHomeFragment : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository
    private lateinit var classAdapter: StudentClassAdapter
    private lateinit var upcomingAdapter: StudentUpcomingActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        studentRepository = StudentRepository(requireContext())

        setupAdapters()
        loadDashboardData()
    }

    private fun setupAdapters() {
        classAdapter = StudentClassAdapter { selectedClass ->
            val intent = Intent(requireContext(), DetalleMateriaActivity::class.java)
            intent.putExtra("MATERIA_ID", selectedClass.claseId)
            startActivity(intent)
        }
        binding.rvClasses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvClasses.adapter = classAdapter

        upcomingAdapter = StudentUpcomingActivityAdapter { _ ->
            // Click on upcoming activity
        }
        binding.rvUpcomingActivities.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingActivities.adapter = upcomingAdapter
    }

    private fun loadDashboardData() {
        val studentId = sessionManager.getCurrentUserId()
        lifecycleScope.launch {
            val data = studentRepository.getStudentDashboardData(studentId)

            binding.tvGreeting.text = "Buenos días, ${data.studentName} 👋"
            binding.tvGPA.text = String.format(Locale.getDefault(), "%.2f", data.promedioGeneral)
            binding.tvEvaluatedSummary.text = "${data.porcentajeEvaluadoGlobal.toInt()}% Evaluado"

            if (data.alertMessage != null) {
                binding.cardAlert.visibility = View.VISIBLE
                binding.tvAlertText.text = data.alertMessage
            } else {
                binding.cardAlert.visibility = View.GONE
            }

            classAdapter.submitList(data.classes)
            upcomingAdapter.submitList(data.upcomingActivities)
        }
    }

    override fun onResume() {
        super.onResume()
        loadDashboardData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
