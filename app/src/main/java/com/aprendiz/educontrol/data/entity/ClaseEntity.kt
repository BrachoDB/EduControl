package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clases",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["teacherId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["teacherId"])]
)
data class ClaseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val teacherId: Long,
    val nombreClase: String,
    val codigoClase: String,
    val colorTheme: String = THEME_TEAL
) {
    companion object {
        const val THEME_TEAL = "TEAL"
        const val THEME_BLUE = "BLUE"
        const val THEME_PURPLE = "PURPLE"
        const val THEME_ORANGE = "ORANGE"
    }
}
