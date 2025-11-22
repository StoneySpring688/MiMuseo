# Mi Museo - Aplicación para Meta Quest

Aplicación de museo virtual para Meta Quest que funciona como una aplicación 2D estándar (panel). Permite obtener información sobre distintos objetos de la colección de un museo, así como visualizarlos en 3D si está disponible el modelo.

## 🎯 Características

- **Panel 2D estándar**: La aplicación funciona como cualquier app de Android en Meta Quest (similar a WhatsApp, navegador, etc.)
- **Diseño elegante y minimalista**: Interfaz moderna con Material Design 3
- **Organización por categorías**: Los activos se agrupan por Arte, Historia y Ciencia
- **Navegación intuitiva**: 
  - Vista principal con scroll vertical de categorías
  - Scroll horizontal por categoría para ver todos los activos
  - Vista de detalle al seleccionar un activo
- **Arquitectura modular**: Separación clara entre dominio, repositorios y UI

## 📱 Estructura de la Aplicación

### Actividades
- **MainActivity**: Actividad principal 2D que muestra el panel del museo
- **ImmersiveActivity**: Actividad opcional para experiencias VR inmersivas (disponible como modo alternativo)

### Componentes UI
- **MainPanel**: Panel principal que muestra categorías y activos
- **VistaPrincipal**: Vista con header y listado de categorías
- **VistaDetalleActivo**: Vista de detalle de un activo seleccionado

### Dominio
- **Activo**: Representa un objeto del museo (con nombre, descripción, categoría, imagen y modelo 3D opcional)
- **Categorias**: Enum de categorías (Arte, Historia, Ciencia)

### Repositorios
- **RepositorioActivos**: Gestiona la lista de activos agrupados por categoría

## 🚀 Cómo ejecutar

1. Abre el proyecto en Android Studio
2. Conecta tu Meta Quest mediante ADB o usa el emulador
3. Ejecuta la aplicación
4. La app se abrirá como un panel 2D estándar en Meta Quest

## 📝 Próximos pasos

- [ ] Agregar imágenes reales a los activos
- [ ] Implementar visualización de modelos 3D
- [ ] Agregar funcionalidad de búsqueda
- [ ] Implementar filtros por categoría
- [ ] Agregar animaciones de transición
- [ ] Permitir agregar/editar activos dinámicamente

## 🛠️ Tecnologías

- Kotlin
- Jetpack Compose
- Material Design 3
- Meta Spatial SDK (para funcionalidades VR opcionales)


