package com.egorka.delivery.modules.errorActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorka.delivery.R

class ErrorActivity : AppCompatActivity(), ErrorActivityInterface {

    lateinit var presenter: ErrorPresenterInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        presenter = ErrorPresenter(this)
        presenter.onCreate()
    }

    override fun onResume() {
        presenter.onResume()
        super.onResume()
    }

    override fun onPause() {
        presenter.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun getContext(): Activity {
        return this
    }

}