package com.fakhry.businessapp.core.utils.location

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

@Suppress("DEPRECATION")
object PermissionUtil {
    fun hasPermissions(context: Context, vararg perms: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (perm in perms) {
            val hasPerm = ContextCompat.checkSelfPermission(
                context,
                perm
            ) == PackageManager.PERMISSION_GRANTED
            if (!hasPerm) return false
        }
        return true
    }

    @JvmStatic
    fun checkDeniedPermissionsNeverAskAgain(
        `object`: Any, rationale: String?,
        @StringRes positiveButton: Int,
        @StringRes negativeButton: Int,
        deniedPerms: List<String>,
    ): Boolean {
        var shouldShowRationale: Boolean
        for (perm in deniedPerms) {
            shouldShowRationale = shouldShowRequestPermissionRationale(`object`, perm)
            if (!shouldShowRationale) {
                val activity = getActivity(`object`) ?: return true
                val dialog = AlertDialog.Builder(activity)
                    .setMessage(rationale)
                    .setPositiveButton(positiveButton) { _: DialogInterface?, _: Int ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", activity.packageName, null)
                        intent.data = uri
                        activity.startActivity(intent)
                    }
                    .setNegativeButton(negativeButton, null)
                    .create()
                dialog.show()
                return true
            }
        }
        return false
    }

    fun requestPermissions(
        `object`: Any,
        rationale: String,
        requestCode: Int,
        vararg perms: String,
    ) {
        checkCallingObjectSuitability(`object`)
        val callbacks = `object` as PermissionCallbacks
        var shouldShowRationale = false
        val listPerm = (perms as Array<*>).filterIsInstance<String>().toTypedArray()

        for (perm in perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(`object`, perm)
        }
        if (shouldShowRationale) {
            val activity = getActivity(`object`) ?: return
            val dialog = AlertDialog.Builder(activity)
                .setMessage(rationale)
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                    executePermissionsRequest(
                        `object`,
                        listPerm,
                        requestCode
                    )
                }
                .setNegativeButton(android.R.string.cancel) { _: DialogInterface?, _: Int ->
                    // act as if the permissions were denied
                    callbacks.onPermissionsDenied(requestCode, listOf(*listPerm))
                }
                .create()
            dialog.show()
        } else {
            executePermissionsRequest(`object`, listPerm, requestCode)
        }
    }

    @TargetApi(23)
    private fun shouldShowRequestPermissionRationale(`object`: Any, perm: String): Boolean {
        return when (`object`) {
            is Activity -> {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    `object`,
                    perm
                )
            }
            is Fragment -> {
                `object`.shouldShowRequestPermissionRationale(perm)
            }
            is android.app.Fragment -> {
                `object`.shouldShowRequestPermissionRationale(perm)
            }
            else -> {
                false
            }
        }
    }

    @TargetApi(23)
    private fun executePermissionsRequest(`object`: Any, perms: Array<String>, requestCode: Int) {
        checkCallingObjectSuitability(`object`)
        when (`object`) {
            is Activity -> {
                ActivityCompat.requestPermissions(`object`, perms, requestCode)
            }
            is Fragment -> {
                `object`.requestPermissions(perms, requestCode)
            }
            is android.app.Fragment -> {
                `object`.requestPermissions(perms, requestCode)
            }
        }
    }

    private fun getActivity(`object`: Any): Activity? {
        return when (`object`) {
            is Activity -> {
                `object`
            }
            is Fragment -> {
                `object`.activity
            }
            is android.app.Fragment -> {
                `object`.activity
            }
            else -> {
                null
            }
        }
    }

    private fun checkCallingObjectSuitability(`object`: Any) {
        // Make sure Object is an Activity or Fragment
        val isActivity = `object` is Activity
        val isSupportFragment = `object` is Fragment
        val isAppFragment = `object` is android.app.Fragment
        val isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        if (!(isSupportFragment || isActivity || isAppFragment && isMinSdkM)) {
            require(!isAppFragment) { "Target SDK needs to be greater than 23 if caller is android.app.Fragment" }
            throw IllegalArgumentException("Caller must be an Activity or a Fragment.")
        }
        require(`object` is PermissionCallbacks) { "Caller must implement PermissionCallbacks." }
    }

    interface PermissionCallbacks : OnRequestPermissionsResultCallback {
        fun onPermissionsGranted(requestCode: Int, perms: List<String>)
        fun onPermissionsDenied(requestCode: Int, perms: List<String>)
    }
}