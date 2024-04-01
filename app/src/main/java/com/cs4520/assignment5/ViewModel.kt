package com.cs4520.assignment5

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.util.concurrent.TimeUnit

class ViewModel(private val db: AppDatabase, private val workManager: WorkManager) : ViewModel() {

    var productList = MutableLiveData<ProductList?>()
    private val productDao = db.productDao()

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()


        val periodicRequest = PeriodicWorkRequestBuilder<CoroutineApiCall>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "coroutineApiCall",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicRequest
        )


        val workInfoLiveData = workManager.getWorkInfoByIdLiveData(periodicRequest.id)

        workInfoLiveData.observeForever() { workInfo ->
            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val outputData = workInfo.outputData
                val productListByteArray = outputData.getByteArray("productList")
                val productListFromWorker = deserializeProductList(productListByteArray)

                productList.value = productListFromWorker
            }
        }
    }

    private fun deserializeProductList(byteArray: ByteArray?): ProductList? {
        if (byteArray == null) return null
        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val productList = objectInputStream.readObject() as ProductList
        objectInputStream.close()
        return productList
    }


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