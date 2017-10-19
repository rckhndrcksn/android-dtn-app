package com.heb.dtn

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import com.heb.dtn.app.AppProxy
import com.heb.dtn.locator.StoreLocatorActivity
import com.heb.dtn.utils.UIControlDelegate
import com.heb.dtn.utils.UIControlDelegation
import com.inmotionsoftware.imsflow.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : FlowActivity(),  UIControlDelegate by UIControlDelegation()  {

    private var flow: FlowPromise<Unit>? = null

    sealed class Section {
        class Home: Section()
        class Browse: Section()
        class Order: Section()
        class Profile: Section()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        this.controlActivity = this
        this.progressBar = this.findViewById(R.id.spinner) as ProgressBar
        this.fragmentContainerId = R.id.fragmentContainer

        this.homeButtom.setOnClickListener { }
        this.browseButton.setOnClickListener {  }
        this.ordersButton.setOnClickListener {  }
        this.profileButton.setOnClickListener {  }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu?.findItem(R.id.find) == null) {
            menu?.clear()
            menuInflater?.inflate(R.menu.home_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onStartFlow() {
        if (this.flow != null) return
        this.flow = AppProxy.proxy.flow.main(context = this, fragmentContainerView = R.id.fragmentContainer)
        this.flow!!.back { this.finish() }
                .cancel { this.finish() }
                .complete {  }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.find -> {
            this.startActivity(Intent(this, StoreLocatorActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
