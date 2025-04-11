package com.example.permissaolocalizacao

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices

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


object PermissaoPrefs {
    private const val PREF_NAME = "permissao_prefs"
    private const val KEY_JA_NEGOU = "ja_negou"

    fun marcouQueNegou(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_JA_NEGOU, false)
    }

    fun marcarComoNegado(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_JA_NEGOU, true).apply()
    }

    fun limparNegacao(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_JA_NEGOU).apply()
    }
}

object LocalizacaoUtil {
    @SuppressLint("MissingPermission")
    fun pegarUltimaLocalizacao(
        context: Context,
        onResultado: (Location?) -> Unit
    ) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                onResultado(location)
            }
            .addOnFailureListener {
                onResultado(null)
            }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreenA(
    onPermissionGranted: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as Activity

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    var locationText by remember { mutableStateOf<String?>(null) }
    var mensagemNegacao by remember { mutableStateOf("") }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            PermissaoPrefs.limparNegacao(context)
            LocalizacaoUtil.pegarUltimaLocalizacao(context) {
                locationText = it?.let { loc ->
                    "📍 Latitude: ${loc.latitude}\n📍 Longitude: ${loc.longitude}"
                } ?: "Não foi possível obter a localização."
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            locationPermissionState.status.isGranted -> {
                Text("✅ Permissão concedida.")
                Spacer(modifier = Modifier.height(8.dp))
                Text(locationText ?: "Buscando localização...")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onPermissionGranted) {
                    Text("Continuar")
                }
            }

            locationPermissionState.status.shouldShowRationale -> {
                Text("❗ Precisamos da sua permissão para acessar a localização.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                    Text("Permitir Localização")
                }
            }

            else -> {
                if (PermissaoPrefs.marcouQueNegou(context)) {
                    mensagemNegacao =
                        "⚠ Você já negou essa permissão anteriormente. Vá nas configurações do app para permitir manualmente."
                }

                if (mensagemNegacao.isNotEmpty()) {
                    Text(mensagemNegacao, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Text("A permissão de localização ainda não foi concedida.")
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    PermissaoPrefs.marcarComoNegado(context)
                    locationPermissionState.launchPermissionRequest()
                }) {
                    Text("Solicitar Permissão")
                }
            }
        }
    }
}
