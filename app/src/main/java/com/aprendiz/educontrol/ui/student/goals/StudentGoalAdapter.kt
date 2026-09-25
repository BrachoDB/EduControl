package com.aprendiz.educontrol.ui.student.goals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.aprendiz.educontrol.R
import com.aprendiz.educontrol.data.repository.StudentClassSummaryModel
import com.aprendiz.educontrol.databinding.ItemStudentGoalProjectionBinding
import com.aprendiz.educontrol.domain.calculator.GoalStatus
import com.aprendiz.educontrol.domain.calculator.ProjectionCalculator
import java.util.Locale

class StudentGoalAdapter : RecyclerView.Adapter<StudentGoalAdapter.ViewHolder>() {

    private val items = mutableListOf<StudentClassSummaryModel>()

    fun submitList(newList: List<StudentClassSummaryModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStudentGoalProjectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemStudentGoalProjectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StudentClassSummaryModel) {
            val context = binding.root.context
            binding.tvClassName.text = item.nombreClase
            binding.tvTeacherName.text = item.profesor

            val porcentajeRestante = (100.0 - item.porcentajeEvaluado).coerceAtLeast(0.0)

            binding.tvCurrentAvg.text = String.format(Locale.getDefault(), "%.2f", item.promedio)
            binding.tvEvaluatedPercent.text = "${item.porcentajeEvaluado.toInt()}%"
            binding.tvRemainingPercent.text = "${porcentajeRestante.toInt()}%"

            fun updateProjection() {
                val input = binding.etTargetGrade.text?.toString()?.trim()
                val targetGrade = input?.toDoubleOrNull() ?: 4.0

                val res = ProjectionCalculator.calculateProjection(
                    promedioAcumulado = item.promedio,
                    porcentajeEvaluado = item.porcentajeEvaluado,
                    notaObjetivo = targetGrade
                )

                binding.tvExplanationMessage.text = res.mensajeExplicativo
                binding.tvTrendGrade.text = String.format(Locale.getDefault(), "%.2f", res.notaFinalEstimadaTendencia)
                binding.tvMaxGrade.text = String.format(Locale.getDefault(), "%.2f", res.notaFinalMaximaPosible)

                when (res.estado) {
                    GoalStatus.ALCANZADA -> {
                        binding.tvStatusBadge.text = "🎯 Alcanzada"
                        binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                        binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
                        binding.layoutScenarios.setBackgroundResource(R.drawable.bg_chip_approved)
                    }
                    GoalStatus.ALCANZABLE -> {
                        binding.tvStatusBadge.text = "🟢 Alcanzable"
                        binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_chip_approved)
                        binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_approved_text))
                        binding.layoutScenarios.setBackgroundResource(R.drawable.bg_chip_approved)
                    }
                    GoalStatus.EXIGENTE -> {
                        binding.tvStatusBadge.text = "🟡 Exigente"
                        binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_chip_pending)
                        binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_pending_text))
                        binding.layoutScenarios.setBackgroundResource(R.drawable.bg_chip_pending)
                    }
                    GoalStatus.NO_ALCANZABLE -> {
                        binding.tvStatusBadge.text = "🔴 Imposible"
                        binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                        binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
                        binding.layoutScenarios.setBackgroundResource(R.drawable.bg_chip_risk)
                    }
                    GoalStatus.CURSO_CERRADO -> {
                        binding.tvStatusBadge.text = "🔒 Cerrado"
                        binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_chip_risk)
                        binding.tvStatusBadge.setTextColor(ContextCompat.getColor(context, R.color.badge_risk_text))
                        binding.layoutScenarios.setBackgroundResource(R.drawable.bg_chip_risk)
                        binding.tilTargetGrade.isEnabled = false
                    }
                }
            }

            updateProjection()

            binding.etTargetGrade.doOnTextChanged { _, _, _, _ ->
                updateProjection()
            }
        }
    }
}
