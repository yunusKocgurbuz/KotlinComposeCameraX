package com.yunuskocgurbuz.kotlincomposeimageapp.view

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesSQLiteViewModel
import com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel.ImagesViewModelFactory

@Composable
fun ImageDetailScreen(id: String) {

    ImageDetail(id)

}

@Composable
fun ImageDetail(id: String) {
    val context = LocalContext.current
    val userViewModel: ImagesSQLiteViewModel = viewModel(
        factory = ImagesViewModelFactory(context.applicationContext as Application)
    )


    val getUserRecord = userViewModel.readAllImages.observeAsState(listOf()).value


    Scaffold {

            if (getUserRecord.isNotEmpty()) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(5.dp),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 5.dp
                ) {
                    Box(modifier = Modifier.height(200.dp)) {
                        Image(bitmap = getUserRecord[id.toInt()-1].image.asImageBitmap(),
                            contentDescription = "contentDescription",
                            contentScale = ContentScale.FillBounds,
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
                            Column() {
                                Text(getUserRecord[id.toInt()-1].date.toString(), style = TextStyle(color=Color.White, fontSize = 16.sp))
                                Text(getUserRecord[id.toInt()-1].location.toString(), style = TextStyle(color=Color.White, fontSize = 16.sp))
                                Text(getUserRecord[id.toInt()-1].weather.toString(), style = TextStyle(color=Color.White, fontSize = 16.sp))
                            }

                        }
                    }
                }
            }

    }
}