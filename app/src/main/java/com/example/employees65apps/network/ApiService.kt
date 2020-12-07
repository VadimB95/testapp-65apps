/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.employees65apps.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import timber.log.Timber

private const val BASE_URL = "https://gitlab.65apps.com/65gb/static/raw/master/"

/**
 * Retrofit service to fetch employees list
 */
interface StaffApiService {
    @GET("testTask.json")
    suspend fun getContainer(): NetworkStaffContainer

    companion object {
        /**
         * Create StaffApiService
         */
        fun create(): StaffApiService {
            /**
             * Use logging interceptor to obtain http requests
             */
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor { message ->
                Timber.tag("OkHttp").d(message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            /**
             * Build a Moshi object for Retrofit with Kotlin adapter for full Kotlin compatibility
             */
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            /**
             * Use the Retrofit builder to build a retrofit object using a Moshi converter and HttpLoggingInterceptor
             */
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .client(client)
                .build()
                .create(StaffApiService::class.java)
        }
    }
}