package com.yunuskocgurbuz.kotlincomposeimageapp.view


import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.yunuskocgurbuz.kotlincomposeimageapp.R
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity
import com.yunuskocgurbuz.kotlincomposeimageapp.model.weather.WeatherModel
import com.yunuskocgurbuz.kotlincomposeimageapp.ui.theme.Purple500
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesSQLiteViewModel
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesViewModelFactory
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.WeatherViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.location.*


@SuppressLint("PermissionLaunchedDuringComposition")
@ExperimentalPermissionsApi
@Composable
fun CameraOpenScreen(directory: File, navController: NavController, myCity: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current



    Column() {



        SimpleCameraPreview(
            navController,
            myCity,
            modifier = Modifier.fillMaxSize(),
            context = context,
            lifecycleOwner = lifecycleOwner,
            outputDirectory = directory,
            onMediaCaptured = { url -> }
        )
    }


}


@Composable
fun WeatherDetail(location: String) : String? {
    val viewModel: WeatherViewModel = hiltViewModel()
    var weather: String? = null

    val weatherInfo by produceState<Resource<WeatherModel>>(initialValue = Resource.Loading()) {
        value = viewModel.getWeatherForLocation(location)

    }

    when(weatherInfo) {

        is Resource.Success -> {

            val selectedMovie = weatherInfo.data!!

            weather = selectedMovie.description + "  " +selectedMovie.temperature


        }
    }

    return weather
}

@Composable
fun SimpleCameraPreview(
    navController: NavController,
    myCity: String,
    modifier: Modifier = Modifier,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    outputDirectory: File,
    onMediaCaptured: (Uri?) -> Unit
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    val camera: Camera? = null
    var lensFacing by remember { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashEnabled by remember { mutableStateOf(false) }
    var flashRes by remember { mutableStateOf(R.drawable.ic_outline_flashlight_on) }
    val executor = ContextCompat.getMainExecutor(context)
    var cameraSelector: CameraSelector?
    val cameraProvider = cameraProviderFuture.get()
    val context = LocalContext.current
    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(executor, FaceAnalyzer())
                        }
                    imageCapture = ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        imageCapture,
                        preview
                    )
                }, executor)
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Purple500, RoundedCornerShape(15.dp))
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    camera?.let {
                        if (it.cameraInfo.hasFlashUnit()) {
                            flashEnabled = !flashEnabled
                            flashRes = if (flashEnabled) R.drawable.ic_outline_flashlight_off else
                                R.drawable.ic_outline_flashlight_on
                            it.cameraControl.enableTorch(flashEnabled)
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = flashRes),
                    contentDescription = "",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colors.surface
                )
            }

            var bitmap: Bitmap? = null
            val context = LocalContext.current
            val imageViewModel: ImagesSQLiteViewModel = viewModel(
                factory = ImagesViewModelFactory(context.applicationContext as Application)
            )
            var weather = WeatherDetail(myCity)

            Button(
                onClick = {
                    val imgCapture = imageCapture ?: return@Button
                    val photoFile = File(
                        outputDirectory,
                        SimpleDateFormat("yyyyMMDD-HHmmss", Locale.US)
                            .format(System.currentTimeMillis()) + ".jpg"
                    )
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    imgCapture.takePicture(
                        outputOptions,
                        executor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                onMediaCaptured(Uri.fromFile(photoFile))



                                Uri.fromFile(photoFile)?.let {

                                        bitmap = if (Build.VERSION.SDK_INT < 28) {
                                            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                                        } else {
                                            val source = ImageDecoder.createSource(context.contentResolver, it)
                                            ImageDecoder.decodeBitmap(source)
                                        }
                                    }


                                var btmSql: Bitmap? = bitmap!!.scale(750,750, true)

                                val date = SimpleDateFormat("dd.MM.yyyy HH:mm")
                                val imageDate: String = date.format(Date())


                                if(weather == null){
                                    weather = ""
                                }

                                val insertImageDate = ImagesEntity(myCity, weather, imageDate, btmSql!!)


                                imageViewModel.addImage(insertImageDate)

                                navController.navigate(
                                    "images_list_screen"
                                )



                            }
                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )


                },
                modifier = Modifier
                    .size(70.dp)
                    .background(Purple500, CircleShape)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .border(5.dp, Color.LightGray, CircleShape),
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple500),
            ) {

            }

            IconButton(
                onClick = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK

                    cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector as CameraSelector,
                        imageCapture,
                        preview
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_rotate),
                    contentDescription = "",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colors.surface
                )
            }
        }
    }
}


private class FaceAnalyzer(): ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val imagePic = image.image
        imagePic?.close()
    }
}














