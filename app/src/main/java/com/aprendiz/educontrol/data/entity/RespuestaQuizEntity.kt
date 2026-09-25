package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "respuestas_quiz",
    foreignKeys = [
        ForeignKey(
            entity = EntregaEntity::class,
            parentColumns = ["id"],
            childColumns = ["entregaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PreguntaQuizEntity::class,
            parentColumns = ["id"],
            childColumns = ["preguntaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["entregaId"]), Index(value = ["preguntaId"])]
)
data class RespuestaQuizEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val entregaId: Long,
    val preguntaId: Long,
    val respuestaSeleccionada: Int,
    val esCorrecta: Boolean
)
