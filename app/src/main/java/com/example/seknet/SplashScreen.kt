package com.example.seknet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(  {
            val i = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(i)
            finish()
        },1500)
    }
}