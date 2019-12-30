package com.example.ktlintcustomrule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    var tetst = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addd(2, 3, 4, 5, 5)
    }

    fun addd(x: Int, y: Int, z: Int, a: Int, b: Int) {
        x + y
        x + z
        x + a
        x + b
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
