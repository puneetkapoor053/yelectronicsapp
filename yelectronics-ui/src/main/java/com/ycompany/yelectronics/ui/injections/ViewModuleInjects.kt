package com.ycompany.yelectronics.ui.injections

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.ui.home.HomeViewModel
import com.ycompany.yelectronics.ui.login.LoginViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@InstallIn(SingletonComponent::class)
@Module
object ViewModuleInjects {

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModel(firebaseAuth: FirebaseAuth): ViewModel {
        return LoginViewModel(firebaseAuth)
    }

    @Provides
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(sharedPreferences: SharedPreferences): ViewModel {
        return HomeViewModel(sharedPreferences)
    }
}