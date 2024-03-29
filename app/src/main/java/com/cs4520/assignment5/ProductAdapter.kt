package com.cs4520.assignment5

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment5.databinding.ProductItemBinding

class ProductAdapter(private var products: ProductList?,
                     private val container: ViewGroup?):RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val productItemBinding: ProductItemBinding =
            ProductItemBinding.inflate(inflater, container, false)
        return ProductViewHolder(productItemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products!![position]
        holder.name.text = product.name
        val text = product.expiryDate ?: ""
        holder.expiryDate.text = text
        if (text == "") {
            holder.expiryDate.visibility = TextView.GONE
        }
        holder.price.text = "$ " + product.price.toString()
        if (product.type == "Equipment") {
            holder.type.setImageResource(R.drawable.equipment)
            holder.itemView.setBackgroundColor(0xFFE06666.toInt())
        } else if (product.type == "Food") {
            holder.type.setImageResource(R.drawable.food)
            holder.itemView.setBackgroundColor(0xFFFFD965.toInt())
        }
    }

    fun setProducts(productList: ProductList) {
        products = productList
    }


    override fun getItemCount(): Int {
        return products?.size ?: 0
    }
}


class ProductViewHolder(productItemBinding: ProductItemBinding): RecyclerView.ViewHolder(productItemBinding.root) {
    val name:TextView = productItemBinding.name
    val type:ImageView = productItemBinding.type
    val expiryDate: TextView = productItemBinding.expiryDate
    val price: TextView = productItemBinding.price
}
