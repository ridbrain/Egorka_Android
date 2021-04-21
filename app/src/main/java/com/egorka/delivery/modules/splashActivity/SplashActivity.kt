package com.egorka.delivery.modules.splashActivity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.egorka.delivery.R
import kotlinx.android.synthetic.main.activity_main.*

class SplashActivity : AppCompatActivity(), SplashActivityInterface {

    private var presenter: SplashPresenterInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slpash)
        presenter = SplashPresenter(this)
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun getContext(): Activity {
        return this
    }

    override fun getLogo(): View {
        return logo
    }

    override fun getTransitionName(): String {
        return getString(R.string.splash_transition)
    }

}