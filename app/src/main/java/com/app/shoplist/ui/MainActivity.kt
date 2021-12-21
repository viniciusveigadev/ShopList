package com.app.shoplist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.shoplist.R
import com.app.shoplist.data.model.ProductDetails
import com.app.shoplist.data.network.response.RetrofitInstance
import com.app.shoplist.databinding.ActivityMainBinding
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter
    private var productsItems: ArrayList<ProductDetails> = ArrayList()
    private var stateItems: ArrayList<ProductDetails> = ArrayList()
    private var cityItems: ArrayList<ProductDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerView()
        filterButtonVisibilty()

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

                populateMenuDropdown(response)

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

    private fun filterButtonVisibilty() {
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

    private fun populateMenuDropdown(response: Response<List<ProductDetails>>) {

        //Products
        productsItems = response.body() as ArrayList<ProductDetails>
        val data: MutableList<String> = ArrayList()
        productsItems.forEach { data.add(0, it.brand_name) }

        val adapter = ArrayAdapter<String>(
            this@MainActivity,
            R.layout.support_simple_spinner_dropdown_item,
            data
        )
        binding.productsDropdown.setAdapter(adapter)

        //States
        stateItems = response.body() as ArrayList<ProductDetails>
        val data2: MutableList<String> = ArrayList()
        stateItems.forEach { data2.add(0, it.address.state) }

        val adapter2 = ArrayAdapter<String>(
            this@MainActivity,
            R.layout.support_simple_spinner_dropdown_item,
            data2
        )
        binding.stateDropdown.setAdapter(adapter2)

        //Cities
        cityItems = response.body() as ArrayList<ProductDetails>
        val data3: MutableList<String> = ArrayList()
        cityItems.forEach { data3.add(0, it.address.city) }

        val adapter3 = ArrayAdapter<String>(
            this@MainActivity,
            R.layout.support_simple_spinner_dropdown_item,
            data3
        )
        binding.cityDropdown.setAdapter(adapter3)
    }
}