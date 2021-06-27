package com.rskot.locweathermvvm.network

import com.rskot.locweathermvvm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor class adds app id in all request as query parameter
 */
class QueryParameterAddInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url().newBuilder()
            .addQueryParameter("appid", BuildConfig.APP_ID)
            .build()

        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer token")
            .url(url)
            .build()

        return chain.proceed(request)
    }
}