package com.example.hrms.Models

import java.math.BigInteger
import java.sql.Date

data class UserModel(
    val insert : String,
    val name: String,
    val email: String,
    val password: String,
    val phone: BigInteger,
    val gender: Int,
    val department: Int,
    val position: Int,
    val salary : Int,
    val joiningDate: String,
    val dob: String
)

