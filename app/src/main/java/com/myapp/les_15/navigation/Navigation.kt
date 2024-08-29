package com.myapp.les_15.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.myapp.les_15.LoginScreen
import com.myapp.les_15.MainViewModel
import com.myapp.les_15.ProductScreen
import com.myapp.les_15.ProductsListScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Navigation (viewModel: MainViewModel) {
    val navController = rememberNavController()

    Scaffold (modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = LOGIN_ROUTE, route = ROOT_ROUTE) {
            navigation(startDestination = Screen.LoginScreen.route, route = LOGIN_ROUTE) {
                composable(route =Screen.LoginScreen.route) {
                    LoginScreen(navController = navController, viewModel = viewModel)
                }
            }
            navigation(startDestination = Screen.ProductsListScreen.route, route = PRODUCTS_ROUTE) {
                composable(route =Screen.ProductsListScreen.route) {
                    ProductsListScreen(navController = navController, viewModel = viewModel)
                }
//                `composable(route =Screen.ProductScreen.route) {
//                    ProductScreen(navController)
//                }`
            }
        }
    }
}