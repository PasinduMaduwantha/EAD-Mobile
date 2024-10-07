package com.example.ead_kotlin.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body loginDto: LoginDto): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body userDto: UserDto): Response<UserDto>


    @GET("api/products/get-all-products")
    suspend fun getAllProducts(): Response<ApiResponse<List<ProductDto>>>

    @GET("api/products/get-one-product/{productId}")
    suspend fun getProductById(@Path("productId") id: String): Response<ApiResponse<ProductDto>>


    @POST("api/orders/create-order")
    suspend fun createOrder(@Body createOrderDto: CreateOrderDto): Response<OrderDto>

    @GET("api/orders/get-customer-orders")
    suspend fun getCustomerOrders(): Response<List<OrderDto>>

    @GET("api/users/profile")
    suspend fun getUserProfile(): Response<UserDto>

    @PUT("api/users/profile")
    suspend fun updateUserProfile(@Body userUpdateDto: UserUpdateDto): Response<UserDto>

    @POST("api/vendor-ratings/create-vendor-rating")
    suspend fun createVendorRating(@Body vendorRatingCreateDto: VendorRatingCreateDto): Response<VendorRatingDto>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:5076/" // This is the local machine's localhost for Android emulator

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

data class LoginDto(val email: String, val password: String)
data class LoginResponse(val token: String, val user: UserDto)
data class UserDto(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val age: Int,
    val role: String? = null,
    val status: String? = null
)
data class UserUpdateDto(val firstName: String, val lastName: String, val email: String, val age: Int)
//data class ProductDto(
//    val id: String,
//    val name: String,
//    val description: String,
//    val price: Double,
//    val vendorId: String
//)
data class CreateOrderDto(val items: List<OrderItemDto>)
data class OrderItemDto(val productId: String, val quantity: Int)
data class OrderDto(
    val id: String,
    val customerId: String,
    val items: List<OrderItemDto>,
    val totalAmount: Double,
    val status: String
)
data class VendorRatingCreateDto(val vendorId: String, val rating: Int, val comment: String)
data class VendorRatingDto(
    val id: String,
    val vendorId: String,
    val customerId: String,
    val rating: Int,
    val comment: String
)
data class ProductDto(
    val id: String,
    val vendorId: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val qty: Int,
    val categoryId: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val category: Category
)

data class Category(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)
data class ApiResponse<T>(
    val isSuccessful: Boolean,
    val timeStamp: String,
    val message: String,
    val data: T
)
