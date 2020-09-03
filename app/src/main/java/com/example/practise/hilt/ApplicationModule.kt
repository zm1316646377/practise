package com.example.practise.hilt

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.practise.App
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton
import com.example.practise.hilt.Constants.PREFERENCES_APP_INFO
import com.example.practise.hilt.Constants.PREFERENCES_USER_INFO
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@InstallIn(ApplicationComponent::class)
@Module
class ApplicationModule {
    @Singleton
    @Provides
    @Named(Constants.PREFERENCES_APP_INFO)
    fun provideSharedPreference(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            PREFERENCES_APP_INFO,
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    @Named(PREFERENCES_USER_INFO)
    fun provideUserSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences(
            PREFERENCES_USER_INFO,
            Context.MODE_PRIVATE
        )
    }
}
