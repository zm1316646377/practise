package com.example.practise.camera

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var holder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        holder = preview_view.holder
        holder.addCallback(this)
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

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        //  Called after onResume
        Log.e("TEST", "surfaceCreated")
    }
}