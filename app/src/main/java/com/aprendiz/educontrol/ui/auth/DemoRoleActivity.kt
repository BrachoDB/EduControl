package com.aprendiz.educontrol.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.data.seeder.DemoDataSeeder
import com.aprendiz.educontrol.data.session.SessionManager
import com.aprendiz.educontrol.databinding.ActivityDemoRoleBinding
import com.aprendiz.educontrol.ui.student.StudentMainActivity
import com.aprendiz.educontrol.ui.teacher.TeacherMainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DemoRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDemoRoleBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        // Seed demo data asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            DemoDataSeeder.seedDatabaseIfNeeded(applicationContext)
        }

        if (sessionManager.isLoggedIn()) {
            val role = sessionManager.getCurrentRole()
            val intent = if (role == SessionManager.ROLE_STUDENT) {
                Intent(this, StudentMainActivity::class.java)
            } else {
                Intent(this, TeacherMainActivity::class.java)
            }
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityDemoRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardEstudiante.setOnClickListener {
            val bottomSheet = DemoAccountBottomSheetFragment.newInstance(SessionManager.ROLE_STUDENT)
            bottomSheet.show(supportFragmentManager, "demo_student_sheet")
        }

        binding.cardProfesor.setOnClickListener {
            val bottomSheet = DemoAccountBottomSheetFragment.newInstance(SessionManager.ROLE_TEACHER)
            bottomSheet.show(supportFragmentManager, "demo_teacher_sheet")
        }
    }
}
