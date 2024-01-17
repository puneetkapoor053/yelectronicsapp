package com.ycompany.yelectronics.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.ui.R

class ProductHighlightAdapter(
    private val onProductClickListener: OnProductClickListener,
    private val listOfProductHighlights: List<Product>
) :
    RecyclerView.Adapter<ProductHighlightAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_highlight_list_item, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = listOfProductHighlights[position]
        holder.productHighlightTitle.text = product.productNote
        Glide.with(holder.itemView.context)
            .load(product.productImage)
            .into(holder.productHighlightImage)
        holder.productHighlightClick.setOnClickListener {
            onProductClickListener.onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return listOfProductHighlights.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productHighlightImage: ImageView = itemView.findViewById(R.id.productImage_coverPage)
        val productHighlightTitle: TextView = itemView.findViewById(R.id.productNoteCover)
        val productHighlightClick: Button = itemView.findViewById(R.id.productCheck_coverPage)
    }
}