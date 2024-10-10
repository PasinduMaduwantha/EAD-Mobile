package com.example.ead_kotlin.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

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
    suspend fun createOrder(@Header("Authorization") authToken: String, @Body createOrderDto: CreateOrderDto): Response<ApiResponse<OrderDto>>

    @GET("api/orders/get-customer-orders")
    suspend fun getCustomerOrders(@Header("Authorization") authToken: String): Response<ApiResponse<List<GetOrderDto>>>

    @GET("api/products/get-products-by-category")
    suspend fun getProductsByCategory(): Response<GroupedProductsResponse>

    @PUT("api/orders/update-order-status/{orderId}")
    suspend fun cancelOrder(@Header("Authorization") authToken: String, @Path("orderId") id: String?, @Body orderDto: OrderDto) : Response<ApiResponse<OrderDto>>

    @GET("api/users/profile")
    suspend fun getUserProfile(@Header("Authorization") authToken: String): Response<ApiResponse<UserProfileDto>>

    @PUT("api/users/profile")
    suspend fun updateUserProfile(@Header("Authorization") authToken: String, @Body userUpdateDto: UserUpdateDto): Response<ApiResponse<UserProfileDto>>

    @PUT("/api/users/update-status/{userId}")
    suspend fun updateUserProfileStatus(@Header("Authorization") authToken: String, @Path("userId") id: String?, @Body userStatusUpdateDto: UserStatusUpdateDto): Response<ApiResponse<UserProfileDto>>

    @POST("api/vendor-ratings/create-vendor-rating")
    suspend fun createVendorRating(@Header("Authorization") authToken: String, @Body vendorRatingCreateDto: VendorRatingCreateDto): Response<ApiResponse<VendorRatingDto>>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:5076/" // This is the local machine's localhost for Android emulator

        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

//            val client = OkHttpClient.Builder()
//                .addInterceptor(logger)
//                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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

data class UserStatusUpdateDto (val status : String )

data class LoginData(
    val Token: String,
    val User: UserDto
)

data class GroupedProductsDto(
    val categories: Map<String, List<ProductDto>>
)

data class GroupedProductsResponse(
    val IsSuccessful: Boolean,
    val TimeStamp: String,
    val Message: String,
    val Data: GroupedProductsDto
)


data class LoginDto(val email: String, val password: String)
data class LoginResponse(
    val IsSuccessful: Boolean,
    val TimeStamp: String,
    val Message: String,
    val Data: LoginData
)
data class UserDto(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val age: Int,
    val role: String? = null,
    val Status: String? = null
)
data class UserProfileDto(
    val id: String? = null,
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val Age: Int,
    val Role: String? = null,
    val Status: String? = null
)
data class UserUpdateDto(val firstName: String, val lastName: String, val email: String, val age: Int, val status: String)
//data class ProductDto(
//    val id: String,
//    val name: String,
//    val description: String,
//    val price: Double,
//    val vendorId: String
//)
data class CreateOrderDto(val orderItems: List<OrderItemDto>)
data class OrderItemDto(val productId: String, val quantity: Int)
data class OrderDto(
    val Id: String,
    val CustomerId: String,
    val OrderItems: List<GetOrderItemDto>,
    val TotalAmount: Double,
    val OrderStatus: String
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
    val Id: String,
    val VendorId: String,
    val Name: String,
    val Description: String,
    val ImageUrl: String,
    val Price: Double,
    val Qty: Int,
    val CategoryId: String,
    val IsActive: Boolean,
    val CreatedAt: String,
    val UpdatedAt: String,
    val Category: Category,
    val Vendor: VendorDto
)

data class Category(
    val Id: String,
    val Name: String,
    val Description: String,
    val IsActive: Boolean,
    val CreatedAt: String,
    val UpdatedAt: String
)
data class ApiResponse<T>(
    val IsSuccessful: Boolean,
    val TimeStamp: String,
    val Message: String,
    val Data: T
)

data class GetOrderDto(
    val Id: String,
    val CustomerId: String,
    val OrderStatus: String,
    val TotalAmount: Double,
    val CreatedAt: String,
    val UpdatedAt: String,
    val OrderItems: List<GetOrderItemDto>,
    val Customer: CustomerDto,
    val Status: String
)

data class GetOrderItemDto(
    val Id: String,
    val OrderId: String,
    val ProductId: String,
    val Quantity: Int,
    val Price: Double,
    val VendorId: String,
    val Status: String,
    val Product: OrderProductDto,
    val Vendor: VendorDto,
)

data class CustomerDto(
    val id: String?,
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val Age: Int,
    val Password: String?,
    val Role: String,
    val Status: String?
)

data class VendorDto(
    val id: String?,
    val FirstName: String,
    val LastName: String,
    val Email: String,
    val Age: Int,
    val Password: String?,
    val Role: String,
    val Status: String?
)

data class OrderProductDto(
    val Id: String,
    val VendorId: String?,
    val Name: String,
    val Description: String,
    val ImageUrl: String,
    val Price: Double,
    val Qty: Int,
    val CategoryId: String?,
    val IsActive: Boolean,
    val CreatedAt: String,
    val UpdatedAt: String,
    val Category: CategoryDto?,
    val Vendor: VendorDto?
)

data class CategoryDto(
    val Id: String,
    val Name: String,
    val Description: String,
    val IsActive: Boolean,
    val CreatedAt: String,
    val UpdatedAt: String
)
