package com.heb.dtn.flow.fullfillment

import android.support.v7.app.AppCompatActivity
import com.heb.dtn.flow.core.BaseFlowController
import com.heb.dtn.locator.*
import com.heb.dtn.locator.domain.StoreItem
import com.heb.dtn.service.domain.profile.Profile
import com.inmotionsoftware.imsflow.*
import com.heb.dtn.foundation.promise.android.*

class LocateStoreFlowController(private val context: AppCompatActivity, fragmentContainerView: Int, option: StoreLocatorOption)
    : BaseFlowController<LocateStoreFlowController.State, Unit, StoreItem>(context = context, fragmentContainerView = fragmentContainerView) {

    private data class SavePreferredStoreParams(val retry: Boolean, val store: StoreItem, val profile: Profile)

    private val mapListFragment: StoreLocatorMapListFragment by lazy { StoreLocatorMapListFragment() }
    private val storeDetailFragment: StoreLocatorStoreDetailFragment by lazy { StoreLocatorStoreDetailFragment() }

    private val option: StoreLocatorOption
    private var selectedStore: StoreLocatorSelectedStore? = null

    enum class State : FlowState {
        BEGIN
        , FIND_STORE
        , SELECT_STORE
//        , SAVE_PREFERRED_STORE
//        , AUTHENTICATE
        , DONE
        , CANCEL
        , BACK
        , FAIL
    }

    init {
        this.initialize()
        this.option = option
    }

    override fun registerStates(states: Builder<State>) {
        states
                .initialState(state = State.BEGIN)
                .add(from = State.BEGIN, to = State.FIND_STORE)
                .add(from = State.FIND_STORE, to = State.BACK)
                .add(from = State.FIND_STORE, to = State.SELECT_STORE)

                .add(from = State.SELECT_STORE, to = State.FIND_STORE)
//                .add(from = State.SELECT_STORE, to = State.SAVE_PREFERRED_STORE)
                .add(from = State.SELECT_STORE, to = State.DONE)

//                .add(from = State.SAVE_PREFERRED_STORE, to = State.SELECT_STORE)
//                .add(from = State.SAVE_PREFERRED_STORE, to = State.DONE)
//                .add(from = State.SAVE_PREFERRED_STORE, to = State.AUTHENTICATE)

//                .add(from = State.AUTHENTICATE, to = State.SAVE_PREFERRED_STORE)
//                .add(from = State.AUTHENTICATE, to = State.SELECT_STORE)

                .addFromAny(to = State.CANCEL)
                .addFromAny(to = State.FAIL)
    }

    override fun registerEvents(listener: StateListener<State>) {
        listener
                .on(state = State.FIND_STORE, execute = this::onFindPharmacy)
                .onType(state = State.SELECT_STORE, execute = this::onSelectPharmacy)
//                .onType(state = State.SAVE_PREFERRED_STORE, execute = this::onSavePreferredPharmacy)
//                .on(state = State.AUTHENTICATE, execute = this::onAuthenticate)
                .on(state = State.CANCEL, execute = this::onCancel)
                .on(state = State.BACK, execute = this::onBack)
                .onType(state = State.DONE, execute = this::onDone)
                .onType(state = State.FAIL, execute = this::onFail)
    }

    override fun onStart(args: Unit) {
        this.transition(from = State.BEGIN, to = State.FIND_STORE)
    }

    //
    // Private methods
    //

    private fun onFindPharmacy(state: State, with: Any?) {
        this.flow(fragment = this.mapListFragment, args = this.option)
                .back { this.transition(from = state, to = State.BACK) }
                .cancel { this.transition(from = state, to = State.CANCEL) }
                .complete { this.transition(from = state, to = State.SELECT_STORE, with = it) }
                .catch { this.transition(from = state, to = State.BACK, with = it) }
    }

    private fun onSelectPharmacy(state: State, storeParam: StoreLocatorSelectedStore) {
        this.selectedStore = storeParam
        val params = StoreDetailParams(
                storeItem = storeParam.store
                , userLocation = storeParam.userLocation
                , option = this.option
        )

        this.flow(fragment = this.storeDetailFragment, args = params)
                .back { this.transition(from = state, to = State.FIND_STORE) }
                .cancel { this.transition(from = state, to = State.CANCEL) }
                .complete { store ->
                    /*
                    val pharmacyProfileDelegation = AppProxy.proxy.accountManager().userProfile as? PharmacyProfileDelegation
                    if (this.option == StoreLocatorOption.SAVE_PREFERRED_STORE && pharmacyProfileDelegation != null) {
                        this.transition(
                                from = state
                                , to = State.SAVE_PREFERRED_STORE
                                , with = SavePreferredPharmacyParams(retry = true, store = store, profile = pharmacyProfileDelegation.profile))
                    } else {
                        this.transition(from = state, to = State.DONE, with = store)
                    }*/
                    this.transition(from = state, to = State.DONE, with = store)
                }
                .catch { this.transition(from = state, to = State.FAIL, with = it) }
    }

    /*
    private fun onSavePreferredPharmacy(state: State, params: SavePreferredPharmacyParams) {
        val (retry, store, profile) = params

        val form = UpdateProfile(profile = profile)
        form.preferredStore = store.storeId

        val activityIndicator = this.showActivityIndicator()

        AppProxy.proxy().accountManager()
                .updateProfile(form = form)
                .thenp { this.showSavedPreferredSuccess(store = store) }
                .then { this.transition(from = state, to = State.DONE, with = store) }
                .catch {
                    this.showErrorDialog(error = it)
                        .always { this.transition(from = state, to = State.SELECT_STORE, with = this.selectedStore) }
                }
                .always { activityIndicator?.hide() }
    }

    private fun showSavedPreferredSuccess(store: StoreItem): Promise<Int> {
        val onInitCustomView = { view: View ->
            view.name.text = store.name
            view.address.text = store.mailingAddress
            view.navigateIcon.setOnClickListener( {
                val position = store.position
                val uri = Uri.parse("https://maps.google.com/maps?daddr=${position.latitude},${position.longitude}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.`package` = "com.google.android.apps.maps"

                if (intent.resolveActivity(this.context.packageManager) != null) {
                    this.context.startActivity(intent);
                }
            })
        }
        val actions = arrayOf(this.getString(android.R.string.ok))
        return this.showDialog(layoutId = R.layout.dialog_preferred_store_selected
                                , onInitCustomView = onInitCustomView
                                , actions = actions)
    }
*/
    private fun onDone(state: State, store: StoreItem){
        this.finish(result = store)
    }
}
