package com.ycompany.yelectronics.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ycompany.yelectronics.network.dto.Orders
import com.ycompany.yelectronics.ui.R

class OrderDetailsChildAdapter(
    private val listOfOrderDetails: List<Orders>
) :
    RecyclerView.Adapter<OrderDetailsChildAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_list_item, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val orderDetails: Orders = listOfOrderDetails[position]

        holder.cartName.text = orderDetails.name
        holder.cartPrice.text =
            "${context.getString(R.string.rupees)}${orderDetails.price * orderDetails.quantity}"
        holder.quantityTvCart.text = orderDetails.quantity.toString()

        holder.plusLayout.visibility = View.INVISIBLE
        holder.minusLayout.visibility = View.INVISIBLE
        holder.cartDelete.visibility = View.INVISIBLE

        Glide.with(context)
            .load(orderDetails.productImage)
            .into(holder.cartImage)
    }

    override fun getItemCount(): Int {
        return listOfOrderDetails.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cartImage: ImageView = itemView.findViewById(R.id.cartImage)
        val cartMore: ImageView = itemView.findViewById(R.id.cartDelete)
        val cartName: TextView = itemView.findViewById(R.id.cartName)
        val cartPrice: TextView = itemView.findViewById(R.id.cartPrice)
        val quantityTvCart: TextView = itemView.findViewById(R.id.quantityTvCart)

        val plusLayout: ImageView = itemView.findViewById(R.id.plusLayout)
        val minusLayout: ImageView = itemView.findViewById(R.id.minusLayout)
        val cartDelete: ImageView = itemView.findViewById(R.id.cartDelete)

    }
}