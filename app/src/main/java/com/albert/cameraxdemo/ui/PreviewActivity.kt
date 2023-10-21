package com.albert.cameraxdemo.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.albert.cameraxdemo.databinding.ActivityPreviewBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PreviewActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private lateinit var binding: ActivityPreviewBinding

    //cameraX 包下
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    //Java任务执行服务
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.btnPreview.setOnClickListener {
            // Request camera permissions
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }
        binding.btnTakePhoto.setOnClickListener {

        }
    }

    private fun startCamera() {
        val cameraproviderFuture = ProcessCameraProvider.getInstance(this)//可监听任务
        cameraproviderFuture.addListener(Runnable {
            //此实例用于将相机的生命周期绑定到生命周期所有者
            //由于 CameraX 具有生命周期感知能力，所以这样可以省去打开和关闭相机的任务。
            val cameraProvider: ProcessCameraProvider = cameraproviderFuture.get()
            //预览
            //Preview，一个CameraX定义的 UseCase。Preview本身也 继承了 UserCase
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            //选择后置摄像头
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                //unbind use cases before rebinding
                //use case ：cameraX这套api的重要概念
                cameraProvider.unbindAll()
                //bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


}