package com.aprendiz.educontrol.ui.auth

data class DemoAccountModel(
    val id: Long,
    val name: String,
    val role: String,
    val description: String,
    val email: String,
    val avatarEmoji: String
)
