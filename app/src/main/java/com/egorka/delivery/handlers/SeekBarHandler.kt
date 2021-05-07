package com.egorka.delivery.handlers

import android.widget.SeekBar

class SeekBarHandler(val callback: (Int) -> Unit): SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { }
    override fun onStartTrackingTouch(seekBar: SeekBar?) { }
    override fun onStopTrackingTouch(seekBar: SeekBar?) { seekBar?.let { callback(it.progress) } }
}