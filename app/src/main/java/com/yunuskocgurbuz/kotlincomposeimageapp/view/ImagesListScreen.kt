package com.yunuskocgurbuz.kotlincomposeimageapp.view


import android.Manifest
import android.app.Application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.yunuskocgurbuz.kotlincomposeimageapp.entity.ImagesEntity
import com.yunuskocgurbuz.kotlincomposeimageapp.util.StaggeredVerticalGrid
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesSQLiteViewModel
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesViewModelFactory


@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun ImagesListScreen(
    navController: NavController
) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA)
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CameraX Tumbe"
                    )
                },
                backgroundColor = Color.White
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    cameraPermissionState.launchPermissionRequest()
                    navController.navigate("camera_open_screen")
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add image")
            }
        },
        scaffoldState = scaffoldState
    ){

        Column {
            CallImages(navController)
        }

    }
}

@ExperimentalFoundationApi
@Composable
fun CallImages(navController: NavController) {
    val context = LocalContext.current
    val imageViewModel: ImagesSQLiteViewModel = viewModel(
        factory = ImagesViewModelFactory(context.applicationContext as Application)
    )


    val getAllImages = imageViewModel.readAllImages.observeAsState(listOf()).value


    Spacer(modifier = Modifier.padding(5.dp))

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(2.dp)
        ) {
            StaggeredVerticalGrid(
                numColumns = 2, //put the how many column you want
                modifier = Modifier.padding(2.dp)
            ) {

                for(image in getAllImages){
                    ImageRow(image, navController)
                }
            }
        }


}

@Composable
fun ImageRow(image: ImagesEntity, navController: NavController){

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {

                           navController.navigate(
                               "image_detail_screen/${image.uuId}"
                           )


                },
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp
        ) {
            Box(modifier = Modifier.height(200.dp)) {
                Image(bitmap = image.image.asImageBitmap(),
                    contentDescription = "contentDescription",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )

                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY = 300f
                        )
                    )) {

                }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(image.date.toString(), style = TextStyle(color=Color.White, fontSize = 16.sp))
                }
            }
        }
}


