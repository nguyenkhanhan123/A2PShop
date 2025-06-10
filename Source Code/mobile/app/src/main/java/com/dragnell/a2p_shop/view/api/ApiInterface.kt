package com.dragnell.a2p_shop.view.api

import com.dragnell.a2p_shop.model.Account
import com.dragnell.a2p_shop.model.Category
import com.dragnell.a2p_shop.model.req_api.CategoryAddReq
import com.dragnell.a2p_shop.model.Product
import com.dragnell.a2p_shop.model.req_api.AccountAddReq
import com.dragnell.a2p_shop.model.req_api.AccountFixReq
import com.dragnell.a2p_shop.model.req_api.AdminLoginReq
import com.dragnell.a2p_shop.model.req_api.CartDeleteReq
import com.dragnell.a2p_shop.model.req_api.CartFixReq
import com.dragnell.a2p_shop.model.req_api.CategoryDeleteReq
import com.dragnell.a2p_shop.model.req_api.CategoryFixReq
import com.dragnell.a2p_shop.model.req_api.ClientCheckOutOrderReq
import com.dragnell.a2p_shop.model.req_api.ClientLoginReq
import com.dragnell.a2p_shop.model.req_api.ClientSignUpReq
import com.dragnell.a2p_shop.model.req_api.PostOrderReq
import com.dragnell.a2p_shop.model.req_api.ProductAddReq
import com.dragnell.a2p_shop.model.req_api.RoleAddReq
import com.dragnell.a2p_shop.model.req_api.RoleFixReq
import com.dragnell.a2p_shop.model.res_api.AccountInfoRes
import com.dragnell.a2p_shop.model.res_api.AccountsFixRes
import com.dragnell.a2p_shop.model.res_api.AdminLoginRes
import com.dragnell.a2p_shop.model.res_api.CartAddRes
import com.dragnell.a2p_shop.model.res_api.CartDeleteRes
import com.dragnell.a2p_shop.model.res_api.CartFixRes
import com.dragnell.a2p_shop.model.res_api.CartRes
import com.dragnell.a2p_shop.model.res_api.CategoryAddRes
import com.dragnell.a2p_shop.model.res_api.CategoryDeleteRes
import com.dragnell.a2p_shop.model.res_api.CategoryFixRes
import com.dragnell.a2p_shop.model.res_api.CategorySlugRes
import com.dragnell.a2p_shop.model.res_api.ClientLogOutRes
import com.dragnell.a2p_shop.model.res_api.ClientLoginRes
import com.dragnell.a2p_shop.model.res_api.ClientSignUpRes
import com.dragnell.a2p_shop.model.res_api.GetOrderAdminRes
import com.dragnell.a2p_shop.model.res_api.GetOrderClientRes
import com.dragnell.a2p_shop.model.res_api.PostOrderRes
import com.dragnell.a2p_shop.model.res_api.ProductAddRes
import com.dragnell.a2p_shop.model.res_api.ProductRes
import com.dragnell.a2p_shop.model.res_api.RevenueRes
import com.dragnell.a2p_shop.model.res_api.RoleFixRes
import com.dragnell.a2p_shop.model.res_api.RoleRes
import com.dragnell.a2p_shop.model.res_api.SearchRes
import com.dragnell.a2p_shop.model.res_api.UserInfoRes
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {
    @POST("auth/register")
    @Headers("Content-Type: application/json")
    fun clientSignUp(
        @Body req: ClientSignUpReq
    ): Call<ClientSignUpRes>

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun clientLogin1(
        @Body req: ClientLoginReq
    ): Call<ClientLoginRes>

    @POST("admin/auth/login")
    @Headers("Content-Type: application/json")
    fun adminLogin(
        @Body req: AdminLoginReq
    ): Call<AdminLoginRes>

    @POST("auth/login")
    @Headers("Content-Type: application/json")
    fun clientLogin2(
        @Body req: ClientLoginReq, @Header("Cookie") cookie: String
    ): Call<ClientLoginRes>

    @GET("search")
    @Headers("Content-Type: application/json")
    fun searchProducts(@Query("keyword") keyword: String): Call<SearchRes>

    @GET("cart")
    @Headers("Content-Type: application/json")
    fun getCart(@Header("Authorization") token: String, @Header("Cookie") cookie: String): Call<CartRes>

    @GET("user/info")
    @Headers("Content-Type: application/json")
    fun getUserInfo(@Header("Authorization") token: String): Call<UserInfoRes>

    @POST("auth/logout")
    @Headers("Content-Type: application/json")
    fun clientLogOut(@Header("Authorization") token: String): Call<ClientLogOutRes>

    @GET("products/detail/{slug}")
    @Headers("Content-Type: application/json")
    fun getProductBySlug(@Path("slug") slug: String): Call<Product>

    @POST("cart/add")
    @Headers("Content-Type: application/json")
    fun cartAdd(@Body req: CartFixReq, @Header("Authorization") token: String): Call<CartAddRes>

    @HTTP(method = "DELETE", path = "cart/delete", hasBody = true)
    @Headers("Content-Type: application/json")
    fun cartDelete(@Body req: CartDeleteReq, @Header("Authorization") token: String): Call<CartDeleteRes>

    @PATCH("cart/update-quantity")
    @Headers("Content-Type: application/json")
    fun cartUpdateQuantity(@Body req: CartFixReq, @Header("Authorization") token: String): Call<CartFixRes>

    @POST("checkout/oder")
    @Headers("Content-Type: application/json")
    fun clientCheckOutOrder(@Body req: ClientCheckOutOrderReq, @Header("Authorization") token: String, @Header("Cookie") cookie: String): Call<CartFixRes>

    @GET("admin/role")
    @Headers("Content-Type: application/json")
    fun getRole(@Header("Authorization") token: String): Call<RoleRes>

    @GET("admin/accounts")
    @Headers("Content-Type: application/json")
    fun getAccounts(@Header("Authorization") token: String): Call<List<Account>>

    @POST("admin/role/create")
    @Headers("Content-Type: application/json")
    fun adminAddRole(@Body req: RoleAddReq, @Header("Authorization") token: String): Call<RoleFixRes>

    @PATCH("admin/role/edit")
    @Headers("Content-Type: application/json")
    fun adminFixRole(@Body req: RoleFixReq, @Header("Authorization") token: String): Call<RoleFixRes>

    @POST("admin/accounts/create")
    @Headers("Content-Type: application/json")
    fun adminAddAccount(@Body req: AccountAddReq, @Header("Authorization") token: String): Call<AccountsFixRes>

    @PATCH("admin/accounts/edit")
    @Headers("Content-Type: application/json")
    fun adminFixAccount(@Body req: AccountFixReq, @Header("Authorization") token: String): Call<AccountsFixRes>

    @GET("admin/myaccount")
    @Headers("Content-Type: application/json")
    fun getMyAccount(@Header("Authorization") token: String): Call<AccountInfoRes>

    @GET("admin/auth/logout")
    @Headers("Content-Type: application/json")
    fun adminLogOut(@Header("Authorization") token: String): Call<ClientLogOutRes>

    @GET("admin/product-category")
    @Headers("Content-Type: application/json")
    fun getCategories(@Header("Authorization") token: String): Call<List<Category>>

    @POST("admin/product-category/createPost")
    @Headers("Content-Type: application/json")
    fun addCategories(@Body req: CategoryAddReq, @Header("Authorization") token: String): Call<CategoryAddRes>

    @HTTP(method = "DELETE", path = "admin/product-category/deleted", hasBody = true)
    @Headers("Content-Type: application/json")
    fun deleteCategories(@Body req: CategoryDeleteReq, @Header("Authorization") token: String): Call<CategoryDeleteRes>

    @PATCH("admin/product-category/edit")
    @Headers("Content-Type: application/json")
    fun fixCategories(@Body req: CategoryFixReq, @Header("Authorization") token: String): Call<CategoryFixRes>

    @POST("admin/products/createPost")
    @Headers("Content-Type: application/json")
    fun addProducts(@Body req: ProductAddReq, @Header("Authorization") token: String): Call<ProductAddRes>

    @POST("order/post")
    @Headers("Content-Type: application/json")
    fun orderPost(@Body req: PostOrderReq, @Header("Authorization") token: String): Call<PostOrderRes>

    @GET("order/view")
    @Headers("Content-Type: application/json")
    fun getOrderClient(@Header("Authorization") token: String): Call<GetOrderClientRes>

    @GET("admin/order")
    @Headers("Content-Type: application/json")
    fun getOrderAdmin(@Header("Authorization") token: String): Call<GetOrderAdminRes>

    @GET("admin/revenue")
    @Headers("Content-Type: application/json")
    fun getRevenueAdmin(@Header("Authorization") token: String): Call<RevenueRes>

    @GET("admin/products")
    @Headers("Content-Type: application/json")
    fun getProductBySlugCategoryAdmin(@Query("slugcategory") slug: String,@Header("Authorization") token: String): Call<CategorySlugRes>

    @GET("products")
    @Headers("Content-Type: application/json")
    fun getProduct(@Header("Authorization") token: String): Call<ProductRes>

}