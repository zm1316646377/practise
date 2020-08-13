package com.example.practise.audio

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practise.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.activity_audio_player.*

private const val MEDIA_SOURCE_URL = "这里是音频源的URL"

class AudioPlayerActivity: AppCompatActivity() {

    private val player: SimpleExoPlayer by lazy {
        SimpleExoPlayer.Builder(applicationContext).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initListener()
    }

    private fun initListener() {
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // 需要设置player之后才会渲染Control视图
        player_view.player = player

        val mediaSource = ProgressiveMediaSource.Factory(
            DefaultHttpDataSourceFactory("Kathy", DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true)
        ).createMediaSource(Uri.parse(MEDIA_SOURCE_URL))

        player.prepare(mediaSource)
        // 开始播放
        player.playWhenReady = true
    }
}