package com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.yunuskocgurbuz.kotlincomposeimageapp.database.ImagesDatabase
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity
import com.yunuskocgurbuz.kotlincomposeimageapp.repository.ImagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagesSQLiteViewModel(application: Application): AndroidViewModel(application){

    val readAllImages: LiveData<List<ImagesEntity>>

    private val repository: ImagesRepository

    init {
        val imagesDao = ImagesDatabase.getInstance(application).imagesDao()
        repository = ImagesRepository(imagesDao = imagesDao)
        readAllImages = repository.readAllImages
    }


    fun addImage(item: ImagesEntity){
        viewModelScope.launch(Dispatchers.IO ) {
            repository.addImages(item = item)
        }
    }

    fun getImageById(id : Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.getImageById(id)
        }
    }

}

class ImagesViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(ImagesSQLiteViewModel::class.java)){
            return ImagesSQLiteViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}

