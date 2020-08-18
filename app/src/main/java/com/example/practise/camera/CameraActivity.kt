package com.example.practise.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.hardware.Camera
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.Surface
import android.view.SurfaceHolder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.OutputStream

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PreviewCallback {

    private lateinit var mHolder: SurfaceHolder
    private lateinit var mCamera: Camera
//    private val handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        mHolder = preview_view.holder.apply {
            addCallback(this@CameraActivity)
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        initListener()
    }

    private fun initListener() {
        take_pic.setOnClickListener { view ->
            mCamera.takePicture(null, null) { bytes: ByteArray, camera: Camera ->
                // 只是为了完成demo, 实际应用大概应该在后台线程执行图片处理的逻辑

                // 旋转视图, 如果屏幕竖直着拍照 采样的数据decode之后方向不对
                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                val matrix = Matrix()
                matrix.setRotate(90F)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                val picDirectoryPath = Environment.getExternalStorageDirectory().path + File.separator + "Pictures" + File.separator + "practise"
                val picDirectory = File(picDirectoryPath)
                if (!picDirectory.exists()) {
                    picDirectory.mkdir()
                }
                val picPath = picDirectoryPath + File.separator + "IMG" + System.currentTimeMillis() + ".jpg"

                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "test.jpg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/practise")
                } else {
                    contentValues.put(MediaStore.Images.Media.DATA, picPath);
                }

                contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                var os: OutputStream? = null
                try {
                    if (uri != null) {
                        os = contentResolver.openOutputStream(uri)

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os)
                    }
                } catch (e: Exception) {

                } finally {
                    os?.close()
                }

                val exifInterface = ExifInterface(picPath)
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90.toString())
                exifInterface.saveAttributes()
                val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,  ExifInterface.ORIENTATION_NORMAL)
                Log.e("TEST", orientation.toString())
                camera.startPreview()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("TEST", "STARTED")
    }

    override fun onResume() {
        super.onResume()
        Log.e("TEST", "RESUME")
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCamera.apply {
            stopPreview()
            setPreviewCallback(null)
            release()
        }
        mCamera
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        //  Called after onResume
        Log.e("TEST", "surfaceCreated")
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)

        mCamera.apply {
            setPreviewDisplay(mHolder)
            setDisplayOrientation(getDisplayOrientation())
            val parameters = parameters.apply {
                focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                exposureCompensation = -10 // 曝光补偿
                val point = findBestPreviewSizeValue(supportedPreviewSizes, getPreviewSurfaceVResolution())
                setPreviewSize(point?.x ?: preview_view.height, point?.y ?: preview_view.width)
            }
            // FOCUS_MODE_CONTINUOUS_PICTURE必须调用cancelAutoFocus
            cancelAutoFocus()
            setParameters(parameters)
            startPreview()
            setPreviewCallback(this@CameraActivity)
        }

//        handler.postDelayed(runnable, 1000)
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
//        https://blog.csdn.net/tinyzhao/article/details/52681600
//        val canvas = mHolder.lockCanvas()
//        val paint = Paint()
//        canvas.drawArc(10F, 10F, 90F, 90F, 0F, 100F, true, paint)
    }

    // 设置预览方向
    private fun getDisplayOrientation(): Int {
        val info = Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info)
        val wm: WindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager;
        val display:Display = wm.defaultDisplay;

        val rotation: Int = display.rotation;
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0;
            Surface.ROTATION_90 -> degrees = 90;
            Surface.ROTATION_180 -> degrees = 180;
            Surface.ROTATION_270 -> degrees = 270;
        }

        var result: Int;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private fun findBestPreviewSizeValue(
        supportSizeList: List<Camera.Size>,
        screenResolution: Point
    ): Point? {
        var bestX = 0
        var bestY = 0
        var diff = Int.MAX_VALUE
        for (previewSize in supportSizeList) {
            val newX = previewSize.width
            val newY = previewSize.height
            val newDiff =
                Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y)
            if (newDiff == 0) {
                bestX = newX
                bestY = newY
                break
            } else if (newDiff < diff) {
                bestX = newX
                bestY = newY
                diff = newDiff
            }
        }
        return if (bestX > 0 && bestY > 0) {
            Point(bestX, bestY)
        } else null
    }

    private fun getPreviewSurfaceVResolution(): Point {
        val screenResolution = Point()
        // 预览视图（屏幕）宽高和预览分辨率宽高相反, 否则预览视图会被拉伸
        screenResolution.x = preview_view.height
        screenResolution.y = preview_view.width
        return screenResolution
    }

//    private val runnable: Runnable = object : Runnable {
//        override fun run() {
//            mCamera.apply {
//                cancelAutoFocus()
////                val parameters = parameters.apply {
////                    focusMode = Camera.Parameters.FOCUS_MODE_MACRO
////                    exposureCompensation = 5
////                }
////                setParameters(parameters)
//                autoFocus {success, camera ->
//                    println(success)
//                }
//            }
////            handler.postDelayed(this, 1000)
//        }
//    }
}