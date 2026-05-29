package com.example.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Enum of all permissions that the Agent may request at runtime.
 * Each entry contains the Android permission string and a user-facing description.
 */
enum class AgentPermission(
    val permission: String,
    val description: String
) {
    READ_CALENDAR(
        android.Manifest.permission.READ_CALENDAR,
        "Read your calendar events to suggest free time slots"
    ),
    WRITE_CALENDAR(
        android.Manifest.permission.WRITE_CALENDAR,
        "Create events in your calendar on your behalf"
    ),
    READ_CONTACTS(
        android.Manifest.permission.READ_CONTACTS,
        "Read contacts to address emails and identify people"
    ),
    SEND_SMS(
        android.Manifest.permission.SEND_SMS,
        "Send SMS messages (optional, for future extensions)"
    ),
    ACCESS_FINE_LOCATION(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        "Access precise location for location-based suggestions"
    ),
    GET_ACCOUNTS(
        android.Manifest.permission.GET_ACCOUNTS,
        "Read your device accounts for email integration"
    )
}

/**
 * Helper that simplifies requesting multiple runtime permissions from an Activity.
 * It uses the new Activity Result API and suspends until the user responds.
 */
class PermissionsManager(private val activity: ComponentActivity) {

    private var pendingContinuation: ((Map<String, Boolean>) -> Unit)? = null

    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result: Map<String, Boolean> ->
        pendingContinuation?.invoke(result)
        pendingContinuation = null
    }

    /**
     * Checks whether a given permission is already granted.
     */
    fun isGranted(permission: AgentPermission): Boolean =
        ContextCompat.checkSelfPermission(activity, permission.permission) ==
                PackageManager.PERMISSION_GRANTED

    /**
     * Requests one or more permissions and suspends until the user answers.
     * Returns a map of permission string to granted boolean.
     */
    suspend fun requestPermissions(vararg perms: AgentPermission): Map<String, Boolean> =
        suspendCancellableCoroutine { cont ->
            val permissionStrings = perms.map { it.permission }.toTypedArray()
            pendingContinuation = { result ->
                cont.resume(result)
            }
            requestPermissionLauncher.launch(permissionStrings)
        }
}
