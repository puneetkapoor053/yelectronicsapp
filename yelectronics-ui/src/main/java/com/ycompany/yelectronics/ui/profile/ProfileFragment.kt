package com.ycompany.yelectronics.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.HomeFragmentBinding
import com.ycompany.yelectronics.ui.databinding.ProfileFragmentBinding
import com.ycompany.yelectronics.ui.home.HomeActivity
import com.ycompany.yelectronics.injections.CustomViewModelFactory
import com.ycompany.yelectronics.ui.R
import com.ycompany.yelectronics.ui.cart.OrderDetailsFragment
import com.ycompany.yelectronics.ui.login.LoginActivity
import com.ycompany.yelectronics.ui.login.LoginViewModel
import com.ycompany.yelectronics.utils.Constants
import com.ycompany.yelectronics.utils.Extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding>() {
    private lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel = ViewModelProvider(this, customViewModelFactory)[LoginViewModel::class.java]

        binding?.apply {
            myOrdersLayout.setOnClickListener {
                //activity?.applicationContext?.let { toast("My Orders Clicked", it) }
                findNavController().navigate(R.id.action_orderList)
            }
            signOutLayout.setOnClickListener {
                activity?.applicationContext?.let { toast("SignOut Clicked", it) }
                loginViewModel.signOutUser()
                sharedPreferences.edit().remove(Constants.PREF_USERNAME).apply()

                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }

    companion object {
        fun getInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
}