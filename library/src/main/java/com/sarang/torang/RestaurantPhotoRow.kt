package com.sarang.torang

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed interface RestaurantPhotoRowData {
    class Single(url : String) : RestaurantPhotoRowData
    class Double(url : String, url1 : String) : RestaurantPhotoRowData
}

@Preview(showBackground = true)
@Composable
fun RestaurantPhotoRow(modifier : Modifier = Modifier.height(300.dp),
                       list : List<RestaurantPhotoRowData> = emptyList()){
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(list){
            when(it){
                is RestaurantPhotoRowData.Single -> {
                    Image(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .widthIn(50.dp, 260.dp)
                            .fillMaxHeight(),
                        painter = painterResource(R.drawable.original),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }

                is RestaurantPhotoRowData.Double -> {
                    Column {
                        Image(
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                                .widthIn(50.dp, 130.dp)
                                .fillMaxHeight()
                                .weight(1f)
                            ,
                            painter = painterResource(R.drawable.original),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                                .widthIn(50.dp, 130.dp)
                                .fillMaxHeight()
                                .weight(1f)
                            ,
                            painter = painterResource(R.drawable.original),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewRestaurantPhotoRow(){
    RestaurantPhotoRow(
        list = listOf(
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            RestaurantPhotoRowData.Single(""),
            RestaurantPhotoRowData.Double("", ""),
            )
    )
}