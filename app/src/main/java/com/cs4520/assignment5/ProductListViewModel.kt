package com.cs4520.assignment5

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.work.WorkManager

class ProductListViewModel(private val context: Context): ViewModel() {
    var productList: MutableState<ProductList?> = mutableStateOf(null)
    var fetching: MutableState<Boolean> = mutableStateOf(true)

    init {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "product"
        ).build()
        val workManager = WorkManager.getInstance(context)
        val viewModel = ViewModel(db, workManager)

        // Observe when API call has completed
        viewModel.productList.observeForever { res ->
            fetching.value = true
            var productSet: ProductList = ProductList()
            if (res != null) {
                for (p in res.distinct()) {
                    productSet.add(p)
                }
            }
            productList.value = if (productSet.size > 0) productSet else res
            fetching.value = false
        }

        viewModel.fetchProducts()
    }
}