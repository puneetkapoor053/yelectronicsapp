package com.ycompany.yelectronics.injections

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.network.repository.ProductHighlightRepository
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
    fun homeViewModel(
        sharedPreferences: SharedPreferences,
        productHighlightRepository: ProductHighlightRepository
    ): ViewModel {
        return HomeViewModel(sharedPreferences, productHighlightRepository)
    }
}