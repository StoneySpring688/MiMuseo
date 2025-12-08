package com.example.mimuseo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.core.net.toUri
import com.example.mimuseo.components.WristAttachedSystem
import com.example.mimuseo.WristAttached
import com.meta.spatial.castinputforward.CastInputForwardFeature
import com.meta.spatial.compose.ComposeFeature
import com.meta.spatial.compose.ComposeViewPanelRegistration
import com.meta.spatial.core.BuildConfig
import com.meta.spatial.core.SpatialFeature
import com.meta.spatial.core.SpatialSDKExperimentalAPI
import com.meta.spatial.core.Vector3
import com.meta.spatial.datamodelinspector.DataModelInspectorFeature
import com.meta.spatial.debugtools.HotReloadFeature
//import com.meta.spatial.isdk.IsdkFeature
import com.meta.spatial.okhttp3.OkHttpAssetFetcher
import com.meta.spatial.ovrmetrics.OVRMetricsDataModel
import com.meta.spatial.ovrmetrics.OVRMetricsFeature
import com.meta.spatial.runtime.NetworkedAssetLoader
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.DpPerMeterDisplayOptions
import com.meta.spatial.toolkit.LayoutXMLPanelRegistration
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.PanelStyleOptions
import com.meta.spatial.toolkit.QuadShapeOptions
import com.meta.spatial.toolkit.UIPanelSettings
import com.meta.spatial.vr.LocomotionSystem
import com.meta.spatial.vr.VRFeature

import android.net.Uri
import com.meta.spatial.toolkit.Mesh

import com.example.mimuseo.ui.MAIN_PANEL_HEIGHT
import com.example.mimuseo.ui.MAIN_PANEL_WIDTH
import com.example.mimuseo.ui.MainPanel
import com.example.mimuseo.ui.WristMenuPanel
import com.meta.spatial.core.Entity
import com.meta.spatial.core.Pose
import com.meta.spatial.core.Quaternion
import com.meta.spatial.core.Query
import com.meta.spatial.toolkit.Followable
import com.meta.spatial.toolkit.FollowableSystem
import com.meta.spatial.toolkit.Panel
import com.meta.spatial.toolkit.Scale
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.TransformParent
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.io.normalize
import kotlin.times

class ImmersiveActivity : AppSystemActivity() {
  private val activityScope = CoroutineScope(Dispatchers.Main)

    private var mainPanelEntity: Entity? = null
    private val WRIST_MENU_PANEL_ID = R.id.wrist_menu_panel
    val WRIST_BUTTON_SIZE = 0.08f

    private var current3DModelEntity: Entity? = null

  lateinit var textView: TextView
  lateinit var webView: WebView

  override fun registerFeatures(): List<SpatialFeature> {
    val features =
        mutableListOf<SpatialFeature>(
            VRFeature(this),
            ComposeFeature(),
            //IsdkFeature(this, spatial, systemManager),
        )
    if (BuildConfig.DEBUG) {
      features.add(CastInputForwardFeature(this))
      features.add(HotReloadFeature(this))
      features.add(OVRMetricsFeature(this, OVRMetricsDataModel() { numberOfMeshes() }))
    }
      features.add(DataModelInspectorFeature(spatial, this.componentManager))
      return features
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    NetworkedAssetLoader.init(
        File(applicationContext.getCacheDir().canonicalPath),
        OkHttpAssetFetcher(),
    )

    // Enable MR mode
    systemManager.findSystem<LocomotionSystem>().enableLocomotion(false)
    scene.enablePassthrough(true)

      componentManager.registerComponent<WristAttached>(WristAttached)
      systemManager.registerSystem(WristAttachedSystem())

    loadGLXF()
  }

  override fun onSceneReady() {
    super.onSceneReady()

    scene.setLightingEnvironment(
        ambientColor = Vector3(0f),
        sunColor = Vector3(7.0f, 7.0f, 7.0f),
        sunDirection = -Vector3(1.0f, 3.0f, -2.0f),
        environmentIntensity = 0.3f,
    )
    scene.updateIBLEnvironment("environment.env")

    scene.setViewOrigin(0.0f, 0.0f, 2.0f, 180.0f)

  }

    private fun spawn3DModel(modelPath: String) {
        current3DModelEntity?.destroy()

        val mainPanelEntity: Entity = Query.where { has(Panel.id) }
            .eval()
            .firstOrNull { entity ->
                val panelComponent = entity.getComponent<Panel>()
                panelComponent.panelRegistrationId == R.id.main_panel
            } as Entity

        current3DModelEntity = Entity.create(
            listOf(
                Mesh(modelPath.toUri()),
                TransformParent(mainPanelEntity),
                Transform(Pose(Vector3(2f, -0.5f, 0f))),
                Scale(Vector3(3.5f, 1f, 1f))
            )
        )
    }

  @OptIn(SpatialSDKExperimentalAPI::class)
  override fun registerPanels(): List<PanelRegistration> {
    return listOf(
        // Registering Main Museum Panel
        ComposeViewPanelRegistration(
            R.id.main_panel,
            composeViewCreator = { _, context ->
              ComposeView(context).apply { setContent { MainPanel() {path -> runOnUiThread { spawn3DModel(path) }} }}
            },
            settingsCreator = {
              UIPanelSettings(
                  shape =
                      QuadShapeOptions(width = MAIN_PANEL_WIDTH, height = MAIN_PANEL_HEIGHT),
                  style = PanelStyleOptions(themeResourceId = R.style.PanelAppThemeTransparent),
                  display = DpPerMeterDisplayOptions(),
              )
            }
        ),
        ComposeViewPanelRegistration(
            WRIST_MENU_PANEL_ID,
            composeViewCreator = { _, context ->
                ComposeView(context).apply {
                    setContent {
                        // Pasamos la acción al botón
                        WristMenuPanel() {
                            // Importante: Ejecutar en el hilo principal si tocamos UI o corrutinas de escena
                            runOnUiThread { showOrMoveMainPanel() }
                        }
                    }
                }
            },
            settingsCreator = {
                UIPanelSettings(
                    shape = QuadShapeOptions(width = WRIST_BUTTON_SIZE, height = WRIST_BUTTON_SIZE),
                    // Usamos transparente para que se vea nuestro fondo custom (Box con alpha)
                    style = PanelStyleOptions(themeResourceId = R.style.PanelAppThemeTransparent),
                    display = DpPerMeterDisplayOptions(),
                )
            }
        )
    )
  }

    private fun showOrMoveMainPanel() {
        val locomotionSystem = systemManager.findSystem<LocomotionSystem>()
        val headPose = locomotionSystem.getScene().getViewerPose()

        val forwardDirection = headPose.q * Vector3(0f, 0f, 0.6f)

        val targetPosition = (headPose.t * Vector3(1f,1f,1f)) + forwardDirection

        val panelRotation = headPose.q * Quaternion(1f, 1f, 0f)

        val mainPanelEntity = Query.where { has(Panel.id) }
            .eval()
            .firstOrNull { entity ->
                val panelComponent = entity.getComponent<Panel>()
                panelComponent.panelRegistrationId == R.id.main_panel
            }

        if (mainPanelEntity != null) {
            // Actualizar posición y rotación
            mainPanelEntity.setComponent(Transform(Pose(targetPosition, panelRotation)))

            // Asegurar que sea visible
            mainPanelEntity.setComponent(com.meta.spatial.toolkit.Visible(true))
        } else {
            println("Error: No se encontró la entidad con R.id.main_panel en la escena.")
        }

    }

    override fun onSpatialShutdown() {
    super.onSpatialShutdown()
  }

  private fun loadGLXF(): Job {
    return activityScope.launch {
      glXFManager.inflateGLXF(
          "apk:///scenes/Composition.glxf".toUri(),
          keyName = "example_key_name",
      )
    }
  }
}


