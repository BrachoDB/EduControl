package com.aprendiz.educontrol.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.UserEntity
import com.aprendiz.educontrol.databinding.ActivitySplashBinding
import com.aprendiz.educontrol.ui.auth.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            val existing = db.userDao().getUserByEmail("test@educontrol.com")
            if (existing == null) {
                db.userDao().insertUser(UserEntity(nombre = "Test User", email = "test@educontrol.com", password = "123456"))
            }
        }

        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}
