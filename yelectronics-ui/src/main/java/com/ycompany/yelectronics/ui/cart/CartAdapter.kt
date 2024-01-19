package com.ycompany.yelectronics.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ycompany.yelectronics.network.database.CartEntity
import com.ycompany.yelectronics.ui.R

class CartAdapter(
    private val listener: CartItemClickListener,
    private val listOfCartProducts: ArrayList<CartEntity>
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val cartView =
            LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item, parent, false)

        return CartViewHolder(cartView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {


        val cartItem: CartEntity = listOfCartProducts[position]

        holder.cartName.text = cartItem.name
        holder.cartPrice.text = "₹ " + cartItem.price * cartItem.quantity
        holder.quantityTvCart.text = cartItem.quantity.toString()
        holder.cartMore.setOnClickListener {

        }

        Glide.with(holder.itemView.context)
            .load(cartItem.productImage)
            .into(holder.cartImage)

        holder.cartMore.setOnClickListener {
            listener.onItemDeleteClick(cartItem)
        }
    }

    override fun getItemCount(): Int {
        return listOfCartProducts.size
    }
    fun updateList(){
        notifyDataSetChanged()
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cartImage: ImageView = itemView.findViewById(R.id.cartImage)
        val cartMore: ImageView = itemView.findViewById(R.id.cartDelete)
        val cartName: TextView = itemView.findViewById(R.id.cartName)
        val cartPrice: TextView = itemView.findViewById(R.id.cartPrice)
        val quantityTvCart: TextView = itemView.findViewById(R.id.quantityTvCart)
    }
}

interface CartItemClickListener {
    fun onItemDeleteClick(product: CartEntity)
    fun onItemUpdateClick(product: CartEntity)
}