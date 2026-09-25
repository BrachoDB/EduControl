package com.aprendiz.educontrol.ui.teacher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.databinding.ActivityTeacherMainBinding
import com.aprendiz.educontrol.ui.teacher.classes.TeacherClassesFragment
import com.aprendiz.educontrol.ui.teacher.home.TeacherHomeFragment
import com.aprendiz.educontrol.ui.teacher.performance.TeacherPerformanceFragment
import com.aprendiz.educontrol.ui.teacher.profile.TeacherProfileFragment

class TeacherMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(TeacherHomeFragment())
        }

        binding.teacherBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_teacher_home -> {
                    replaceFragment(TeacherHomeFragment())
                    true
                }
                R.id.nav_teacher_classes -> {
                    replaceFragment(TeacherClassesFragment())
                    true
                }
                R.id.nav_teacher_performance -> {
                    replaceFragment(TeacherPerformanceFragment())
                    true
                }
                R.id.nav_teacher_profile -> {
                    replaceFragment(TeacherProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.teacherFragmentContainer, fragment)
            .commit()
    }
}
