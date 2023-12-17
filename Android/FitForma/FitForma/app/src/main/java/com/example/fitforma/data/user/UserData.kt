package com.example.fitforma.data.user

data class UserData(
    var name : String ?= null,
    var email : String ?= null,
    var age : Int ?= null,
    var weight : Double ?= null,
    var height : Double ?= null
)