package org.brainless.telyucreative.data.remote.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.brainless.telyucreative.data.model.Category
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/indrasz/Telyu-Creative/static-api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface CategoryApiService {
    @GET("static-api.JSON")
    suspend fun getCategory(): List<Category>
}
object CategoryApi {
    val service: CategoryApiService by lazy {
        retrofit.create(CategoryApiService::class.java)
    }
    fun getCategoryUrl(nama: String): String {
        return "$BASE_URL$nama.png"
    }
}
enum class ApiStatus { LOADING, SUCCESS, FAILED }