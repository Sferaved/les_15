package com.myapp.les_15

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.les_15.database.Product
import com.myapp.les_15.database.ProductDao
import com.myapp.les_15.database.ProductResponse
import com.myapp.les_15.network.AuthRequest
import com.myapp.les_15.network.AuthResponse
import com.myapp.les_15.network.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val authService: AuthService,
    private val encryptedPreferences: EncryptedPreferences,
    private val myServiceInterceptor: MyServiceInterceptor,
    private val productsDao: ProductDao
    ): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _authResponse: MutableStateFlow<AuthResponse?> = MutableStateFlow(null)
    val authResponse get() = _authResponse.asStateFlow()

    private val _state = MutableStateFlow(ProductScreenState())
    val state get() = _state.asStateFlow()

    init {
        if(encryptedPreferences.getJwtToken().isNotEmpty()) {
            myServiceInterceptor.setSessionToken(encryptedPreferences.getJwtToken())
        }
    }
    fun authenticate(
        username: String,
        password: String,
        onSuccess: (()->Unit)? = null
    ) {
        viewModelScope.launch {
            _isLoading.emit(true)
            authService.authenticate(AuthRequest(username, password)).onSuccess {
                _isLoading.emit(false)
                _authResponse.emit(it)
                it.token?.let {
                   token ->encryptedPreferences.saveJwtToken(token)
                }
                onSuccess?.invoke()
            }.onFailure {
                _isLoading.emit(false)
            }
        }
    }

    fun navigateToProducts(onSuccess: (() -> Unit)? = null) {
        if (encryptedPreferences.getJwtToken().isNotEmpty()) {
            onSuccess?.invoke()
        }
    }

    private fun insertDatabaseProducts(products: List<Product?>?) {
        viewModelScope.launch {
            products?.filterNotNull()?.let { nonNullProducts ->
                productsDao.insertAll(nonNullProducts)
            }
        }
    }


    private suspend fun getProducts(
        pageSize: Int,
        page: Int): Result<ProductResponse> {
        return authService.getProducts(
            page,
            pageSize
        )
    }

    private fun getDatabase() {
        viewModelScope.launch {
            val products = productsDao.getAllItems()
            _state.value = _state.value.copy(products = products, endReached = true)
        }
    }

    private fun paginator()  = DefaultPaginator(
         initialKey = _state.value.page,
         onLoadUpdate = {
             _state.value = _state.value.copy(isLoading = it)
         },
         onRequest = {nextPage ->
             getProducts(nextPage, 10)
         },
        getNextKey = {
            _state.value.page + 10
        },
        onError = {
            if(it?.localizedMessage == "No internet connection") {
                getDatabase()
            }
        },
        onSuccess = { items, newKey ->
            insertDatabaseProducts(items.products)
            items.products?.let{
                _state.value = _state.value.copy(
                     products = _state.value.products + items.products,
                     page = newKey,
                     endReached = items.products.isEmpty()
                )
            }

        }
    )
    fun loadNextProducts() {
        viewModelScope.launch {
            paginator().loadNextItems()
        }
    }
}

data class ProductScreenState(
    val isLoading: Boolean = false,
    val products: List<Product?> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)