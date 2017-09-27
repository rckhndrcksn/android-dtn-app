package com.heb.dtn;

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.heb.dtn.app.AppProxy
import com.heb.dtn.foundation.promise.android.always
import com.heb.dtn.foundation.promise.android.catch
import com.heb.dtn.foundation.promise.android.then
import com.heb.dtn.foundation.service.HTTPService
import com.heb.dtn.service.domain.isUp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)
        this.checkServerHealth()
    }

    private fun checkServerHealth() {
        AppProxy.proxy.serviceManager().serverInfoService()
                .serverStatus()
                .then {
                    this.textView.text = if (it.isUp()) "Server is up!!" else "Server is down!!"
                    this.textView.setTextColor(Color.BLACK)
                }
                .catch {
                    this.textView.setTextColor(Color.RED)
                    when(it) {
                        is HTTPService.Error -> this.textView.text = "Server is dead. Long live the server."
                        else -> this.textView.text = "Oops, something went wrong."
                    }
                }
                .always {
                    this.startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }
    }

}
