package com.example.practise.coordinatordemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.practise.R
import kotlinx.android.synthetic.main.activity_coordinator.*

class CoordinatorActivity : AppCompatActivity() {
    private lateinit var adapter: BaseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
        adapter = BaseAdapter(this)
        recycler.adapter = adapter
//        collapse_layout.post {
//            collapse_layout.minimumHeight = 300
//        }
    }

    public fun test() {

    }
}