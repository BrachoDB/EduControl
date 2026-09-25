package com.aprendiz.educontrol.ui.student.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.repository.StudentClassSummaryModel
import com.aprendiz.educontrol.databinding.ItemStudentClassBinding
import java.util.Locale

class StudentClassAdapter(
    private val onClassClick: (StudentClassSummaryModel) -> Unit
) : RecyclerView.Adapter<StudentClassAdapter.ViewHolder>() {

    private val classes = mutableListOf<StudentClassSummaryModel>()

    fun submitList(newList: List<StudentClassSummaryModel>) {
        classes.clear()
        classes.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentClassBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classes[position])
    }

    override fun getItemCount(): Int = classes.size

    inner class ViewHolder(private val binding: ItemStudentClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentClassSummaryModel) {
            val context = binding.root.context
            binding.tvClassName.text = item.nombreClase
            binding.tvTeacherName.text = item.profesor

            binding.tvGradeBadge.text = String.format(Locale.getDefault(), "%.2f", item.promedio)
            if (item.promedio >= 3.0) {
                binding.tvGradeBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.tvGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.tvGradeBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.tvGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }

            binding.progressEvaluated.progress = item.porcentajeEvaluado.toInt()
            binding.tvEvaluatedPercent.text = "${item.porcentajeEvaluado.toInt()}% Evaluado"

            binding.root.setOnClickListener {
                onClassClick(item)
            }
        }
    }
}
