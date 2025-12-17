package com.example.floreriaapp.repository

import com.example.floreriaapp.Flor
import com.example.floreriaapp.database.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosRepository {
    private val apiService = RetrofitClient.instance

    fun obtenerFlores(onResult: (List<Flor>?, Throwable?) -> Unit) {
        apiService.obtenerFlores().enqueue(object : Callback<List<Flor>> {
            override fun onResponse(call: Call<List<Flor>>, response: Response<List<Flor>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, Exception("Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Flor>>, t: Throwable) {
                onResult(null, t)
            }
        })
    }
}
