package com.ycompany.yelectronics.network.dto

import android.os.Parcelable
import com.ycompany.yelectronics.network.database.ProductHighlightEntity
import com.ycompany.yelectronics.network.database.ProductsEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val productName: String = "",
    val productId: String = "",
    val productPrice: String = "",
    val productDes: String = "",
    val productRating: Float = 0.0F,
    val productDisCount: String = "",
    val productHave: Boolean = false,
    val productBrand: String = "",
    val productImage: String = "",
    val productCategory: String = "",
    val productNote: String = ""
) : Parcelable {
    companion object {
        fun toHighlightDTO(product: ProductHighlightEntity): Product {
            return Product(
                productName = product.productName,
                productId = product.productId,
                productPrice = product.productPrice,
                productDes = product.productDes,
                productRating = product.productRating,
                productDisCount = product.productDisCount,
                productHave = product.productHave,
                productBrand = product.productBrand,
                productImage = product.productImage,
                productCategory = product.productCategory,
                productNote = product.productNote
            )
        }

        fun toProductDTO(product: ProductsEntity): Product {
            return Product(
                productName = product.productName,
                productId = product.productId,
                productPrice = product.productPrice,
                productDes = product.productDes,
                productRating = product.productRating,
                productDisCount = product.productDisCount,
                productHave = product.productHave,
                productBrand = product.productBrand,
                productImage = product.productImage,
                productCategory = product.productCategory,
                productNote = product.productNote
            )
        }
    }
}