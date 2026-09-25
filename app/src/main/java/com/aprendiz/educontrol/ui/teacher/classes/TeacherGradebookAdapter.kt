package com.aprendiz.educontrol.ui.teacher.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.data.repository.TeacherActivityGradeModel
import com.aprendiz.educontrol.databinding.ItemTeacherGradebookBinding
import java.util.Locale

class TeacherGradebookAdapter(
    private val onGradeClick: (TeacherActivityGradeModel) -> Unit
) : RecyclerView.Adapter<TeacherGradebookAdapter.ViewHolder>() {

    private val items = mutableListOf<TeacherActivityGradeModel>()

    fun submitList(newList: List<TeacherActivityGradeModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeacherGradebookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemTeacherGradebookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeacherActivityGradeModel) {
            binding.tvStudentName.text = item.studentName
            binding.tvStudentAvatar.text = item.avatarEmoji
            binding.tvFeedbackSnippet.text = item.retroalimentacion ?: "Sin retroalimentación"

            binding.btnGrade.text = if (item.nota != null) {
                String.format(Locale.getDefault(), "%.2f ✎", item.nota)
            } else {
                "Calificar ✎"
            }

            binding.btnGrade.setOnClickListener {
                onGradeClick(item)
            }
        }
    }
}
