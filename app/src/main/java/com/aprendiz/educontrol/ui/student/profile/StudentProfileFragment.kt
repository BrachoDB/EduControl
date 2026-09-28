package com.aprendiz.educontrol.ui.student.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.data.repository.StudentRepository
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentStudentProfileBinding
import com.aprendiz.educontrol.ui.auth.DemoRoleActivity
import kotlinx.coroutines.launch
import java.util.Locale

class StudentProfileFragment : Fragment() {

    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var studentRepository: StudentRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        studentRepository = StudentRepository(requireContext())

        val studentId = sessionManager.getCurrentUserId()
        binding.tvProfileName.text = sessionManager.getCurrentUserName()
        binding.tvProfileEmail.text = sessionManager.getCurrentEmail()

        lifecycleScope.launch {
            val user = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                com.aprendiz.educontrol.data.AppDatabase.getDatabase(requireContext()).userDao().getUserById(studentId)
            }
            if (user != null) {
                binding.tvStudentAvatar.text = user.avatarEmoji
            }
            val dashboardData = studentRepository.getStudentDashboardData(studentId)
            binding.tvClassesCount.text = "${dashboardData.classes.size}"
            binding.tvOverallGPA.text = String.format(Locale.getDefault(), "%.2f", dashboardData.promedioGeneral)
        }

        binding.btnLogout.setOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(requireContext(), DemoRoleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
