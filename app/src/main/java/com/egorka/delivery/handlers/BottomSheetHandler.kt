package com.egorka.delivery.handlers

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetHandler(val callback: (Int) -> Unit): BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) { callback(newState) }
    override fun onSlide(bottomSheet: View, slideOffset: Float) { }
}