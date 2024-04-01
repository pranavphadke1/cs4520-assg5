package com.cs4520.assignment5

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun ProductListWrapper(context: Context) {
    val viewModel = ProductListViewModel(context)
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            val productList by remember {
                viewModel.productList
            }
            val fetching by remember {
                viewModel.fetching
            }
            if (fetching) {
                CircularProgressIndicator(modifier = Modifier.width(15.dp))
            }
            else {
                if (productList != null && productList!!.size > 0) {
                    LazyColumn() {
                        items(productList!!) { product ->
                            ProductItem(product = product)
                        }
                    }
                } else if (productList == null) {
                    Text(text = stringResource(id = R.string.no_result))
                } else {
                    Text(text = stringResource(id = R.string.no_products))
                }
            }
        }
    }
}


@Composable
fun ProductItem(product: Product) {
    val img = if (product.type=="Equipment") R.drawable.equipment else R.drawable.food
    val desc = if (product.type=="Equipment") R.string.equipment_image_desc else R.string.food_image_desc
    val background = if (product.type=="Equipment") Color(0xFFE06666) else Color(0xFFFFD965)
    Row(
        modifier = Modifier
            .background(color = background)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = img),
            contentDescription = stringResource(id = desc),
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Crop)
        Column {
            Text(text = product.name)
            Text(text = product.price.toString())
            if (product.expiryDate != null) {
                Text(text = product.expiryDate)
            }
        }
    }
}