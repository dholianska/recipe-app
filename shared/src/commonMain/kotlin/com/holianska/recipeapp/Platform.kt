package com.holianska.recipeapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform