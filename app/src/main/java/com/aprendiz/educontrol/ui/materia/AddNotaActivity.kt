package com.aprendiz.educontrol.ui.materia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.NotaEntity
import com.aprendiz.educontrol.databinding.ActivityAddNotaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotaBinding
    private lateinit var database: AppDatabase
    private var materiaId: Long = -1L
    private var porcentajeAcumulado: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        materiaId = intent.getLongExtra("MATERIA_ID", -1L)
        porcentajeAcumulado = intent.getDoubleExtra("PORCENTAJE_ACUMULADO", 0.0)

        if (materiaId == -1L) {
            finish()
            return
        }

        database = AppDatabase.getDatabase(this)

        binding.btnGuardarNota.setOnClickListener {
            val nombre = binding.etNombreEvaluacion.text.toString()
            val calificacionStr = binding.etCalificacion.text.toString()
            val porcentajeStr = binding.etPorcentaje.text.toString()

            if (nombre.isEmpty() || calificacionStr.isEmpty() || porcentajeStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val calificacion = calificacionStr.toDoubleOrNull() ?: 0.0
            val porcentaje = porcentajeStr.toDoubleOrNull() ?: 0.0
            
            if (calificacion < 0.0 || calificacion > 5.0) {
                binding.tilCalificacion.error = "Debe ser entre 0.0 y 5.0"
                return@setOnClickListener
            } else {
                binding.tilCalificacion.error = null
            }
            
            if (porcentaje <= 0.0 || porcentaje > 100.0) {
                binding.tilPorcentaje.error = "Debe ser entre 1 y 100"
                return@setOnClickListener
            } else {
                binding.tilPorcentaje.error = null
            }

            if (porcentajeAcumulado + porcentaje > 100.0) {
                val restante = 100.0 - porcentajeAcumulado
                binding.tilPorcentaje.error = getString(R.string.error_porcentaje_excedido, remainingToString(restante))
                return@setOnClickListener
            } else {
                binding.tilPorcentaje.error = null
            }

            val nuevaNota = NotaEntity(
                materiaId = materiaId,
                nombreEvaluacion = nombre,
                calificacion = calificacion,
                porcentaje = porcentaje
            )

            lifecycleScope.launch(Dispatchers.IO) {
                database.notaDao().insertNota(nuevaNota)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }
    }
    
    private fun remainingToString(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(java.util.Locale.getDefault(), "%.1f", value)
        }
    }
}
