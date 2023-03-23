package com.ekh.library

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun AppCompatActivity.callbackPermission(): Lazy<CallbackPermission> = lazy { CallbackPermission(this) }

fun Fragment.callbackPermission(): Lazy<CallbackPermission> = lazy { CallbackPermission(this) }

class CallbackPermission {
    private val context: Context
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
    private var onRequest = false
    private var callback: PermissionCheckCallBack? = null

    constructor(activity: AppCompatActivity) {
        context = activity
        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { doOnComplete(it) }
    }

    constructor(fragment: Fragment) {
        context = fragment.requireContext()
        permissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { doOnComplete(it) }
    }

    private val doOnComplete = { results: Map<String, Boolean> ->
        onRequest = false
        val deniedPermissions = results.filter { !it.value }.keys
        callback?.onResult(deniedPermissions)
    }

    fun isPermissionGranted(vararg permissions: String): Boolean {
        val deniedPermission = permissions.asSequence()
            .map { ContextCompat.checkSelfPermission(context, it) }
            .filter { it != PackageManager.PERMISSION_GRANTED }
            .take(1)

        return deniedPermission.toList().isEmpty()
    }

    fun requestPermission(vararg permissions: String, callback: PermissionCheckCallBack) {
        if (onRequest) return
        onRequest = true
        this.callback = callback

        permissionLauncher.launch(arrayOf(*permissions))
    }
}


fun interface PermissionCheckCallBack {
    fun onResult(deniedPermissions: Set<String>)
}