package com.example.hrms.models

import java.io.Serializable

data class SalaryModel(
    var finalSalary : String,
    var originalSalary : String,
    var totalDeduction : String
) : Serializable