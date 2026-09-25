package com.aprendiz.educontrol.ui.teacher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentTeacherHomeBinding

class TeacherHomeFragment : Fragment() {

    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

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
        binding.tvTeacherGreeting.text = "Buenos días, ${sessionManager.getCurrentUserName()} 👋"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
