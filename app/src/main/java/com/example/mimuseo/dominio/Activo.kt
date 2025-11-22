package com.example.mimuseo.dominio

import androidx.annotation.DrawableRes

/**
 * <p>
 *     Representa un activo en el museo, que puede ser una pieza física o una información digital.
 *     Cada activo tiene un ID único, un nombre, una descripción, una imagen representativa y
 *     opcionalmente una ruta a un modelo 3D si está disponible.
 *     </p>
 *     @param id Un ID único
 *     @param nombre Nombre del activo
 *     @param descripcion Descripción detallada del activo
 *     @param categoria Categoría a la que pertenece el activo
 *     @param imagenResId Recurso de imagen representativa (opcional)
 *     @param modelo3dPath Ruta al modelo 3D del activo (opcional
 **/
data class Activo(
    private val id: Int, // Un ID único
    private val nombre: String,
    private val descripcion: String,
    private val categoria: Categorias,
    @DrawableRes private val imagenResId: Int? = null, //  Referencia R.drawable.xxx
    private val modelo3dPath: String? = null   // Nullable: puede ser null si solo es info 2D
){
    fun getId(): Int = id
    fun getNombre(): String = nombre
    fun getDescripcion(): String = descripcion
    fun getCategoria(): String = categoria.key()
    fun getImagenResId(): Int? = imagenResId
    fun getModelo3dPath(): String? = modelo3dPath
    fun tieneModelo3d(): Boolean = modelo3dPath != null
}