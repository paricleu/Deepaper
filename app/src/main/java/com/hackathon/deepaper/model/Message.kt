package com.hackathon.deepaper.model

data class Message(
    var msg: String = "",
    var sender: User,
    var createdAt: String = ""
)