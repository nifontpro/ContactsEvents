package ru.nifontbus.core_ui.permission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import ru.nifontbus.core_ui.Half3Gray
import ru.nifontbus.core_ui.bigPadding
import ru.nifontbus.core_ui.mediumPadding
import ru.nifontbus.core_ui.R

@ExperimentalPermissionsApi
@Composable
fun GetPermission(
    permission: String,
    text: String,
    content: @Composable (() -> Unit)
) {
    val permissionState = rememberPermissionState(permission)
    if (permissionState.shouldShowRationale) {
        PermissionMessageBox(text, stringResource(R.string.sRetry)) {
            permissionState.launchPermissionRequest()
        }
    }
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            LaunchedEffect(true) {
                permissionState.launchPermissionRequest()
            }
        },
        permissionNotAvailableContent = {
            val context = LocalContext.current
            PermissionMessageBox(text, stringResource(R.string.sOpenSettings)) {
                openPrivacySettings(context)
            }
        },
        content = content
    )
}

@ExperimentalPermissionsApi
@Composable
private fun PermissionMessageBox(
    text: String,
    buttonText: String,
    event: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(bigPadding)
                .clip(RoundedCornerShape(5))
                .background(Half3Gray)

        ) {
            Text(
                text,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(mediumPadding)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = event, modifier = Modifier.padding(bottom = mediumPadding)) {
                Text(buttonText)
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun openPrivacySettings(context: Context) {
    val intent = Intent(Settings.ACTION_PRIVACY_SETTINGS)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

/*@ExperimentalPermissionsApi
@Composable
fun GetAllPermission() {

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
                        Log.e("my", "Read contacts permission accepted")
                    }
                    perm.shouldShowRationale -> {
                        Log.e(
                            "my", "Read contacts permission is needed " +
                                    "to access the read contacts"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.e(
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
                        Log.e("my", "Write contacts permission accepted")
                    }
                    perm.shouldShowRationale -> {
                        Log.e(
                            "my", "Write contacts permission is needed " +
                                    "to access the write contacts"
                        )
                    }
                    perm.isPermanentlyDenied() -> {
                        Log.e(
                            "my", "Write contacts permission was permanently " +
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
*/