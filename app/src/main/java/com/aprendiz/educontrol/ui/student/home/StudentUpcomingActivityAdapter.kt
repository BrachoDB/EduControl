package com.aprendiz.educontrol.ui.student.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.data.entity.ActividadEntity
import com.aprendiz.educontrol.data.repository.StudentUpcomingActivityModel
import com.aprendiz.educontrol.databinding.ItemStudentUpcomingActivityBinding

class StudentUpcomingActivityAdapter(
    private val onActivityClick: (StudentUpcomingActivityModel) -> Unit
) : RecyclerView.Adapter<StudentUpcomingActivityAdapter.ViewHolder>() {

    private val activities = mutableListOf<StudentUpcomingActivityModel>()

    fun submitList(newList: List<StudentUpcomingActivityModel>) {
        activities.clear()
        activities.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentUpcomingActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    inner class ViewHolder(private val binding: ItemStudentUpcomingActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentUpcomingActivityModel) {
            binding.tvActivityTitle.text = item.titulo
            binding.tvActivityClassAndDate.text = "${item.nombreClase} • ${item.fechaEntrega}"
            binding.tvActivityWeight.text = "${item.porcentaje.toInt()}%"

            binding.tvActivityIcon.text = when (item.tipo) {
                ActividadEntity.TYPE_QUIZ -> "🧪"
                ActividadEntity.TYPE_EXAM -> "📚"
                else -> "📝"
            }

            binding.root.setOnClickListener {
                onActivityClick(item)
            }
        }
    }
}
