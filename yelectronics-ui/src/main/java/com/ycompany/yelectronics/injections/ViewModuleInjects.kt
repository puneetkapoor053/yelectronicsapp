package com.ycompany.yelectronics.injections

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.network.repository.CartRepository
import com.ycompany.yelectronics.network.repository.FavoritesRepository
import com.ycompany.yelectronics.network.repository.ProductsRepository
import com.ycompany.yelectronics.ui.cart.CartViewModel
import com.ycompany.yelectronics.ui.favorite.FavoritesViewModel
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
        productsRepository: ProductsRepository
    ): ViewModel {
        return HomeViewModel(sharedPreferences, productsRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun favoritesViewModel(
        favoritesRepository: FavoritesRepository,
        productsRepository: ProductsRepository
    ): ViewModel {
        return FavoritesViewModel(favoritesRepository, productsRepository)
    }

    @Provides
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    fun cartViewModel(
        cartRepository: CartRepository
    ): ViewModel {
        return CartViewModel(cartRepository)
    }
}