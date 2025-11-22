package com.example.mimuseo.dominio

enum class Categorias(val nombre: String) {
    ARTE("Arte"),
    HISTORIA("Historia"),
    CIENCIA("Ciencia");

    fun key(): String {
        return nombre
    }

}