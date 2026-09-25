package com.aprendiz.educontrol.ui.teacher.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.repository.TeacherClassSummaryModel
import com.aprendiz.educontrol.databinding.ItemTeacherClassBinding
import java.util.Locale

class TeacherClassAdapter(
    private val onClassClick: (TeacherClassSummaryModel) -> Unit
) : RecyclerView.Adapter<TeacherClassAdapter.ViewHolder>() {

    private val classes = mutableListOf<TeacherClassSummaryModel>()

    fun submitList(newList: List<TeacherClassSummaryModel>) {
        classes.clear()
        classes.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeacherClassBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classes[position])
    }

    override fun getItemCount(): Int = classes.size

    inner class ViewHolder(private val binding: ItemTeacherClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeacherClassSummaryModel) {
            val context = binding.root.context
            binding.tvClassName.text = item.nombreClase
            binding.tvClassCodeAndStudents.text = "Código: ${item.codigoClase} • 👥 ${item.studentCount} Estudiantes"

            binding.tvCourseAvgBadge.text = String.format(Locale.getDefault(), "%.2f", item.promedioCurso)
            if (item.promedioCurso >= 3.0) {
                binding.tvCourseAvgBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.tvCourseAvgBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.tvCourseAvgBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.tvCourseAvgBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }

            binding.progressEvaluated.progress = item.porcentajeEvaluado.toInt()
            binding.tvEvaluatedPercent.text = "${item.porcentajeEvaluado.toInt()}% Evaluado"

            binding.root.setOnClickListener {
                onClassClick(item)
            }
        }
    }
}
