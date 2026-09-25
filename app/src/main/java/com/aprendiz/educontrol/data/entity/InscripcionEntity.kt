package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "inscripciones",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ClaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["claseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["studentId"]), Index(value = ["claseId"])]
)
data class InscripcionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val claseId: Long
)
