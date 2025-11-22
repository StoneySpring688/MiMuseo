package com.example.mimuseo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mimuseo.ui.MainPanel
import com.example.mimuseo.ui.theme.MiMuseoTheme

/**
 * Activity principal para la aplicación 2D estándar de Mi Museo
 * Esta es una aplicación Android normal que se ejecuta como panel 2D en Meta Quest
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiMuseoTheme {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Establecer tamaños min y max
                    // Min: 350dp ancho x 450dp alto (2 cards SMALL + 2 categorías visibles)
                    // Max: 900dp ancho x 1200dp alto (evita espacio vacío excesivo)
                    val constrainedWidth = maxWidth.coerceIn(350.dp, 900.dp)
                    val constrainedHeight = maxHeight.coerceIn(450.dp, 1200.dp)

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainPanel(
                            availableWidth = constrainedWidth,
                            availableHeight = constrainedHeight
                        )
                    }
                }
            }
        }
    }
}

