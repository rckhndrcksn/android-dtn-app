//
// Created by Khuong Huynh on 11/28/16.
//

package com.heb.dtn.locator

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.heb.dtn.R
import com.heb.dtn.foundation.widget.recyclerview.Index
import com.heb.dtn.foundation.widget.recyclerview.RecyclerListView
import com.heb.dtn.app.RecyclerList
import com.heb.dtn.app.RecyclerListViewFragment
import com.heb.dtn.locator.domain.StoreItem
import kotlinx.android.synthetic.main.view_store_info.view.*

class StoreLocatorListFragment : RecyclerListViewFragment<List<StoreItem>>() {

    interface ListItemListener {
        fun onItemClicked(storeItem: StoreItem)
    }

    var itemListener: ListItemListener? = null
    private lateinit var listHeaderTitle: String


    override fun onInitView() {
        this.listView?.setItemDividerVisible(false)
        this.switchToNearByList()
    }

    fun switchToSearchList() {
        this.listHeaderTitle = this.getString(R.string.rx_locator_search_result_header_filtered)
        this.handlePlaceHolderViews(true)
    }

    fun switchToNearByList() {
        this.listHeaderTitle = this.getString(R.string.rx_locator_search_result_header_nearby)
        this.setData(arrayListOf<StoreItem>())
        this.handlePlaceHolderViews(false)
    }

    override fun backgroundColor(): Int = ContextCompat.getColor(this.activity, R.color.defaultBackground)
    override fun actionType(): RecyclerList.ActionType = RecyclerList.ActionType.NO_ACTION
    override fun onCreateAdapter(data: List<StoreItem>): RecyclerListView.Adapter = StoreLocatorListAdapter(this.context, data, this.listHeaderTitle)
    override fun showPlaceholderViews(): Boolean = this.listView?.adapter?.itemCount == 0

    inner class StoreLocatorListAdapter(context: Context, val storeItems: List<StoreItem>, val headerTitle: String) : RecyclerListView.Adapter() {

        private val VIEW_TYPE_HEADER = 1
        private val VIEW_TYPE_ITEM = 2

        override fun numberOfRowsInSection(section: Int): Int = this.storeItems.size

        override fun onCreateView(parent: ViewGroup?, viewType: Int): View? {

            val inflater = LayoutInflater.from(this@StoreLocatorListFragment.context)

            val view = when(viewType) {
                VIEW_TYPE_ITEM -> inflater.inflate(R.layout.view_store_info, null)
                VIEW_TYPE_HEADER -> inflater.inflate(R.layout.view_header_title, null)
                else -> null
            }

            if (view != null) {
                view.layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            }

            return view
        }

        override fun onBindView(view: View?, index: Index) {
            val storeItem = this.storeItems[index.row]

            view?.title?.text = storeItem.name
            view?.address1?.text = storeItem.address1
            view?.address2?.text = storeItem.address2
            view?.distance?.text = "${storeItem.distanceToLocation} mi."

            view?.setOnClickListener { this@StoreLocatorListFragment.itemListener?.onItemClicked(storeItem) }
        }

        override fun onBindHeaderView(view: View, section: Int) {
            val standardView = view as TextView

            standardView.text = this.headerTitle
        }

        override fun getItemViewTypeAtIndex(index: Index): Int = VIEW_TYPE_ITEM

        override fun getHeaderViewTypeInSection(section: Int): Int = VIEW_TYPE_HEADER

        override fun hasHeaderInSection(section: Int): Boolean = true
    }
}
