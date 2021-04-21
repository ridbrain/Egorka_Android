package com.egorka.delivery.modules.splashActivity

import com.egorka.delivery.handlers.DataHandler
import com.egorka.delivery.handlers.NetworkHandler
import com.egorka.delivery.services.GeneralRouter
import com.egorka.delivery.services.MainService
import com.egorka.delivery.services.ServiceConnect

class SplashPresenter(private val view: SplashActivityInterface): SplashPresenterInterface {

    private var mainService: MainService? = null

    override fun onResume() {
        ServiceConnect(view.getContext()) { mainService = it ; onStart() }
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