package com.ycompany.yelectronics.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ycompany.yelectronics.network.dto.Product
import com.ycompany.yelectronics.ui.R

class ProductsAdapter(
    private val onProductClickListener: OnProductClickListener,
    private val listOfProducts: List<Product>
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val productView = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product: Product = listOfProducts[position]

        holder.productBrandName.text = product.productBrand
        holder.productName.text = product.productName
        holder.productPrice.text = "${holder.itemView.context.getString(R.string.rupees)}${product.productPrice}"
        holder.productRating.rating = product.productRating

        Glide.with(holder.itemView.context)
            .load(product.productImage)
            .placeholder(R.drawable.bn)
            .into(holder.productImage)

        if (product.productHave) {
            holder.discountProductText.text = product.productDisCount
            holder.discountLayout.visibility = View.VISIBLE
        }

        // To check if the product is New or not.
        if (!product.productHave) {
            holder.discountLayout.visibility = View.VISIBLE
            holder.discountProductText.text = "New"
        }

        holder.itemView.setOnClickListener {
            onProductClickListener.onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
      //  val productAddToFav: ImageView = itemView.findViewById(R.id.product_add_to_fav)
        val productRating: RatingBar = itemView.findViewById(R.id.product_rating)
        val productBrandName: TextView = itemView.findViewById(R.id.product_brand_name)
        val discountProductText: TextView = itemView.findViewById(R.id.discount_product_text)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productPrice: TextView = itemView.findViewById(R.id.product_price)
        val discountLayout = itemView.findViewById<LinearLayout>(R.id.discount_layout)
    }
}