package com.app.shoplist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.shoplist.data.network.response.RetrofitInstance
import com.app.shoplist.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        dropdownButton()

        lifecycleScope.launchWhenCreated {
            binding.progressCircular.isVisible = true

            val response = try {
                RetrofitInstance.api.getProducts()
            } catch (e: IOException) {
                Log.e(TAG, "IOException: check your internet connection")
                binding.progressCircular.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: no response")
                binding.progressCircular.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                productAdapter.products = response.body()!!
            } else {
                Log.e(TAG, "Error: no response")
            }
            binding.progressCircular.isVisible = false
        }
    }

    private fun setUpRecyclerView() = binding.rvProducts.apply {
        productAdapter = ProductAdapter()
        adapter = productAdapter
        layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun dropdownButton() {
        binding.ivFilter.setOnClickListener {
            val isMenuVisible = binding.ivDropdownfilters.isVisible
            binding.ivDropdownfilters.isVisible = !isMenuVisible
            binding.tvFilter2.isVisible = !isMenuVisible
            binding.ivLineVector5.isVisible = !isMenuVisible
            binding.menu.isVisible = !isMenuVisible
            binding.menu2.isVisible = !isMenuVisible
            binding.menu3.isVisible = !isMenuVisible
        }
    }
}