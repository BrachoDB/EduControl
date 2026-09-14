package com.aprendiz.educontrol.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.entity.MateriaEntity
import com.aprendiz.educontrol.databinding.ItemMateriaBinding

class HomeAdapter(
    private val onItemClick: (MateriaEntity) -> Unit,
    private val onOptionsClick: (MateriaEntity, android.view.View) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val materias = mutableListOf<MateriaEntity>()
    private val promedios = mutableMapOf<Long, Double>()

    fun submitList(list: List<MateriaEntity>, mapPromedios: Map<Long, Double>) {
        materias.clear()
        materias.addAll(list)
        promedios.clear()
        promedios.putAll(mapPromedios)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMateriaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materia = materias[position]
        val promedio = promedios[materia.id] ?: 0.0
        holder.bind(materia, promedio)
    }

    override fun getItemCount(): Int = materias.size

    inner class ViewHolder(private val binding: ItemMateriaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(materia: MateriaEntity, promedio: Double) {
            binding.tvNombreMateria.text = materia.nombreMateria
            binding.tvProfesor.text = materia.profesor
            
            // Format to 2 decimal places
            binding.chipPromedio.text = String.format("%.2f", promedio)
            
            val context = binding.root.context
            if (promedio >= 3.0) {
                binding.chipPromedio.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.chipPromedio.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.chipPromedio.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.chipPromedio.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }

            binding.root.setOnClickListener {
                onItemClick(materia)
            }
            
            binding.ivOptions.setOnClickListener { view ->
                onOptionsClick(materia, view)
            }
        }
    }
}
