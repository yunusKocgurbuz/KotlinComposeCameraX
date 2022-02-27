package com.yunuskocgurbuz.kotlincomposeimageapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yunuskocgurbuz.kotlincomposeimageapp.dao.imagesDao
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.Converters
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity

@TypeConverters(Converters::class)
@Database(entities = [ImagesEntity::class], version = 1, exportSchema = false)
abstract class ImagesDatabase: RoomDatabase() {

    abstract fun imagesDao(): imagesDao

    companion object{
        @Volatile
        private var INSTANCE: ImagesDatabase? = null

        fun getInstance(context: Context): ImagesDatabase{
            synchronized(this){
                return INSTANCE?: Room.databaseBuilder(
                    context.applicationContext,
                    ImagesDatabase::class.java,
                    "images_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}