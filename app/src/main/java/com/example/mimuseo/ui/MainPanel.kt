package com.example.mimuseo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mimuseo.R
import com.example.mimuseo.dominio.Activo
import com.example.mimuseo.repositorios.RepositorioActivos

/**
 * Enum para definir el tamaño del panel
 */
enum class PanelSize {
    SMALL,   // < 550dp ancho (mínimo 350dp - muestra 2 cards)
    MEDIUM,  // 550-800dp ancho (muestra 3-4 cards)
    LARGE    // > 800dp ancho (hasta 900dp máx - muestra 4+ cards)
}

/**
 * Data class para configuración responsive
 */
data class ResponsiveConfig(
    val panelSize: PanelSize,
    val cardWidth: Dp,
    val cardHeight: Dp,
    val showCardTitle: Boolean,
    val headerFontSize: Int,
    val categoryFontSize: Int,
    val padding: Dp,
    val spacing: Dp
)

/**
 * Calcula la configuración responsive basada en el ancho disponible
 */
fun calculateResponsiveConfig(width: Dp): ResponsiveConfig {
    return when {
        width < 550.dp -> ResponsiveConfig(
            panelSize = PanelSize.SMALL,
            cardWidth = 140.dp,
            cardHeight = 140.dp,
            showCardTitle = false,
            headerFontSize = 20,
            categoryFontSize = 16,
            padding = 12.dp,
            spacing = 12.dp
        )
        width < 800.dp -> ResponsiveConfig(
            panelSize = PanelSize.MEDIUM,
            cardWidth = 180.dp,
            cardHeight = 200.dp,
            showCardTitle = true,
            headerFontSize = 26,
            categoryFontSize = 20,
            padding = 18.dp,
            spacing = 14.dp
        )
        else -> ResponsiveConfig(
            panelSize = PanelSize.LARGE,
            cardWidth = 220.dp,
            cardHeight = 240.dp,
            showCardTitle = true,
            headerFontSize = 32,
            categoryFontSize = 24,
            padding = 24.dp,
            spacing = 16.dp
        )
    }
}

/**
 * Panel principal que muestra los activos del museo organizados por categorías.
 *
 * @param modifier Modificador para personalizar el layout
 * @param availableWidth Ancho disponible para el panel
 * @param availableHeight Alto disponible para el panel
 */
@Composable
fun MainPanel(
    modifier: Modifier = Modifier,
    availableWidth: Dp = 800.dp,
    availableHeight: Dp = 1000.dp
) {
    // Estado para controlar qué activo está seleccionado
    var activoSeleccionado by remember { mutableStateOf<Activo?>(null) }

    // Obtenemos los activos agrupados por categoría
    val activosPorCategoria = remember { RepositorioActivos.obtenerActivosAgrupadosPorCategoria() }

    // Configuración responsive
    val config = remember(availableWidth) { calculateResponsiveConfig(availableWidth) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (activoSeleccionado == null) {
            // Vista principal: muestra categorías y activos
            VistaPrincipal(
                activosPorCategoria = activosPorCategoria,
                onActivoClick = { activo -> activoSeleccionado = activo },
                config = config
            )
        } else {
            // Vista de detalle: muestra el activo seleccionado
            VistaDetalleActivo(
                activo = activoSeleccionado!!,
                onVolverClick = { activoSeleccionado = null },
                config = config
            )
        }
    }
}

/**
 * Vista principal que muestra el header y las categorías con sus activos
 */
@Composable
private fun VistaPrincipal(
    activosPorCategoria: Map<String, List<Activo>>,
    onActivoClick: (Activo) -> Unit,
    config: ResponsiveConfig
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(config.padding)
    ) {
        // Header
        HeaderPanel(config)

        Spacer(modifier = Modifier.height(config.spacing))

        // Contenido con scroll vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(config.spacing)
        ) {
            // Iteramos sobre cada categoría
            activosPorCategoria.forEach { (categoria, activos) ->
                CategoriaSeccion(
                    nombreCategoria = categoria,
                    activos = activos,
                    onActivoClick = onActivoClick,
                    config = config
                )
            }
        }
    }
}

/**
 * Header del panel con el título "Mi Museo"
 */
@Composable
private fun HeaderPanel(config: ResponsiveConfig) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF2C3E50),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = config.padding, horizontal = config.padding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mi Museo",
            fontSize = config.headerFontSize.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Sección de una categoría con su lista horizontal de activos
 */
@Composable
private fun CategoriaSeccion(
    nombreCategoria: String,
    activos: List<Activo>,
    onActivoClick: (Activo) -> Unit,
    config: ResponsiveConfig
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(config.spacing)
    ) {
        // Título de la categoría
        Text(
            text = nombreCategoria,
            fontSize = config.categoryFontSize.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF34495E),
            modifier = Modifier.padding(start = 8.dp)
        )

        // Lista horizontal de activos
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(activos) { activo ->
                TarjetaActivo(
                    activo = activo,
                    onClick = { onActivoClick(activo) },
                    config = config
                )
            }
        }
    }
}

/**
 * Tarjeta individual de un activo en la vista de categoría
 */
@Composable
private fun TarjetaActivo(
    activo: Activo,
    onClick: () -> Unit,
    config: ResponsiveConfig
) {
    Card(
        modifier = Modifier
            .width(config.cardWidth)
            .height(config.cardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = if (config.showCardTitle) Arrangement.SpaceBetween else Arrangement.Center
        ) {
            // Imagen del activo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(if (config.showCardTitle) 1f else 1f)
                    .background(
                        color = Color(0xFFECF0F1),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mimuseoplaceholder),
                    contentDescription = "Imagen de ${activo.getNombre()}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Mostrar título solo si la configuración lo permite
            if (config.showCardTitle) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activo.getNombre(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C3E50),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Vista de detalle de un activo seleccionado
 */
@Composable
private fun VistaDetalleActivo(
    activo: Activo,
    onVolverClick: () -> Unit,
    config: ResponsiveConfig
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(config.padding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(config.spacing)
    ) {
        // Botón para volver
        Button(
            onClick = onVolverClick,
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("← Volver")
        }

        // Título del activo (ajustar tamaño según config)
        Text(
            text = activo.getNombre(),
            fontSize = if (config.panelSize == PanelSize.SMALL) 24.sp else 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Imagen grande del activo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (config.panelSize == PanelSize.SMALL) 200.dp else 300.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFECF0F1)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mimuseoplaceholder),
                    contentDescription = "Imagen grande de ${activo.getNombre()}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Categoría
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3498DB).copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(config.padding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categoría:",
                    fontSize = if (config.panelSize == PanelSize.SMALL) 14.sp else 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = activo.getCategoria(),
                    fontSize = if (config.panelSize == PanelSize.SMALL) 14.sp else 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF3498DB)
                )
            }
        }

        // Descripción
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(config.padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Descripción:",
                    fontSize = if (config.panelSize == PanelSize.SMALL) 16.sp else 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = activo.getDescripcion(),
                    fontSize = if (config.panelSize == PanelSize.SMALL) 14.sp else 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF7F8C8D),
                    lineHeight = if (config.panelSize == PanelSize.SMALL) 20.sp else 24.sp
                )
            }
        }

        // Botón para ver modelo 3D
        Button(
            onClick = { /* TODO: Implementar visualización de modelo 3D */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Ver Modelo 3D")
        }
    }
}

/**
 * Función auxiliar para refrescar los datos del panel
 * Puede ser llamada cuando se añaden nuevos activos al repositorio
 */
fun actualizarPanel() {
    // Esta función puede ser expandida en el futuro para forzar
    // una recomposición cuando los datos cambien externamente
    RepositorioActivos.obtenerActivosAgrupadosPorCategoria()
}

