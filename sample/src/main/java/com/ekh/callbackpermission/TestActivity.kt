package com.ekh.callbackpermission

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekh.callbackpermission.databinding.ActivityTestBinding
import com.ekh.library.CallbackPermission
import com.ekh.library.callbackPermission
import com.google.android.material.snackbar.Snackbar

class TestActivity : AppCompatActivity() {

    private val callbackPermissions = callbackPermission()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityTestBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btCheckPermission.setOnClickListener {
            val isPermitted = callbackPermissions.hasPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            val text = if (isPermitted) "Permitted" else "Denied"
            Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
        }

        binding.btRequestPermission.setOnClickListener {
            callbackPermissions.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION) { deniedPermissions ->
                Snackbar.make(binding.root, deniedPermissions.toString(), Snackbar.LENGTH_LONG).show()
            }

        }
    }
}