package com.rvygon.notforgot.Model

data class RegisterResponse (
    val name: String,
    val email: String,
    val id: Int,
    val api_token: String
)