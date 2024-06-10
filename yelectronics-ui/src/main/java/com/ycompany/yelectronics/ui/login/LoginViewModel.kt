package com.ycompany.yelectronics.ui.login

import android.util.Patterns
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.network.repository.CartRepository
import com.ycompany.yelectronics.network.repository.FavoritesRepository
import com.ycompany.yelectronics.viewmodel.StateLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val favoritesRepository: FavoritesRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val singInLiveData: StateLiveData<Boolean> = StateLiveData()
    private val signInAnonymously: StateLiveData<Boolean> = StateLiveData()

    fun signInUser(email: String, password: String) {
        singInLiveData.postLoading()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signIn ->
            if (signIn.isSuccessful) {
                singInLiveData.postSuccess(true)
            } else {
                singInLiveData.postError(Throwable(signIn.exception?.message))
            }
        }
    }

    fun signInUserAnonymously() {
        signInAnonymously.postLoading()
        firebaseAuth.signInAnonymously().addOnCompleteListener { signIn ->
            if (signIn.isSuccessful) {
                signInAnonymously.postSuccess(true)
            } else {
                signInAnonymously.postError(Throwable(signIn.exception?.message))
            }
        }
    }

    fun signOutUser() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signOut()
            favoritesRepository.deleteAllFavorites()
            cartRepository.deleteAllCartItems()
            cartRepository.deleteAllOrders()
        }
    }

    fun singInLiveDataObservable(): StateLiveData<Boolean> {
        return singInLiveData
    }

    fun signInAnonymouslyObservable(): StateLiveData<Boolean> {
        return signInAnonymously
    }

    fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }
}