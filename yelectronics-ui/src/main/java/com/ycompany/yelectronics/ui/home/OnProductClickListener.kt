package com.ycompany.yelectronics.ui.home

import com.ycompany.yelectronics.network.dto.Product

interface OnProductClickListener {
    fun onProductClick(product: Product)
}