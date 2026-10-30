
package com.berberajandasi.app

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.view.Gravity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val testText = TextView(this)
        testText.text = "BERBER AJANDASI\n\nUygulama başarıyla açıldı!"
        testText.textSize = 24f
        testText.gravity = Gravity.CENTER

        setContentView(testText)
    }
}
