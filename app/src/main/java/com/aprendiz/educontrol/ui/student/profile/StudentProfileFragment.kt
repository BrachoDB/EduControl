package com.aprendiz.educontrol.ui.student.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentStudentProfileBinding
import com.aprendiz.educontrol.ui.auth.DemoRoleActivity

class StudentProfileFragment : Fragment() {

    private var _binding: FragmentStudentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

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
        binding.tvProfileName.text = sessionManager.getCurrentUserName()

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
