package com.jeckso.fillament_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.SurfaceView
import com.google.android.filament.*
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            Utils.init()
        }
    }

    private lateinit var engine: Engine
    private lateinit var camera: Camera
    private lateinit var surfaceView: SurfaceView
    private lateinit var view: View
    private lateinit var choreographer: Choreographer
    private lateinit var modelViewer: ModelViewer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = Engine.create()
        camera = engine.createCamera(engine.entityManager.create())
        view = engine.createView()
        view.camera = camera
//        camera.setProjection(
//            Camera.Projection.ORTHO,
//            0.0, 0.0, 0.0, 0.0, 0.0, 10.0
//        )
        surfaceView = SurfaceView(this).apply { setContentView(this) }
        choreographer = Choreographer.getInstance()
        modelViewer = ModelViewer(surfaceView)
      //  surfaceView.setOnTouchListener(modelViewer)
        loadGlb("gltf")


        //   modelViewer.scene.skybox = Skybox.Builder().environment(readAsset("envs/bg.filamesh")).build()
        modelViewer.scene.skybox = Skybox.Builder().build(modelViewer.engine)
        loadGlb("gltf")
      //  loadEnvironment("myOutDir1")
      //  Log.e("MODEL", " HERE ${modelViewer.asset?.resourceUris}")
    }

//    private fun loadEnvironment(ibl: String) {
//        // Create the indirect light source and add it to the scene.
//        var buffer = readAsset("envs/$ibl/${ibl}_ibl.ktx")
//        KtxLoader.createIndirectLight(modelViewer.engine, buffer).apply {
//            intensity = 50_000f
//            modelViewer.scene.indirectLight = this
//            modelViewer
//        }
//        // Create the sky box and add it to the scene.
//        buffer = readAsset("envs/$ibl/${ibl}_skybox.ktx")
//        KtxLoader.createSkybox(modelViewer.engine, buffer).apply {
//            modelViewer.scene.skybox = this
//        }
//    }

    private fun loadGlb(name: String) {
        val buffer = readAsset("models/${name}.glb")
        modelViewer.loadModelGlb(buffer)
        modelViewer.transformToUnitCube()
    }

    private fun readAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()
        override fun doFrame(currentTime: Long) {
            val seconds = (currentTime - startTime).toDouble() / 1_000_000_000
            choreographer.postFrameCallback(this)
            modelViewer.animator?.apply {
                if (animationCount > 0) {
                    applyAnimation(0, seconds.toFloat())
                }
                updateBoneMatrices()
            }
            modelViewer.render(currentTime)
        }
    }

    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameCallback)
    }

    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameCallback)
    }
}