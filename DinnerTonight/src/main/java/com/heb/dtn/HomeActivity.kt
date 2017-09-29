package com.heb.dtn

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.heb.dtn.service.manager.DefaultDinnerTonightServiceManager
import kotlinx.android.synthetic.main.activity_home.*

import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()

        val manager = DefaultDinnerTonightServiceManager(context = this)
        val result1 = manager.productsService().findProducts()
        if (result1 == null || result1.items?.isEmpty() == true) {
            this.products.setTextColor(Color.RED)
            this.products.text = "Something went wrong"
        } else {
            this.products.setTextColor(Color.BLACK)
            val sb = StringBuilder("")
            for (i in result1.items?.indices!!) {
                sb.append(result1.items!![i].toString())
                sb.append("\n")
            }
            this.products.text = sb.toString()
        }


        val result2 = manager.storeService().locateStore(lat = 29.594423, lon = -98.456813, radius = 5)
        if (result2 == null || result2.items?.isEmpty() == true) {
            this.stores.setTextColor(Color.RED)
            this.stores.text = "Something went wrong"
        } else {
            this.stores.setTextColor(Color.BLACK)
            val sb = StringBuilder("")
            for (i in result2.items?.indices!!) {
                sb.append(result2.items!![i].toString())
                sb.append("\n")
            }
            this.stores.text = sb.toString()
        }
    }
}
