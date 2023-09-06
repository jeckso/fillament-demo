package com.jeckso.fillament_demo

import android.animation.ValueAnimator
import android.graphics.Color
import android.opengl.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.SurfaceView
import android.view.animation.LinearInterpolator
import android.widget.Button
import com.google.android.filament.*
import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.Manipulator
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import com.google.android.filament.utils.rotation
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
    private val animator = ValueAnimator.ofFloat(0.0f, 360.0f)
    @Entity private var renderable = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = Engine.create()
        renderable = EntityManager.get().create()
        camera = engine.createCamera(renderable)
        view = engine.createView()
        view.camera = camera
//        camera.setProjection(
//            Camera.Projection.ORTHO,
//            0.0, 0.0, 0.0, 0.0, 0.0, 10.0
//        )
        setContentView(R.layout.activity_main)
        surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        choreographer = Choreographer.getInstance()
        modelViewer = ModelViewer(surfaceView)
        surfaceView.setOnTouchListener(modelViewer)
        loadGlb("gltf")

        val btnColor = findViewById<Button>(R.id.colorDoor)
        val btnReverse = findViewById<Button>(R.id.reverseDoor)

        btnColor.setOnClickListener {

        }

        //   modelViewer.scene.skybox = Skybox.Builder().environment(readAsset("envs/bg.filamesh")).build()
        modelViewer.scene.skybox = Skybox.Builder().build(modelViewer.engine)
        loadGlb("gltf")
        //  loadEnvironment("myOutDir1")
        val asset = modelViewer.asset!!
        val rm = modelViewer.engine.renderableManager
        btnColor.setOnClickListener {
            for (entity in asset.entities) {
                val renderable = rm.getInstance(entity)
                if (renderable == 0) {
                    continue
                }

                val material = rm.getMaterialInstanceAt(renderable, 0)
                if (material.name == "Dverka_liva_1") {
                    material.setParameter(
                        "baseColorFactor",
                        0.6000000715255737f,
                        0.0404584646224976f,
                        0.07538094371557236f,
                        1f
                    )
                }
            }
        }

        btnReverse.setOnClickListener {
            for (entity in asset.entities) {
                val renderable = rm.getInstance(entity)
                if (renderable == 0) {
                    continue
                }

                val material = rm.getMaterialInstanceAt(renderable, 0)
                if (material.name == "Dverka_liva_1") {
                    material.setParameter(
                        "baseColorFactor",
                        0.0000000715255737f,
                        0.6404584646224976f,
                        0.07538094371557236f,
                        1f
                    )
                }
            }
        }

        for (entity in asset.entities) {
            val renderable = rm.getInstance(entity)
            if (renderable == 0) {
                continue
            }


            if (asset.getName(entity) == "Zadniy_bamper") {
                //  rm.setLayerMask(renderable, 0xff, 0xFF0000)

            }

            val material = rm.getMaterialInstanceAt(renderable, 0)
            if (material.name == "Zadniy_bamper") {
                material.setParameter(
                    "baseColorFactor",
                    0.1000000715255737f,
                    0.6404584646224976f,
                    0.07538094371557236f,
                    1f
                )
            } else {
                material.setParameter(
                    "baseColorFactor",
                    0.1000000715255737f,
                    0.6404584646224976f,
                    0.07538094371557236f,
                    1f
                )

            }


        }
//        modelViewer.asset?.entities?.forEach { model ->
//            val instance = modelViewer.engine.renderableManager.getInstance(model)
//            val assets = modelViewer.asset?.instance?.materialInstances
//            modelViewer.engine.renderableManager.setMaterialInstanceAt(
//                model,
//                0,
//                assets!![0]
//            )
//            val materialInstance =
//                modelViewer.engine.renderableManager.getMaterialInstanceAt(instance, 0)
//         //   modelViewer.engine.renderableManager.setMaterialInstanceAt()
//        }

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


    //    startAnimation()
//        modelViewer.asset?.apply {
//            modelViewer.transformToUnitCube()
//            val rootTransform = this.root.getTransform()
//            val degrees = 20f * 3.toFloat()
//            val zAxis = Float3(0.000f, 0.000f, 0f)
//            this.root.setTransform(rootTransform * rotation(zAxis, degrees))
//        }
//        modelViewer.asset?.apply {
//            val transformMatrix = FloatArray(16)
//            Matrix.setRotateM(transformMatrix, 0, -(45f), 0.0f, 1.0f, 0.0f)
//            val tm = modelViewer.engine.transformManager
//            this.root.setTransform(this.root, transformMatrix)
//        //    tm.setTransform(tm.getInstance(this.root), transformMatrix)
//            //Matrix.setRotateM(transformMatrix, 0, -(10 as Float), 0.0f, 0.0f, 1.0f)
//          //  this.root.setTransform(rootTransform * rotation(zAxis, degrees))
//            //this.root.setTransform(rootTransform * rotation(0f,1f,0f))
//        }
    }

    private fun startAnimation() {
        // Animate the triangle
        animator.interpolator = LinearInterpolator()
        animator.duration = 4000
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            val transformMatrix = FloatArray(16)
            override fun onAnimationUpdate(a: ValueAnimator) {
                Matrix.setRotateM(transformMatrix, 0, -(a.animatedValue as Float), 0.0f, 0.0f, 1.0f)
                val tcm = engine.transformManager
                tcm.setTransform(tcm.getInstance(renderable), transformMatrix)
            }
        })
        animator.start()
    }

    private fun Int.getTransform(): Mat4 {
        val tm = modelViewer.engine.transformManager
        val dbnull: FloatArray? = null
        return Mat4.of(*tm.getTransform(tm.getInstance(this), dbnull))
    }

    private fun Int.setTransform(mat: Mat4) {
        val tm = modelViewer.engine.transformManager
        tm.setTransform(tm.getInstance(this), mat.toFloatArray())
    }
    private fun Int.setTransform(mat: Int,arr:FloatArray) {
        val tm = modelViewer.engine.transformManager
        tm.setTransform(mat, arr)
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