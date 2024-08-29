@file:Suppress("DEPRECATION")

package com.myapp.les_15

import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.myapp.les_15.database.AppDatabase
import org.koin.dsl.module
import com.myapp.les_15.network.AuthService
import com.myapp.les_15.network.ResultCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://dummyjson.com/"
val appModule = module {
    // Зареєструйте AppDatabase
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "my-database"
        ).build()
    }

    // Зареєструйте ProductDao
    single { get<AppDatabase>().productDao() }

    // Зареєструйте інші компоненти
    viewModelOf(::MainViewModel)
    single { MyServiceInterceptor() }
    single { createWebService(AuthService::class.java, BASE_URL, get()) }
    single { MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC) }
    single {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        EncryptedSharedPreferences.create(
            "encrypted_preferences",
            mainKeyAlias,
            androidContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }
    single { EncryptedPreferences(get()) }
}


inline fun <reified T> createWebService(
    service: Class<T>,
    url: String,
    myServiceInterceptor: MyServiceInterceptor
): T? {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG)
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor(myServiceInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(ResultCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(service)
}

class MyServiceInterceptor : Interceptor {
    private var sessionToken: String? = null
    fun setSessionToken(sessionToken: String?) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()

        // Check if request should bypass authentication
        if (request.header("No-Authentication") == null) {
            if (sessionToken.isNullOrEmpty()) {
                // Handle the case where the session token is null
                // For example, redirect to login screen or refresh token
                throw RuntimeException("Session token should be defined for auth APIs")
            } else {
                requestBuilder.addHeader("Authorization", "Bearer $sessionToken")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

}
