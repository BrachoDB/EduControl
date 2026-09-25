package com.aprendiz.educontrol.ui.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.databinding.ActivityStudentMainBinding
import com.aprendiz.educontrol.ui.student.activities.StudentActivitiesFragment
import com.aprendiz.educontrol.ui.student.classes.StudentClassesFragment
import com.aprendiz.educontrol.ui.student.goals.StudentGoalsFragment
import com.aprendiz.educontrol.ui.student.home.StudentHomeFragment
import com.aprendiz.educontrol.ui.student.profile.StudentProfileFragment

class StudentMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(StudentHomeFragment())
        }

        binding.studentBottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_student_home -> {
                    replaceFragment(StudentHomeFragment())
                    true
                }
                R.id.nav_student_classes -> {
                    replaceFragment(StudentClassesFragment())
                    true
                }
                R.id.nav_student_activities -> {
                    replaceFragment(StudentActivitiesFragment())
                    true
                }
                R.id.nav_student_goals -> {
                    replaceFragment(StudentGoalsFragment())
                    true
                }
                R.id.nav_student_profile -> {
                    replaceFragment(StudentProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.studentFragmentContainer, fragment)
            .commit()
    }
}
