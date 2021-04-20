package com.egorka.delivery.modules.splashActivity

import android.content.Context
import android.content.Intent
import com.egorka.delivery.handlers.DataHandler
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.BasePresenter
import com.egorka.delivery.services.GeneralRouter
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class SplashPresenter(private val view: SplashActivityInterface): BasePresenter, SplashPresenterInterface {

    override var mainService: MainService? = null
    private var serviceConnection = ServiceConnect(this)

    override fun onResume() {
        view.getContext().bindService(Intent(view.getContext(), MainService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {

        val data = DataHandler(view.getContext())

        if (data.isUserUUID()) {

            openMainActivity()

        } else {

            NetworkHandler(view.getContext()).getUUID {

                data.setUserUUID(it.ID!!)
                data.setUserSecure(it.Secure!!)

                this.openMainActivity()

            }

        }

    }

    private fun openMainActivity() {

        DataHandler(view.getContext()).setErrorActivityState(false)
        GeneralRouter(view.getContext()).openMainActivity(view.getLogo(), view.getTransitionName())

    }

}