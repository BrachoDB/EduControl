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
    private var materiaId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMateriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("USER_ID", -1L)
        materiaId = intent.getLongExtra("MATERIA_ID", -1L)
        
        if (userId == -1L) {
            finish()
            return
        }

        database = AppDatabase.getDatabase(this)
        
        if (materiaId != -1L) {
            binding.tvTitle.text = getString(R.string.editar_materia)
            loadMateria()
        }

        binding.btnGuardarMateria.setOnClickListener {
            guardarMateria()
        }
    }
    
    private fun loadMateria() {
        lifecycleScope.launch(Dispatchers.IO) {
            val materia = database.materiaDao().getMateriaById(materiaId)
            withContext(Dispatchers.Main) {
                materia?.let {
                    binding.etNombreMateria.setText(it.nombreMateria)
                    binding.etProfesor.setText(it.profesor)
                }
            }
        }
    }

    private fun guardarMateria() {
        val nombre = binding.etNombreMateria.text.toString().trim()
        val profesor = binding.etProfesor.text.toString().trim()

        if (nombre.isEmpty() || profesor.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
            return
        }

        val materia = MateriaEntity(
            id = if (materiaId != -1L) materiaId else 0L,
            userId = userId,
            nombreMateria = nombre,
            profesor = profesor
        )

        lifecycleScope.launch(Dispatchers.IO) {
            if (materiaId != -1L) {
                database.materiaDao().updateMateria(materia)
            } else {
                database.materiaDao().insertMateria(materia)
            }
            withContext(Dispatchers.Main) {
                if (materiaId != -1L) {
                    Toast.makeText(this@AddMateriaActivity, getString(R.string.materia_actualizada), Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }
}
