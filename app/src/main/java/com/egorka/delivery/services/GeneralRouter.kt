package com.egorka.delivery.services

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.egorka.delivery.R
import com.egorka.delivery.modules.detailsActivity.DetailsActivity
import com.egorka.delivery.modules.errorActivity.ErrorActivity
import com.egorka.delivery.modules.mainActivity.MainActivity
import com.egorka.delivery.modules.newOrderActivity.NewOrderActivity

class GeneralRouter(val context: Activity) {

    private fun getOptionsBundle(view: View, name: String): Bundle? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return ActivityOptions.makeSceneTransitionAnimation(context, view, name).toBundle()
        }

        return null

    }

    fun openErrorActivity() {

        context.startActivity(Intent(context, ErrorActivity::class.java))

    }

    fun openMainActivity(view: View, name: String) {

        val intent = Intent(context, MainActivity::class.java)

        getOptionsBundle(view, name)?.let { context.startActivity(intent, it); return }
        context.startActivity(intent)

    }

    fun openNewOrderActivity() {

        context.startActivity(Intent(context, NewOrderActivity::class.java))
        context.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)

    }

    fun openDetailsActivity(view: View, name: String) {

        val intent = Intent(context, DetailsActivity::class.java)

        getOptionsBundle(view, name)?.let { context.startActivity(intent, it); return }
        context.startActivity(intent)

    }

    fun openNewLocationActivity() {

        context.startActivity(Intent(context, DetailsActivity::class.java))

    }

}