package com.example.newsapp.utils

fun String.formatTime(): String {
    return this.take(10)
}