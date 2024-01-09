package com.ycompany.yelectronics.ui.home

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.HomeFragmentBinding
import com.ycompany.yelectronics.ui.injections.CustomViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    private lateinit var homeViewModel: HomeViewModel
    lateinit var highlightRecyclerView: RecyclerView


    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this, customViewModelFactory)[HomeViewModel::class.java]

        binding?.apply {

            //welcomeText.text = "Welcome ${homeViewModel.getUserName()}"
        }
    }

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}