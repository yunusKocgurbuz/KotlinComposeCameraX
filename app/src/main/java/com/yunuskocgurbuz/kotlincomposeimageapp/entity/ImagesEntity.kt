package com.yunuskocgurbuz.kotlincomposeimageapp.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.*
import java.io.ByteArrayOutputStream


@Entity(tableName = "imagesData")
data class ImagesEntity(
    @ColumnInfo(name = "location")
    var location: String?,

    @ColumnInfo(name = "weather")
    var weather: String?,

    @ColumnInfo(name = "date")
    var date: String?,

    @ColumnInfo(name = "image")
    var image: Bitmap

) {
    @PrimaryKey(autoGenerate = true)
    var uuId : Int = 0
}

class Converters {

    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}