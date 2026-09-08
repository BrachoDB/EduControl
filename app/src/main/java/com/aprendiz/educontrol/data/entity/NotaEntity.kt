package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notas",
    foreignKeys = [
        ForeignKey(
            entity = MateriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["materiaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["materiaId"])]
)
data class NotaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val materiaId: Long,
    val nombreEvaluacion: String,
    val calificacion: Double,
    val porcentaje: Double
)
