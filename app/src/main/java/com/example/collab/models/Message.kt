package com.example.collab.models

class Message constructor(
    var message: String,
    var imageUrl: String,
    var audioUrl: String,
    var audioName: String,
    var currentUser: Boolean,
    var createdAt: String
) {
}