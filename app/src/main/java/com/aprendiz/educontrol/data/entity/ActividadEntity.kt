package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "actividades",
    foreignKeys = [
        ForeignKey(
            entity = ClaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["claseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["claseId"])]
)
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val claseId: Long,
    val titulo: String,
    val descripcion: String,
    val tipo: String = TYPE_TASK,
    val porcentaje: Double,
    val fechaEntrega: String,
    val mesTimeline: String = "SEPTIEMBRE"
) {
    companion object {
        const val TYPE_TASK = "TASK"
        const val TYPE_QUIZ = "QUIZ"
        const val TYPE_EXAM = "EXAM"
    }
}
