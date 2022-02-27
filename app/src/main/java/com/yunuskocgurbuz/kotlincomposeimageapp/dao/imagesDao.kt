package com.yunuskocgurbuz.kotlincomposeimageapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity

@Dao
interface imagesDao {

    @Query("SELECT * FROM imagesData")
    fun getAllImages(): LiveData<List<ImagesEntity>>

    @Query("SELECT * FROM imagesData WHERE uuId = :id")
    fun getById(id: Int): ImagesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(item: ImagesEntity)

}