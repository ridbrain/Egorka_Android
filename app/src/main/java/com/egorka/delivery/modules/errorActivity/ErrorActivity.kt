package com.egorka.delivery.modules.errorActivity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorka.delivery.R
import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity(), ErrorActivityInterface {

    private var presenter: ErrorPresenterInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        presenter = ErrorPresenter(this)

        updateButton.setOnClickListener { presenter?.pressUpdate() }

    }

    override fun onResume() {
        presenter?.onResume()
        super.onResume()
    }

    override fun onPause() {
        presenter?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        presenter?.onBackPressed()
    }

    override fun getContext(): Activity {
        return this
    }

}