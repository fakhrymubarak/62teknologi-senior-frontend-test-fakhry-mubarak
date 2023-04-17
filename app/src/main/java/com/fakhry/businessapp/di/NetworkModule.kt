package com.fakhry.businessapp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fakhry.businessapp.BuildConfig
import com.fakhry.businessapp.core.network.NetworkState
import com.fakhry.businessapp.data.business.remote.BusinessApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = BuildConfig.DEBUG,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        val chuckInterceptor = ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(true)
            .build()

        httpLoggingInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chuckInterceptor)
            .addInterceptor(getAuthInterceptor())
            .build()
    }

    private fun getAuthInterceptor() = Interceptor { chain: Interceptor.Chain ->
        val oldRequest = chain.request()
        val newRequestBuilder = oldRequest.newBuilder()
        newRequestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.TOKEN_ACCESS}")
        chain.proceed(newRequestBuilder.build())
    }

    private fun retrofitBuilderV1(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    fun provideNetworkState(@ApplicationContext context: Context): NetworkState = NetworkState(context)

    @Provides
    fun provideBusinessApiService(client: OkHttpClient): BusinessApiService =
        retrofitBuilderV1(client).create(BusinessApiService::class.java)

}

