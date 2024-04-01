package com.cs4520.assignment5
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class CoroutineApiCall (
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            
            val products = ApiProduct.service.getProducts().body()
            val productListByteArray = serializeProductList(products)
            val outputData = workDataOf("productList" to productListByteArray)
            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun serializeProductList(productList: ProductList?): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(productList)
        objectOutputStream.close()
        return byteArrayOutputStream.toByteArray()
    }
}