package com.example.permissaotempoexecucao

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*

@Composable
fun HomeScreen(funcition: () -> Unit,){
    Button(onClick = {funcition()},
        modifier = Modifier.padding(top = 100.dp)

    ) {
        Text("Localização")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            locationPermissionState.status.isGranted -> {
                Text("Permissão concedida. Localização pode ser usada.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onPermissionGranted) {
                    Text("Continuar")
                }
            }

            locationPermissionState.status.shouldShowRationale -> {
                // olha... sem a loca aoijoif
                // justificarrr
                Text("Precisamos da sua permissão para acessar a localização.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Permitir Localização")
                }
            }

            else -> {

                Text("Permissão de localização ainda não foi concedida.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    //


                    locationPermissionState.launchPermissionRequest() }) {
                    Text("Solicitar Permissão")




                }
            }
        }
    }
}