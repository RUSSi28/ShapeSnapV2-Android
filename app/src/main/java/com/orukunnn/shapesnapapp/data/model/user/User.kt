package com.orukunnn.shapesnapapp.data.model.user

data class User(
    val uid: String = "",
    val posts: List<String> = emptyList(),
    val storage: List<String> = emptyList(),
)