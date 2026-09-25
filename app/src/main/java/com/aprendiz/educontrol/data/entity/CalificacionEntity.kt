package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "calificaciones",
    foreignKeys = [
        ForeignKey(
            entity = EntregaEntity::class,
            parentColumns = ["id"],
            childColumns = ["entregaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["entregaId"])]
)
data class CalificacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entregaId: Long,
    val nota: Double,
    val retroalimentacion: String,
    val fechaCalificacion: String
)
