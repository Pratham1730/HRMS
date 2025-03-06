package com.example.hrms.models

import java.math.BigInteger

data class UpdateDataModel(
    var method : String,
    var u_id : Int,
    var u_name : String,
    var u_phone : BigInteger,
    var u_dob : String
)