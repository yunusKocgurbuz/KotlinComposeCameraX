package com.yunuskocgurbuz.kotlincomposeimageapp.repository

import androidx.lifecycle.LiveData
import com.yunuskocgurbuz.kotlincomposeimageapp.dao.imagesDao
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity

class ImagesRepository(private  val imagesDao: imagesDao) {

    val readAllImages: LiveData<List<ImagesEntity>> = imagesDao.getAllImages()

    suspend fun addImages(item: ImagesEntity){
        imagesDao.insertImage(item)
    }

    suspend fun getImageById(id: Int){
        imagesDao.getById(id)
    }
}