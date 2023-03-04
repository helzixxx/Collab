package com.example.collab.models

class Message constructor(
    var message: String,
    var imageUrl: String,
    var currentUser: Boolean,
    var createdAt: String
) {
}