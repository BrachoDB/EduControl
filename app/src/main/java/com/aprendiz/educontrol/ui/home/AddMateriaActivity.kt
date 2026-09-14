package com.aprendiz.educontrol.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.MateriaEntity
import com.aprendiz.educontrol.databinding.ActivityAddMateriaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddMateriaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMateriaBinding
    private lateinit var database: AppDatabase
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", -1L)
        if (userId == -1L) {
            finish()
            return
        }

        database = AppDatabase.getDatabase(this)

        binding.btnGuardarMateria.setOnClickListener {
            val nombre = binding.etNombreMateria.text.toString()
            val profesor = binding.etProfesor.text.toString()

            if (nombre.isEmpty() || profesor.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaMateria = MateriaEntity(
                userId = userId,
                nombreMateria = nombre,
                profesor = profesor
            )

            lifecycleScope.launch(Dispatchers.IO) {
                database.materiaDao().insertMateria(nuevaMateria)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
}
