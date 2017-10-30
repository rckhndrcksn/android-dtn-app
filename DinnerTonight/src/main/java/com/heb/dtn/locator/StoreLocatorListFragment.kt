//
// Created by Khuong Huynh on 11/28/16.
//

package com.heb.dtn.locator

import android.content.Context
import android.support.v4.content.ContextCompat
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

    fun switchToNearByList() {
        this.listHeaderTitle = getString(R.string.locations_hint)
        this.setData(arrayListOf<StoreItem>())
        this.handlePlaceHolderViews(false)
    }

    override fun backgroundColor(): Int = ContextCompat.getColor(this.activity, android.R.color.transparent)
    override fun actionType(): RecyclerList.ActionType = RecyclerList.ActionType.NO_ACTION
    override fun onCreateAdapter(data: List<StoreItem>): RecyclerListView.Adapter = StoreLocatorListAdapter(this.context, data, this.listHeaderTitle)
    override fun showPlaceholderViews(): Boolean = this.listView?.adapter?.itemCount == 0

    inner class StoreLocatorListAdapter(context: Context, val storeItems: List<StoreItem>, val headerTitle: String) : RecyclerListView.Adapter() {
        private val NUMBER_OF_SECTIONS = 2

        private val SEARCH_SECTION_INDEX = 0
        private val STORE_ITEMS_SECTION_INDEX = 1

        private val VIEW_TYPE_SEARCH_ITEM = 1
        private val VIEW_TYPE_HEADER = 2
        private val VIEW_TYPE_ITEM = 3

        override fun numberOfRowsInSection(section: Int): Int = when(section) {
            SEARCH_SECTION_INDEX -> 1
            STORE_ITEMS_SECTION_INDEX -> storeItems.size
            else -> 0
        }

        override fun onCreateView(parent: ViewGroup?, viewType: Int): View? {
            val inflater = LayoutInflater.from(this@StoreLocatorListFragment.context)

            return when(viewType) {
                VIEW_TYPE_ITEM -> inflater.inflate(R.layout.view_store_info, parent, false)
                VIEW_TYPE_SEARCH_ITEM -> inflater.inflate(R.layout.view_search, parent, false)
                VIEW_TYPE_HEADER -> inflater.inflate(R.layout.view_header_title, parent, false)
                else -> null
            }
        }

        override fun onBindView(view: View?, index: Index) {
            if (index.section == STORE_ITEMS_SECTION_INDEX) {
                val storeItem = this.storeItems[index.row]
                view?.title?.text = storeItem.name
                view?.address?.text = "${storeItem.address1} ${storeItem.address2}"
                view?.distance?.text = getString(R.string.store_distance_in_miles, storeItem.distanceToLocation)
                view?.setOnClickListener { this@StoreLocatorListFragment.itemListener?.onItemClicked(storeItem) }
            }
        }

        override fun onBindHeaderView(view: View, section: Int) {
            val standardView = view as TextView
            standardView.text = this.headerTitle
        }

        override fun getItemViewTypeAtIndex(index: Index): Int = when(index.section) {
            SEARCH_SECTION_INDEX -> VIEW_TYPE_SEARCH_ITEM
            STORE_ITEMS_SECTION_INDEX -> VIEW_TYPE_ITEM
            else -> super.getItemViewTypeAtIndex(index)
        }

        override fun getHeaderViewTypeInSection(section: Int): Int = when(section) {
            STORE_ITEMS_SECTION_INDEX -> VIEW_TYPE_HEADER
            else -> super.getHeaderViewTypeInSection(section)
        }

        override fun hasHeaderInSection(section: Int): Boolean = section == STORE_ITEMS_SECTION_INDEX

        override fun numberOfSections(): Int = NUMBER_OF_SECTIONS
    }
}
