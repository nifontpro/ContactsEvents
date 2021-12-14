package ru.nifontbus.contactsevents.presentation.navigation

import android.Manifest
import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun GetPermission() {
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
        )
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    permissionsState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.READ_CONTACTS -> {
                when {
                    perm.hasPermission -> {
                        Log.d("my", "Read contacts permission accepted")
                    }
                    perm.shouldShowRationale -> {
                        Text(
                            text = "Read contacts permission is needed " +
                                    "to access the contacts"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.d(
                            "my", "Read contacts permission was permanently " +
                                    "denied. You can enable it in the app" +
                                    "settings."
                        )
                    }
                }
            }
            Manifest.permission.WRITE_CONTACTS -> {
                when {
                    perm.hasPermission -> {
                        Log.d("my", "Write contacts permission accepted")
                    }
                    perm.shouldShowRationale -> {
                        Log.d(
                            "my", "Write contacts permission is needed " +
                                    "to access the write contacts"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.d(
                            "my", "Write contacts permission was permanently " +
                                    "denied. You can enable it in the app" +
                                    "settings."
                        )
                    }
                }
            }
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when {
                    perm.hasPermission -> {
                        Log.d("my", "Read storage permission accepted")
                    }
                    perm.shouldShowRationale -> {
                        Log.d(
                            "my", "Read storage permission is needed " +
                                    "to access the write contacts"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.d(
                            "my", "Read storage permission was permanently " +
                                    "denied. You can enable it in the app" +
                                    "settings."
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !shouldShowRationale && !hasPermission
}
