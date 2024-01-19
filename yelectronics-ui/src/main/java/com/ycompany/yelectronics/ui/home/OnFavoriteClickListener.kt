package com.ycompany.yelectronics.ui.home

import com.ycompany.yelectronics.network.dto.Product

interface OnFavoriteClickListener {
    fun onFavoriteClick(favoriteProductId: String, isChecked: Boolean)
}