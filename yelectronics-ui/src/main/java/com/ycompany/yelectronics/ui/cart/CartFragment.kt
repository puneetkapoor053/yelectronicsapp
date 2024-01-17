package com.ycompany.yelectronics.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ycompany.yelectronics.ui.base.BaseFragment
import com.ycompany.yelectronics.ui.databinding.CartFragmentBinding

class CartFragment : BaseFragment<CartFragmentBinding>() {

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        binding = CartFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}