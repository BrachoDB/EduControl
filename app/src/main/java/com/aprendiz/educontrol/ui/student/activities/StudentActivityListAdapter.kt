package com.aprendiz.educontrol.ui.student.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.entity.ActividadEntity
import com.aprendiz.educontrol.data.entity.EntregaEntity
import com.aprendiz.educontrol.data.repository.StudentActivityItemModel
import com.aprendiz.educontrol.databinding.ItemStudentActivityBinding
import java.util.Locale

class StudentActivityListAdapter(
    private val onActivityClick: (StudentActivityItemModel) -> Unit
) : RecyclerView.Adapter<StudentActivityListAdapter.ViewHolder>() {

    private val activities = mutableListOf<StudentActivityItemModel>()

    fun submitList(newList: List<StudentActivityItemModel>) {
        activities.clear()
        activities.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentActivityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size

    inner class ViewHolder(private val binding: ItemStudentActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentActivityItemModel) {
            val context = binding.root.context
            binding.tvActivityTitle.text = item.titulo
            binding.tvClassAndDate.text = "${item.nombreClase} • ${item.fechaEntrega}"

            binding.tvActivityIcon.text = when (item.tipo) {
                ActividadEntity.TYPE_QUIZ -> "🧪"
                ActividadEntity.TYPE_EXAM -> "📚"
                else -> "📝"
            }

            if (item.nota != null) {
                binding.tvStatusOrGradeBadge.text = String.format(Locale.getDefault(), "%.2f", item.nota)
                if (item.nota >= 3.0) {
                    binding.tvStatusOrGradeBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                    binding.tvStatusOrGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
                } else {
                    binding.tvStatusOrGradeBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                    binding.tvStatusOrGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
                }
            } else {
                binding.tvStatusOrGradeBadge.text = if (item.estado == EntregaEntity.STATUS_SUBMITTED) "Entregado" else "Pendiente"
                binding.tvStatusOrGradeBadge.setBackgroundResource(R.drawable.bg_chip_pending)
                binding.tvStatusOrGradeBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_pending_text))
            }

            binding.root.setOnClickListener {
                onActivityClick(item)
            }
        }
    }
}
