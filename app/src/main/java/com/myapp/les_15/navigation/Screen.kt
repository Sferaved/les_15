package com.myapp.les_15.navigation

const val PRODUCTS_ROUTE = "products"
const val LOGIN_ROUTE = "login"
const val ROOT_ROUTE = "root"

sealed class Screen (val route: String) {
    object LoginScreen : Screen("login_screen")
    object ProductsListScreen : Screen("products_list_screen")
    object ProductScreen : Screen("product_screen")
}