package com.aprendiz.educontrol.ui.teacher.performance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.databinding.FragmentTeacherPerformanceBinding

class TeacherPerformanceFragment : Fragment() {

    private var _binding: FragmentTeacherPerformanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherPerformanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
