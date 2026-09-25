package com.aprendiz.educontrol.ui.teacher.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.repository.TeacherStudentSummaryModel
import com.aprendiz.educontrol.databinding.ItemTeacherStudentBinding
import java.util.Locale

class TeacherStudentAdapter(
    private val onStudentClick: (TeacherStudentSummaryModel) -> Unit
) : RecyclerView.Adapter<TeacherStudentAdapter.ViewHolder>() {

    private val students = mutableListOf<TeacherStudentSummaryModel>()

    fun submitList(newList: List<TeacherStudentSummaryModel>) {
        students.clear()
        students.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeacherStudentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    inner class ViewHolder(private val binding: ItemTeacherStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeacherStudentSummaryModel) {
            val context = binding.root.context
            binding.tvStudentName.text = item.nombre
            binding.tvStudentEmail.text = item.email
            binding.tvStudentAvatar.text = item.avatarEmoji

            binding.tvStudentAvgBadge.text = String.format(Locale.getDefault(), "%.2f", item.promedioClase)
            if (item.promedioClase >= 3.0) {
                binding.tvStudentAvgBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                binding.tvStudentAvgBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
            } else {
                binding.tvStudentAvgBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                binding.tvStudentAvgBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
            }

            binding.root.setOnClickListener {
                onStudentClick(item)
            }
        }
    }
}
