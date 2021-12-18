package com.app.shoplist.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.shoplist.databinding.ItemProductsBinding
import com.app.shoplist.data.model.ProductDetails
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductsBinding) :
        RecyclerView.ViewHolder(binding.root)


    private val diffCalback = object : DiffUtil.ItemCallback<ProductDetails>() {
        override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem.date == newItem.date //id isn't available on JSON but must be replaced here
        }

        override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCalback)
    var products: List<ProductDetails>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.binding.apply {
            val product = products[position]

            tvProductName.text = product.product_name
            tvBrandName.text = product.brand_name
            tvPrice.text = "$ ${product.price}"
            tvLocation.text = product.address.state
            tvDate.text = "Date: ${product.date}"
            tvDescription.text = product.discription

            Glide.with(holder.itemView.context)
                .load(product.image)
                .transform(RoundedCorners(10))
                .into(image)
        }
    }

}