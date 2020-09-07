package com.example.practise.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import com.example.practise.common.BaseAdapter
import kotlinx.android.synthetic.main.activity_recyclerview_test.*

// 测试GridView横向滚动分页
class RecyclerViewTestActivity: AppCompatActivity() {

    private lateinit var mAdapter: BaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_test)
        mAdapter = BaseAdapter(this)
        recycler_view.apply {
            this.adapter = mAdapter
        }
    }
}