package com.heb.dtn

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.foundation.service.JSONEncoder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.nio.charset.Charset

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()

        AppProxy.proxy.serviceManager().productService()
                .getProducts()
                .then {
                    if (it.items == null) throw IllegalArgumentException()

                    this.products.setTextColor(Color.BLACK)
                    val sb = StringBuilder("")
                    it.items?.forEach {
                        val str = JSONEncoder().encode(it)?.toString(Charset.defaultCharset()) ?: ""
                        sb.append(str).append("\n\n\n")
                    }
                    this.products.text = sb.toString()
                }
                .thenp {
                    AppProxy.proxy.serviceManager().storeService().getStores(latitude = 29.594423, longitude = -98.456813, radius = 5.0)
                }
                .then {
                    if (it.items == null) throw IllegalArgumentException()
                    val sb = StringBuilder()
                    sb.append(this.products.text).append("\n")

                    it.items?.forEach {
                        val str = JSONEncoder().encode(it)?.toString(Charset.defaultCharset()) ?: ""
                        sb.append(str).append("\n\n\n")
                    }
                    this.products.text = sb.toString()
                }
                .catch {
                    this.products.setTextColor(Color.RED)
                    this.products.text = "Something went wrong"
                }
    }

}
