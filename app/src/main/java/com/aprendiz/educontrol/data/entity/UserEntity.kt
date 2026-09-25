package com.aprendiz.educontrol.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val email: String,
    val password: String = "123456",
    val rol: String = ROLE_STUDENT,
    val avatarEmoji: String = "👨‍🎓"
) {
    companion object {
        const val ROLE_STUDENT = "STUDENT"
        const val ROLE_TEACHER = "TEACHER"
    }
}
