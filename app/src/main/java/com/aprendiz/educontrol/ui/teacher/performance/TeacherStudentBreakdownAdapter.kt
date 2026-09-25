package com.aprendiz.educontrol.ui.teacher.performance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.repository.StudentClassBreakdownModel
import com.aprendiz.educontrol.databinding.ItemStudentClassBreakdownBinding
import java.util.Locale

class TeacherStudentBreakdownAdapter : RecyclerView.Adapter<TeacherStudentBreakdownAdapter.ViewHolder>() {

    private val items = mutableListOf<StudentClassBreakdownModel>()

    fun submitList(newList: List<StudentClassBreakdownModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentClassBreakdownBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemStudentClassBreakdownBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentClassBreakdownModel) {
            val context = binding.root.context
            binding.tvClassName.text = item.nombreClase
            binding.tvClassCode.text = "Código: ${item.codigoClase}"

            binding.tvClassGradeBadge.text = String.format(Locale.getDefault(), "%.2f", item.promedioMateria)
            if (item.promedioMateria >= 3.0) {
                binding.tvClassGradeBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.tvClassGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.tvClassGradeBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.tvClassGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }

            val sb = StringBuilder()
            for (act in item.actividades) {
                val notaStr = if (act.nota != null) String.format(Locale.getDefault(), "%.2f", act.nota) else "Pendiente"
                sb.append("• ${act.titulo}: $notaStr (${act.porcentaje.toInt()}%)\n")
            }
            binding.tvActivitiesList.text = sb.toString().trim()
        }
    }
}
