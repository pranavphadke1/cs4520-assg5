package com.cs4520.assignment5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cs4520.assignment5.databinding.ProductListViewBinding

class ProductListFragment:Fragment(R.layout.product_list_view) {
    private var _product_list_binding: ProductListViewBinding? = null
    private val product_list_binding get() = _product_list_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState);
        _product_list_binding = ProductListViewBinding.inflate(inflater, container, false)

        val products = null

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "product-database"
        ).build()

        val viewModel = ViewModel(db)

        val recyclerView:RecyclerView = _product_list_binding!!.recyclerView
        val productAdapter = ProductAdapter(products,container)
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.productList.observe(viewLifecycleOwner, Observer { res ->
            _product_list_binding!!.pBar.visibility = View.GONE
            if (res == null){
                _product_list_binding!!.errorText.visibility = View.VISIBLE
                _product_list_binding!!.errorText.text = getString(R.string.no_result)
            }
            else if (res.size == 0) {
                _product_list_binding!!.errorText.visibility = View.VISIBLE
                _product_list_binding!!.errorText.text = getString(R.string.no_products)
            }
            else{
                val productList:ProductList = ProductList()
                for (p in res.distinct())
                    productList.add(p)

                productAdapter.setProducts(productList)
                productAdapter.notifyDataSetChanged()
            }
        })

        viewModel.fetchProducts()
        return product_list_binding.root
    }
}