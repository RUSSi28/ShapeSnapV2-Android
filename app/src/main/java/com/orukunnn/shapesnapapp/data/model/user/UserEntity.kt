package com.orukunnn.shapesnapapp.data.model.user

data class UserEntity(
    val uid: String = "",
    val posts: List<String> = emptyList(),
    val storage: List<String> = emptyList(),
    val isSubscribed: Boolean = false,
)
