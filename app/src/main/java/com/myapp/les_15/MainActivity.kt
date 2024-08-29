package com.myapp.les_15

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.myapp.les_15.database.Product

import com.myapp.les_15.navigation.Navigation
import com.myapp.les_15.navigation.Screen
import com.myapp.les_15.ui.theme.Les_15Theme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Les_15Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(viewModel)
                 }
            }
        }
    }
}

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: MainViewModel
    ) {
    LaunchedEffect(Unit) {
        viewModel.navigateToProducts {
            navController.navigate(Screen.ProductsListScreen.route) {
                popUpTo(Screen.LoginScreen.route) {
                    inclusive = true
                }
            }
        }

    }

    var username by rememberSaveable {
        mutableStateOf("emilys")
    }
    var password by rememberSaveable {
        mutableStateOf("emilyspass")
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val isLoading  = viewModel.isLoading.collectAsState()
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = {username = it},
            singleLine = true,
            placeholder = { Text(text = "Username")}
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password,
            onValueChange = {password = it},
            singleLine = true,
            placeholder = { Text(text = "Password")},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if(passwordVisible) {
                    painterResource(id = R.drawable.ic_visibility_24)
                } else {
                    painterResource(id = R.drawable.ic_visibility_off_24)
                }
                val description = if(passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description)
                }
            },

        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            viewModel.authenticate(username, password) {
                navController.navigate(Screen.ProductsListScreen.route) {
                    popUpTo(Screen.LoginScreen.route) {
                        inclusive = true
                    }
                }

        }}) {
            Text(text = "Login")
        }
    }
    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProductsListScreen (navController: NavController, viewModel: MainViewModel) {


   val productScreenState = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadNextProducts()
    }

    LazyColumn (modifier = Modifier.fillMaxSize()) {
        items (productScreenState.value.products.size) {index ->
            if(index >= productScreenState.value.products.size -1 &&
                !productScreenState.value.endReached && !productScreenState.value.isLoading) {
                viewModel.loadNextProducts()
            }
            productScreenState.value.products[index]?.let { ProductItem(product = it) }
        }

        item {
            if(productScreenState.value.isLoading) {
                Row (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ){
                    CircularProgressIndicator()
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "This is ProductsListScreen")
    }
}

@Composable
fun ProductItem(product: Product) {
Row (
    modifier = Modifier.fillMaxSize(),
    verticalAlignment = Alignment.CenterVertically
){
    GlideImage(
        modifier = Modifier.size(80.dp),
        imageModel = {
            product.thumbnail
        },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Fit,
            alignment = Alignment.Center,
            contentDescription = ""
        ),
        requestOptions = {
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
        },
        loading = {
            Box(modifier = Modifier.matchParentSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        failure = {
            Text(text = "image request failed")
        })
    Spacer(modifier = Modifier.width(40.dp))
}
}

@Composable
fun ProductScreen (navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "This is ProductScreen")
    }
}
/*
Які можливості надає бібліотека Retrofit у роботі з мережевими запитами?Які можливості надає бібліотека Retrofit у роботі з мережевими запитами?
Автоматичне перетворення JSON-відповідей у моделі даних

Які можливості надає Jetpack Compose у порівнянні із традиційним XML-підходом для створення користувацького інтерфейсу?
Jetpack Compose дозволяє описувати інтерфейс за допомогою коду на Kotlin, замість XML-розмітки

З чого потрібно починати створення Android додатку?
Встановлення Android Studio

Які переваги використання патерну MVVM у порівнянні з патерном MVC?
Ізоляція даних від відображення

Що таке життєвий цикл активності в Android, і чому важливо його розуміти при розробці додатків?
Послідовність станів, через які проходить активність, від створення до закриття


 */