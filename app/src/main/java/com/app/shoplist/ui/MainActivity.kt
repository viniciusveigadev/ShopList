package com.app.shoplist.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        clearFiltersBtn()
        retrofitApiResponse()
        pullToRefresh()
    }

    private fun retrofitApiResponse() {

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

    fun filtersBtn(view: View) {
        val isMenuVisible = binding.ivDropdownfilters.isVisible
        binding.ivDropdownfilters.isVisible = !isMenuVisible
        binding.tvFilter2.isVisible = !isMenuVisible
        binding.ivLineVector5.isVisible = !isMenuVisible
        binding.menu.isVisible = !isMenuVisible
        binding.menu2.isVisible = !isMenuVisible
        binding.menu3.isVisible = !isMenuVisible
    }

    private fun clearFiltersBtn() {
        binding.ivClearFilters.setOnClickListener {
            binding.productsDropdown.setText("")
            binding.stateDropdown.setText("")
            binding.cityDropdown.setText("")
            Toast.makeText(this, "Filters has been cleaned", Toast.LENGTH_SHORT).show()
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

    private fun pullToRefresh() {
        //Set the colors of the Pull To Refresh View
        binding.itemsswipetorefresh.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.teal_200
            )
        )
        binding.itemsswipetorefresh.setColorSchemeColors(Color.WHITE)

        //Pull to Refresh
        binding.itemsswipetorefresh.setOnRefreshListener {
            retrofitApiResponse()
            binding.itemsswipetorefresh.isRefreshing = false
        }
    }
}