package com.prus.android.prus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Url
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.receiveDataButton).setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val result = JSONObject(
                        Retrofit
                            .Builder()
                            .baseUrl(
                                "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/"
                            )
                            .build()
                            .create(Api::class.java).getData(
                                "${withContext(Dispatchers.Main) {
                               findViewById<EditText>(R.id.enterDateView).text.toString() }
                                }/currencies/${
                                    withContext(Dispatchers.Main) {
                                        findViewById<EditText>(
                                            R.id.enterToCurrencyCodeView
                                        ).text.toString()
                                    }
                                }/${
                                    withContext(Dispatchers.Main) {
                                        findViewById<EditText>(
                                            R.id.enterFromCurrencyCodeView
                                        ).text.toString()
                                    }
                                }.json"
                            ).body()?.string().orEmpty()
                    )
                    withContext(Dispatchers.Main) {
                        findViewById<TextView>(R.id.receivedJsonView).text = result.toString()
                    }
                } catch (e: Exception) {
                    Log.d("TAG", "Api timeout")
                }
            }
        }
    }
}

interface Api {
    @GET
    suspend fun getData(@Url endpoint: String): Response<ResponseBody>
}