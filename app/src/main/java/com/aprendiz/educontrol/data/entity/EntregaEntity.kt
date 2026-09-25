package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "entregas",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["actividadId"]), Index(value = ["studentId"])]
)
data class EntregaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actividadId: Long,
    val studentId: Long,
    val contenidoRespuesta: String? = null,
    val fechaEntrega: String,
    val estado: String = STATUS_PENDING
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_SUBMITTED = "SUBMITTED"
        const val STATUS_LATE = "LATE"
        const val STATUS_GRADED = "GRADED"
    }
}
