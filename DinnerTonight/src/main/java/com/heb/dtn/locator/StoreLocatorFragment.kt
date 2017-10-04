package com.heb.dtn.locator

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.heb.dtn.R

abstract class StoreLocatorFragment<ARGS, RETURN> : LocatorFragment<ARGS, RETURN>() {
/*
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (menu?.findItem(R.id.cancel) == null) {
            inflater?.inflate(R.menu.cancel_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.cancel -> { this.cancel() }
        }

        return super.onOptionsItemSelected(item)
    }
*/
}