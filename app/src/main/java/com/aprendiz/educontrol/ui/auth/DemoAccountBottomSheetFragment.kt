package com.aprendiz.educontrol.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.BottomSheetDemoAccountBinding
import com.aprendiz.educontrol.ui.student.StudentMainActivity
import com.aprendiz.educontrol.ui.teacher.TeacherMainActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DemoAccountBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDemoAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private var targetRole: String = SessionManager.ROLE_STUDENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        targetRole = arguments?.getString(ARG_ROLE) ?: SessionManager.ROLE_STUDENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDemoAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val accounts = getDemoAccountsForRole(targetRole)

        binding.tvBottomSheetTitle.text = if (targetRole == SessionManager.ROLE_STUDENT) {
            getString(R.string.seleccionar_estudiante_demo)
        } else {
            getString(R.string.seleccionar_profesor_demo)
        }

        val adapter = DemoAccountAdapter(accounts) { selectedAccount ->
            sessionManager.saveSession(
                userId = selectedAccount.id,
                role = selectedAccount.role,
                userName = selectedAccount.name,
                email = selectedAccount.email
            )

            val intent = if (selectedAccount.role == SessionManager.ROLE_STUDENT) {
                Intent(requireContext(), StudentMainActivity::class.java)
            } else {
                Intent(requireContext(), TeacherMainActivity::class.java)
            }

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            dismiss()
        }

        binding.rvDemoAccounts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDemoAccounts.adapter = adapter
    }

    private fun getDemoAccountsForRole(role: String): List<DemoAccountModel> {
        return if (role == SessionManager.ROLE_STUDENT) {
            listOf(
                DemoAccountModel(1L, "Santiago", SessionManager.ROLE_STUDENT, "Alto rendimiento • Promedio: 4.4", "santiago@educontrol.com", "👨‍🎓"),
                DemoAccountModel(2L, "Laura", SessionManager.ROLE_STUDENT, "Rendimiento medio • Promedio: 3.5", "laura@educontrol.com", "👩‍🎓"),
                DemoAccountModel(3L, "Carlos", SessionManager.ROLE_STUDENT, "En riesgo académico • Promedio: 2.7", "carlos@educontrol.com", "👨‍🎓"),
                DemoAccountModel(4L, "Valentina", SessionManager.ROLE_STUDENT, "Meta alcanzable • Promedio: 3.4", "valentina@educontrol.com", "👩‍🎓"),
                DemoAccountModel(5L, "Mateo", SessionManager.ROLE_STUDENT, "Meta no alcanzable • Promedio: 2.6", "mateo@educontrol.com", "👨‍🎓")
            )
        } else {
            listOf(
                DemoAccountModel(101L, "Prof. Andrés", SessionManager.ROLE_TEACHER, "Matemáticas 10° y Física 10°", "andres@educontrol.com", "👨‍🏫"),
                DemoAccountModel(102L, "Prof. Carolina", SessionManager.ROLE_TEACHER, "Inglés 10° y Química 10°", "carolina@educontrol.com", "👩‍🏫")
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ROLE = "target_role"

        fun newInstance(role: String): DemoAccountBottomSheetFragment {
            val fragment = DemoAccountBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_ROLE, role)
            fragment.arguments = args
            return fragment
        }
    }
}
