package com.egorka.delivery.modules.aboutActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorka.delivery.R
import com.egorka.delivery.handlers.DataHandler
import com.egorka.delivery.services.copyString
import com.egorka.delivery.services.showToast
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), AboutActivityInterface {

    private var presenter: AboutPresenterInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        presenter = AboutPresenter(this)

        backButton.setOnClickListener { onBackPressed() }
        versionLabel.setOnClickListener {
            DataHandler(this).getUserFCM()?.let { token ->
                copyString(token)
                showToast("Token скопирован")
            }
        }

    }

    override fun getContext(): Activity {
        return this
    }
}