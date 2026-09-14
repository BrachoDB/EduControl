package com.aprendiz.educontrol.ui.materia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.entity.NotaEntity
import com.aprendiz.educontrol.databinding.ItemNotaBinding
import java.util.Locale

class NotasAdapter : RecyclerView.Adapter<NotasAdapter.ViewHolder>() {

    private val notas = mutableListOf<NotaEntity>()

    fun submitList(list: List<NotaEntity>) {
        notas.clear()
        notas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nota = notas[position]
        holder.bind(nota)
    }

    override fun getItemCount(): Int = notas.size

    inner class ViewHolder(private val binding: ItemNotaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(nota: NotaEntity) {
            binding.tvNombreEvaluacion.text = nota.nombreEvaluacion
            binding.tvPorcentaje.text = "${nota.porcentaje}%"
            binding.tvCalificacion.text = String.format(Locale.getDefault(), "%.1f", nota.calificacion)
            
            val context = binding.root.context
            if (nota.calificacion >= 3.0) {
                binding.tvCalificacion.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.tvCalificacion.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }
        }
    }
}
