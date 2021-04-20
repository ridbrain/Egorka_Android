package com.egorka.delivery.modules.mainActivity

import android.app.Activity
import androidx.drawerlayout.widget.DrawerLayout
import com.egorka.delivery.R
import com.egorka.delivery.services.GeneralRouter

class MainMenu(val context: Activity, private val drawer: DrawerLayout) {

    fun pressButton(button: Int) {

        when (button) {
            R.id.currentOrder -> { GeneralRouter(context).openCurrentOrder() }
            R.id.about -> { }
        }

        drawer.closeDrawers()

    }

}