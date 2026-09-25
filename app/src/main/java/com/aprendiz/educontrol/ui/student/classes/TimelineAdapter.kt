package com.aprendiz.educontrol.ui.student.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.entity.ActividadEntity
import com.aprendiz.educontrol.data.entity.EntregaEntity
import com.aprendiz.educontrol.data.repository.TimelineListItem
import com.aprendiz.educontrol.databinding.ItemTimelineActivityBinding
import com.aprendiz.educontrol.databinding.ItemTimelineHeaderMonthBinding
import java.util.Locale

class TimelineAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<TimelineListItem>()

    fun submitList(newList: List<TimelineListItem>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TimelineListItem.MonthHeader -> VIEW_TYPE_HEADER
            is TimelineListItem.ActivityItem -> VIEW_TYPE_ACTIVITY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding = ItemTimelineHeaderMonthBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemTimelineActivityBinding.inflate(inflater, parent, false)
            ActivityViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TimelineListItem.MonthHeader -> (holder as HeaderViewHolder).bind(item)
            is TimelineListItem.ActivityItem -> (holder as ActivityViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class HeaderViewHolder(private val binding: ItemTimelineHeaderMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimelineListItem.MonthHeader) {
            binding.tvMonthTitle.text = item.monthName
        }
    }

    inner class ActivityViewHolder(private val binding: ItemTimelineActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TimelineListItem.ActivityItem) {
            val context = binding.root.context
            binding.tvTimelineTitle.text = item.titulo
            binding.tvTimelineDesc.text = item.descripcion
            binding.tvTimelineDate.text = "📅 ${item.fechaEntrega}"
            binding.tvTimelineWeight.text = "Peso: ${item.porcentaje.toInt()}%"

            binding.tvTimelineNodeIcon.text = when (item.tipo) {
                ActividadEntity.TYPE_QUIZ -> "🧪"
                ActividadEntity.TYPE_EXAM -> "📚"
                else -> "📝"
            }

            if (item.nota != null) {
                binding.tvTimelineGrade.text = String.format(Locale.getDefault(), "%.2f", item.nota)
                if (item.nota >= 3.0) {
                    binding.tvTimelineGrade.setBackgroundResource(R.drawable.bg_chip_approved)
                    binding.tvTimelineGrade.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
                } else {
                    binding.tvTimelineGrade.setBackgroundResource(R.drawable.bg_chip_risk)
                    binding.tvTimelineGrade.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
                }
            } else {
                binding.tvTimelineGrade.text = if (item.estado == EntregaEntity.STATUS_SUBMITTED) "Entregado" else "Pendiente"
                binding.tvTimelineGrade.setBackgroundResource(R.drawable.bg_chip_pending)
                binding.tvTimelineGrade.setTextColor(ContextCompat.getColor(context, R.color.badge_pending_text))
            }

            if (!item.retroalimentacion.isNullOrEmpty()) {
                binding.tvTimelineFeedback.visibility = View.VISIBLE
                binding.tvTimelineFeedback.text = "Retroalimentación: ${item.retroalimentacion}"
            } else {
                binding.tvTimelineFeedback.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ACTIVITY = 1
    }
}
