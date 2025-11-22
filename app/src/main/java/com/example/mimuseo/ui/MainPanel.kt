package com.example.mimuseo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mimuseo.R
import com.example.mimuseo.dominio.Activo
import com.example.mimuseo.repositorios.RepositorioActivos
import com.meta.spatial.toolkit.PanelConstants
import com.meta.spatial.uiset.button.PrimaryButton
import com.meta.spatial.uiset.theme.LocalColorScheme
import com.meta.spatial.uiset.theme.SpatialTheme

const val MAIN_PANEL_WIDTH = 1.2f
const val MAIN_PANEL_HEIGHT = 0.9f

@Composable
@Preview(
    widthDp = (PanelConstants.DEFAULT_DP_PER_METER * MAIN_PANEL_WIDTH).toInt(),
    heightDp = (PanelConstants.DEFAULT_DP_PER_METER * MAIN_PANEL_HEIGHT).toInt(),
)
fun MainPanelPreview() {
    MainPanel()
}

@Composable
fun MainPanel() {
    // Estado para el activo seleccionado
    var activoSeleccionado by remember { mutableStateOf<Activo?>(null) }

    // Cargar datos del repositorio
    val activosPorCategoria = remember { RepositorioActivos.obtenerActivosAgrupadosPorCategoria() }

    SpatialTheme(colorScheme = getPanelTheme()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(SpatialTheme.shapes.large)
                .background(brush = LocalColorScheme.current.panel)
        ) {
            if (activoSeleccionado == null) {
                // Vista principal: muestra categorías y activos
                MainPanelContent(
                    activosPorCategoria = activosPorCategoria,
                    onActivoClick = { activo -> activoSeleccionado = activo }
                )
            } else {
                // Vista de detalle: muestra el activo seleccionado
                ActivoDetalleView(
                    activo = activoSeleccionado!!,
                    onBackClick = { activoSeleccionado = null }
                )
            }
        }
    }
}

@Composable
private fun MainPanelContent(
    activosPorCategoria: Map<String, List<Activo>>,
    onActivoClick: (Activo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Text(
            text = "Mi Museo",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // Columna con categorías
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(activosPorCategoria.entries.toList()) { (categoria, activos) ->
                CategoriaSection(
                    categoria = categoria,
                    activos = activos,
                    onActivoClick = onActivoClick
                )
            }
        }
    }
}

@Composable
private fun CategoriaSection(
    categoria: String,
    activos: List<Activo>,
    onActivoClick: (Activo) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Título de la categoría
        Text(
            text = categoria,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Fila horizontal desplazable con los activos
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(activos) { activo ->
                ActivoCard(
                    activo = activo,
                    onClick = { onActivoClick(activo) }
                )
            }
        }
    }
}

@Composable
private fun ActivoCard(
    activo: Activo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del activo
            Image(
                painter = painterResource(id = R.drawable.mimuseoplaceholder),
                contentDescription = activo.getNombre(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del activo
            Text(
                text = activo.getNombre(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ActivoDetalleView(
    activo: Activo,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // Botón de retroceso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            PrimaryButton(
                label = "← Volver",
                onClick = onBackClick
            )
        }

        // Contenido del detalle
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Imagen grande a la izquierda
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mimuseoplaceholder),
                    contentDescription = activo.getNombre(),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Información a la derecha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Text(
                    text = activo.getNombre(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Categoría
                Text(
                    text = "Categoría: ${activo.getCategoria()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                // Descripción
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = activo.getDescripcion(),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        lineHeight = 24.sp
                    )
                }

                // Botón para ver modelo 3D (solo si está disponible)
                if (activo.tieneModelo3d()) {
                    PrimaryButton(
                        label = "Ver Modelo 3D",
                        expanded = true,
                        onClick = { /* TODO: Implementar visualización de modelo 3D */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun getPanelTheme() =
    if (androidx.compose.foundation.isSystemInDarkTheme())
        com.meta.spatial.uiset.theme.darkSpatialColorScheme()
    else
        com.meta.spatial.uiset.theme.lightSpatialColorScheme()

