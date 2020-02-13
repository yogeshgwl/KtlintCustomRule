package com.example.ktlintcustomrule

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val button: Button? = null
    var tetst = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addd(2, 3, 4, 5, 5, 6, 6)
    }

    fun addd(
        x: Int,
        y: Int, z: Int,
        a: Int, b: Int, c: Int, d: Int
    ) {
        x + y
        x + z
        x + a
        x + b
        x + d
        x + c
        c + b
        d + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        y + b
        z + b
        d + b
        c + b
    }

    fun addd2(x: Int, y: Int, z: Int, a: Int, b: Int) {
        x + y
        x + z
        x + a
    }

    fun addd3(x: Int, y: Int, z: Int, a: Int, b: Int) {
        x + y
        x + z
        x + a
    }

    fun addd4(x: Int, y: Int, z: Int, a: Int, b: Int) {

    }
}
