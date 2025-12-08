package com.example.mimuseo.repositorios

import com.example.mimuseo.dominio.Activo
import com.example.mimuseo.dominio.Categorias
import java.util.ArrayList
import java.util.HashMap

// clase singleton
object RepositorioActivos {
    var listaActivos: MutableMap<String, MutableList<Activo>> = HashMap<String, MutableList<Activo>>()

    // se ejececuta la primera vez que se llama a la clase
    init {
        cargarDatosIniciales()
    }

    private fun cargarDatosIniciales() {
        add(Activo(1, "Mona Lisa", "Famosa pintura de Leonardo da Vinci", Categorias.ARTE))
        add(Activo(2, "El Pensador", "Escultura de Auguste Rodin", Categorias.ARTE))
        add(Activo(3, "Tiranosaurio Rex", "Esqueleto de dinosaurio en el museo de historia natural", Categorias.CIENCIA, modelo3dPath = "apk:///models/TyrannosaurusRex.glb"))
    }

    fun add(activo: Activo) {
        val categoriaKey = activo.getCategoria()
        if (!listaActivos.containsKey(categoriaKey)) {
            listaActivos[categoriaKey] = ArrayList<Activo>()
        }
        listaActivos[categoriaKey]?.add(activo)
    }

    fun obtenerActivosAgrupadosPorCategoria(): Map<String, List<Activo>> {
        return listaActivos
    }


}