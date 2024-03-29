package com.cs4520.assignment5

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(private val db: AppDatabase) : ViewModel() {
    var productList = MutableLiveData<ProductList?>()
    private val productDao = db.productDao()


    fun fetchProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val products = ApiProduct.service.getProducts().body()
                if (products != null){
                    productDao.deleteAll()
                    productDao.insertAll(*products.toTypedArray())
                }
                productList.postValue(products)

            } catch (e: Exception) {
                val productsInDB = productDao.getAll()
                val pList: ProductList = ProductList()
                for (p in productsInDB){
                    pList.add(p)
                }
                productList.postValue(pList)
            }

        }
    }
}