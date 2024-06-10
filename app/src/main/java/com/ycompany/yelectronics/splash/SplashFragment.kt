package com.ycompany.yelectronics.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.databinding.SplashFragmentBinding
import com.ycompany.yelectronics.network.BuildConfig
import com.ycompany.yelectronics.ui.home.HomeActivity
import com.ycompany.yelectronics.ui.login.LoginActivity
import com.ycompany.yelectronics.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashFragmentBinding>() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = SplashFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animateIntro()
    }

    private fun animateIntro() {
        view?.animate()?.alpha(1f)?.setDuration(2000)
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    launchModule()
                }
            })
    }

    private fun launchModule() {
        val intent: Intent =
            if (sharedPreferences.getString(Constants.PREF_USERNAME, null) == null) {
                Intent(activity, LoginActivity::class.java)
            } else {
                Intent(activity, HomeActivity::class.java)
            }
        startActivity(intent)
        activity?.finish()
    }

    companion object {
        fun getInstance(): SplashFragment {
            return SplashFragment()
        }
    }
}