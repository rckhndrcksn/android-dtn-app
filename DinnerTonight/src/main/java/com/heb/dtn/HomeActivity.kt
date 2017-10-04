package com.heb.dtn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.promise.android.thenp
import com.heb.dtn.foundation.service.JSONEncoder
import com.heb.dtn.locator.StoreLocatorActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.nio.charset.Charset

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu?.findItem(R.id.find) == null) {
            menu?.clear()
            menuInflater?.inflate(R.menu.home_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.find -> {
            this.startActivity(Intent(this, StoreLocatorActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
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
                    val sb = StringBuilder()
                    sb.append(this.products.text).append("\n")

                    if (it.items == null) {
                        sb.append("No stores found.")
                    } else {
                        it.items?.forEach {
                            val str = JSONEncoder().encode(it)?.toString(Charset.defaultCharset()) ?: ""
                            sb.append(str).append("\n\n\n")
                        }
                    }
                    this.products.text = sb.toString()
                }
                .catch {
                    this.products.setTextColor(Color.RED)
                    this.products.text = "Something went wrong"
                }
    }

}
