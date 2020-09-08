package com.example.practise.hilt

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import com.example.practise.hilt.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_hilt_test.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HiltTestActivity: AppCompatActivity() {

    @Inject
    @Named(Constants.PREFERENCES_USER_INFO)
    lateinit var userInfoSharePreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hilt_test)
        val name = userInfoSharePreference.getString("name", "Kathy")
        name_view.text = name
    }

}