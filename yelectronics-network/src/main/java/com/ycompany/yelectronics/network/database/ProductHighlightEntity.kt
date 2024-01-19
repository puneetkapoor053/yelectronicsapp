package com.ycompany.yelectronics.network.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ycompany.yelectronics.network.dto.Product

@Entity(tableName = "product_highlight_items")
    data class ProductHighlightEntity(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "rowId") val id: Int = 0,
        @ColumnInfo(name = "productName") val productName: String,
        @ColumnInfo(name = "productId") val productId: String,
        @ColumnInfo(name = "productPrice") val productPrice: String,
        @ColumnInfo(name = "productDes") val productDes: String,
        @ColumnInfo(name = "productRating") val productRating: Float,
        @ColumnInfo(name = "productDisCount") val productDisCount: String,
        @ColumnInfo(name = "productHave") val productHave: Boolean,
        @ColumnInfo(name = "productBrand") val productBrand: String,
        @ColumnInfo(name = "productImage") val productImage: String,
        @ColumnInfo(name = "productCategory") val productCategory: String,
        @ColumnInfo(name = "productNote") val productNote: String,
    ) {
        companion object {
            fun toEntity(product: Product): ProductHighlightEntity {
                return ProductHighlightEntity(
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