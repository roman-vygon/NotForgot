package com.rvygon.notforgot.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    val name: String,
    val email: String,
    val password: String,
    val api_token: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)