package com.egorka.delivery.modules.errorActivity

import com.egorka.delivery.handlers.DataHandler

class ErrorPresenter(override var view: ErrorActivityInterface): ErrorPresenterInterface {

    override fun onCreate() {
        DataHandler(view.getContext()).setErrorActivityState(true)
    }

    override fun onResume() {

    }

    override fun onPause() {
        DataHandler(view.getContext()).setErrorActivityState(false)
    }

    override fun onBackPressed() {

    }

}