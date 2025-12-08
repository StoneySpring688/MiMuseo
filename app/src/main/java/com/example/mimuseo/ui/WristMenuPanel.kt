package com.example.mimuseo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.meta.spatial.uiset.button.PrimaryButton

// Definimos tamaños pequeños para la muñeca
const val WRIST_PANEL_WIDTH = 0.2f // 20 cm
const val WRIST_PANEL_HEIGHT = 0.15f // 15 cm

@Composable
fun WristMenuPanel(
    onOpenClick: () -> Unit
) {
    // Fondo translúcido oscuro
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp)) // Bordes redondeados
            .background(Color.Black.copy(alpha = 0.6f)), // Translúcido
        contentAlignment = Alignment.Center
    ) {
        // Un solo botón para abrir/traer el panel
        PrimaryButton(
            label = "Abrir Museo",
            onClick = onOpenClick,
            modifier = Modifier.padding(16.dp)
        )
    }
}