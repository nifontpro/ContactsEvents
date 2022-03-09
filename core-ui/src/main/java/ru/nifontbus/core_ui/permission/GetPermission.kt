package ru.nifontbus.core_ui.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import ru.nifontbus.core_ui.R
import ru.nifontbus.core_ui.normalPadding

@ExperimentalPermissionsApi
@Composable
fun GetPermission(
    permission: String,
    text: String,
    content: @Composable (() -> Unit)
) {
    val permissionState = rememberPermissionState(permission)

    when {

        permissionState.status.isGranted -> {
            content()
        }

        permissionState.status.shouldShowRationale -> {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(normalPadding)
            ) {
                Text(text = text, style = MaterialTheme.typography.h4)
                Button(onClick = { permissionState.launchPermissionRequest() }) {
                    Text(
                        text = stringResource(R.string.sRetry),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
        }
        else -> {
            Text(text = text, style = MaterialTheme.typography.h4)
            LaunchedEffect(true) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

/*
@SuppressLint("QueryPermissionsNeeded")
fun openPrivacySettings(context: Context) {
    val intent = Intent(Settings.ACTION_PRIVACY_SETTINGS)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}*/
