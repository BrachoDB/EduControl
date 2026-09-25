package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "preguntas_quiz",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["actividadId"])]
)
data class PreguntaQuizEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actividadId: Long,
    val enunciado: String,
    val opcionA: String,
    val opcionB: String,
    val opcionC: String,
    val opcionD: String,
    val opcionCorrecta: Int // Index 0 (A), 1 (B), 2 (C), 3 (D)
)
