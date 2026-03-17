package com.orukunnn.shapesnapapp.data.model.user

data class User(
    val uid: String,
    val posts: List<String>,
    val storage: List<String>,
    val isSubscribed: Boolean,
) {
    constructor(remote: UserEntity) : this(
        posts = remote.posts,
        storage = remote.storage,
        uid = remote.uid,
        isSubscribed = remote.isSubscribed,
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        uid = uid,
        posts = posts,
        storage = storage,
        isSubscribed = isSubscribed,
    )
}
