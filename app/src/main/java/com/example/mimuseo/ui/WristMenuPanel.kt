package com.example.mimuseo.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mimuseo.R

@Composable
fun WristMenuPanel(
    onOpenClick: () -> Unit
) {
    Button(
        onClick = onOpenClick,

        // 1. Tamaño fijo (ajusta el 72.dp si lo quieres más grande o pequeño)
        modifier = Modifier.size(72.dp),

        // 2. Forma completamente circular
        shape = CircleShape,

        // 3. Fondo translúcido y borde fino
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray.copy(alpha = 0.5f),
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),

        // 4. Espacio interno para que el icono respire dentro del círculo
        contentPadding = PaddingValues(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.museo_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(), // Llena el hueco que deja el padding
            contentScale = ContentScale.Fit
        )
    }
}

@Preview(
    name = "Menu Muñeca",
    showBackground = true,
    backgroundColor = 0xFF444444 // Gris oscuro para ver la transparencia y bordes blancos
)
@Composable
fun WristMenuPanelPreview() {
    // Simulamos el tamaño del panel de la muñeca (aprox. proporción 4:1)
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(75.dp)
    ) {
        WristMenuPanel(
            onOpenClick = {}
        )
    }
}

