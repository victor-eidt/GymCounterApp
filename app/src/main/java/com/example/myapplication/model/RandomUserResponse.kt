package com.example.myapplication.model

data class RandomUserResponse(
    val results: List<User>
)

data class User(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    val dob: Dob,
    val phone: String,
    val picture: Picture
)

data class Name(val title: String, val first: String, val last: String)

data class Location(val city: String, val state: String, val country: String)

data class Login(val uuid: String)

data class Dob(val date: String)

data class Picture(val large: String)
