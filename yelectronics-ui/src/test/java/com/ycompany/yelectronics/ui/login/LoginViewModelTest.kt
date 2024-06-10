package com.ycompany.yelectronics.ui.login

import com.google.firebase.auth.FirebaseAuth
import com.ycompany.yelectronics.network.repository.CartRepository
import com.ycompany.yelectronics.network.repository.FavoritesRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    @MockK
    lateinit var firebaseAuth: FirebaseAuth

    @MockK
    lateinit var favoritesRepository: FavoritesRepository

    @MockK
    lateinit var cartRepository: CartRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        loginViewModel = LoginViewModel(firebaseAuth, favoritesRepository, cartRepository)
    }

    @Test
    fun isEmailValid_whenValid_returnTrue() {
        assertTrue(loginViewModel.isEmailValid("sample1@sample.com"))
        assertFalse(loginViewModel.isEmailValid("sample1sample.com"))
        assertFalse(loginViewModel.isEmailValid("sample1@samplecom"))
        assertTrue(loginViewModel.isEmailValid("abc@abc.com"))
    }
}