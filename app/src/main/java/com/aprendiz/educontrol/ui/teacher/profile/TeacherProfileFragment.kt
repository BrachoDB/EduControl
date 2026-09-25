package com.aprendiz.educontrol.ui.teacher.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentTeacherProfileBinding
import com.aprendiz.educontrol.ui.auth.DemoRoleActivity

class TeacherProfileFragment : Fragment() {

    private var _binding: FragmentTeacherProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        binding.tvTeacherProfileName.text = sessionManager.getCurrentUserName()

        binding.btnTeacherLogout.setOnClickListener {
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
