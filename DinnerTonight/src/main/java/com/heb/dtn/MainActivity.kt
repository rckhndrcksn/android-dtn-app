package com.heb.dtn;

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.domain.isUp
import com.heb.dtn.service.manager.DefaultDinnerTonightServiceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        this.checkServerHealth()
    }

    private fun checkServerHealth() {
        val manager = DefaultDinnerTonightServiceManager(context = this)
        manager.serverInfoService().serverStatus()
                .then {
                    this.textView.text = if (it.isUp()) "Server is up!!" else "Server is down!!"
                    this.textView.setTextColor(Color.BLACK)
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }
                .catch {
                    this.textView.setTextColor(Color.RED)
                    when(it) {
                        is HTTPService.Error -> this.textView.text = "Server is dead. Long live the server."
                        else -> this.textView.text = "Oops, something went wrong."
                    }
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }
    }

}
