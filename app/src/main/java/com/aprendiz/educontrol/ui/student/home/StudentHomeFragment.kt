package com.aprendiz.educontrol.ui.student.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.FragmentStudentHomeBinding

class StudentHomeFragment : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

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
        binding.tvGreeting.text = "Buenos días, ${sessionManager.getCurrentUserName()} 👋"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
