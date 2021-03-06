/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

package com.bubelov.coins.api.coins

import com.bubelov.coins.model.Place
import com.bubelov.coins.util.Json

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinsApi {
    @POST("users")
    fun createUser(
        @Json @Body args: CreateUserArgs
    ): Call<AuthResponse>

    @POST("auth/email")
    fun authWithEmail(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<AuthResponse>

    @POST("auth/google-token")
    fun authWithGoogle(
        @Header("token") token: String
    ): Call<AuthResponse>

    @GET("places")
    fun getPlaces(
        @Query("since") since: String,
        @Query("limit") limit: Int
    ): Call<List<Place>>

    @POST("places")
    fun addPlace(
        @Header("session") session: String,
        @Json @Body args: AddPlaceArgs
    ): Call<Place>

    @PATCH("places/{id}")
    fun updatePlace(
        @Path("id") id: Long,
        @Header("session") session: String,
        @Json @Body args: UpdatePlaceArgs
    ): Call<Place>
}