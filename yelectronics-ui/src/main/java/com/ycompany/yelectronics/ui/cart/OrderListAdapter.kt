package com.ycompany.yelectronics.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.ycompany.yelectronics.network.database.OrdersListEntity
import com.ycompany.yelectronics.ui.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class OrderListAdapter(
    private val listOfOrders: List<OrdersListEntity>
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    private val viewPool = RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context)
            .inflate(R.layout.orders_list_item, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order: OrdersListEntity = listOfOrders[position]
        val context = holder.itemView.context
        var sumOfAllProducts = 0

        order.listOfOrders.forEach { orderDetails ->
            sumOfAllProducts = sumOfAllProducts + orderDetails.price * orderDetails.quantity
        }

        holder.orderTotalAmount.text =
            "${context.getString(R.string.total_amount)}$sumOfAllProducts"
        holder.orderId.text = "${context.getString(R.string.orderid)}${order.orderId}"
        holder.orderPaymentReference.text =
            "${context.getString(R.string.payment_reference)}${order.paymentReferenceId}"
        holder.dateOfOrder.text = "${context.getString(R.string.date_of_order)}" +
                "${
                    SimpleDateFormat(context.getString(R.string.order_date_format))
                        .format(Date(order.dateOfOrder))
                }"

        val layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        holder.orderDetailsRecyclerView.layoutManager = layoutManager
        holder.orderDetailsRecyclerView.setHasFixedSize(true)
        holder.orderDetailsRecyclerView.isNestedScrollingEnabled = false

        layoutManager.initialPrefetchItemCount = order.listOfOrders.size

        val orderDetailsChildAdapter = OrderDetailsChildAdapter(
            order.listOfOrders
        )
        holder.orderDetailsRecyclerView.adapter = orderDetailsChildAdapter
        holder.orderDetailsRecyclerView.setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int {
        return listOfOrders.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId: TextView = itemView.findViewById(R.id.order_id)
        val orderPaymentReference: TextView = itemView.findViewById(R.id.order_payment_reference)
        val orderTotalAmount: TextView = itemView.findViewById(R.id.order_total_amount)
        val dateOfOrder: TextView = itemView.findViewById(R.id.date_of_order)
        val orderDetailsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.order_item_recycler_view)
    }
}