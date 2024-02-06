package com.ycompany.yelectronics.network.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "orders_list")
data class OrdersListEntity(
    @ColumnInfo(name = "orders_list") var listOfOrders: List<Orders>,
    @PrimaryKey @ColumnInfo(name = "order_id") var orderId: String,
    @ColumnInfo(name = "payment_reference_id") var paymentReferenceId: String,
    @ColumnInfo(name = "date_of_order") var dateOfOrder: Long
)
