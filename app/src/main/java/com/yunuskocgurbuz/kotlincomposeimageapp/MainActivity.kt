package com.yunuskocgurbuz.kotlincomposeimageapp


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.*
import com.yunuskocgurbuz.kotlincomposeimageapp.ui.theme.KotlinComposeImageAppTheme
import com.yunuskocgurbuz.kotlincomposeimageapp.view.CameraOpenScreen
import com.yunuskocgurbuz.kotlincomposeimageapp.view.ImageDetailScreen
import com.yunuskocgurbuz.kotlincomposeimageapp.view.ImagesListScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.*

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val PERMISSION_ID = 1010
    var myCity: String? = null

    @ExperimentalPermissionsApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            RequestPermission()
            getLastLocation()


        setContent {
            KotlinComposeImageAppTheme {

                val navController = rememberNavController()
                NavHost(navController =  navController, startDestination = "images_list_screen"){

                    composable("camera_open_screen"){

                        CameraOpenScreen(getDirectory(), navController, myCity!!)

                    }

                    composable("images_list_screen"){

                       ImagesListScreen(navController)

                        val activity = (LocalContext.current as? Activity)
                        BackHandler(true) {
                            activity?.finish()
                        }

                    }

                    composable("image_detail_screen/{image_id}", arguments = listOf(
                        navArgument("image_id"){
                            type = NavType.StringType
                        }
                    )){
                        val image_id = remember {
                            it.arguments?.getString("image_id")
                        }

                        ImageDetailScreen(id = image_id ?: "")
                    }

                }



            }
        }
    }

    //Store the capture image
    private fun getDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }


    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{

                        var geoCoder = Geocoder(this, Locale.getDefault())
                        var Adress = geoCoder.getFromLocation(location.latitude, location.longitude,3)
                        if(myCity == null){
                            myCity = "Van" // my sweet city :)
                        }else{
                            myCity = Adress.get(0).locality
                        }

                        Log.d("Debug:" ,"Your Location:"+ myCity)

                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
            }
        }else{
            RequestPermission()
        }
    }


    @SuppressLint("MissingPermission")
    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()!!
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            Log.d("Debug:","your last location: "+ lastLocation.longitude.toString())

        }
    }

    private fun CheckPermission():Boolean{
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }


}

