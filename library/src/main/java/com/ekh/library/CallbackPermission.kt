package com.ekh.library

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


fun ComponentActivity.callbackPermission(): CallbackPermission = CallbackPermissionImpl(this)

fun Fragment.callbackPermission(): CallbackPermission = CallbackPermissionImpl(this)

interface CallbackPermission {
    fun hasPermissions(vararg permissions: String): Boolean

    fun requestPermission(vararg permissions: String, callback: PermissionCheckCallBack)
}

internal class CallbackPermissionImpl : CallbackPermission {
    private val context: Context
    private val permissionLauncher: ActivityResultLauncher<Array<String>>

    private var onRequest = false
    private var callback: PermissionCheckCallBack? = null

    constructor(activity: ComponentActivity) {
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

    override fun hasPermissions(vararg permissions: String): Boolean {
        val deniedPermission = permissions.asSequence()
            .map { ContextCompat.checkSelfPermission(context, it) }
            .filter { it != PackageManager.PERMISSION_GRANTED }
            .take(1)

        return deniedPermission.toList().isEmpty()
    }

    override fun requestPermission(vararg permissions: String, callback: PermissionCheckCallBack) {
        if (onRequest) return
        onRequest = true
        this.callback = callback

        permissionLauncher.launch(arrayOf(*permissions))
    }

}


fun interface PermissionCheckCallBack {
    fun onResult(deniedPermissions: Set<String>)
}