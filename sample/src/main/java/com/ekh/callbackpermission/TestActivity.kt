package com.ekh.callbackpermission

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.ekh.callbackpermission.databinding.ActivityTestBinding
import com.ekh.library.CallbackPermission
import com.ekh.library.callbackPermission

class TestActivity : AppCompatActivity() {

    val callbackPermissions: CallbackPermission by callbackPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


    }
}