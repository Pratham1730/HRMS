package com.example.hrms.Models

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val dob: String,
    val joiningDate: String,
    val gender: String,
    val position: String,
    val department: String
)

