package com.example.mylocationapp

import android.content.Context
import android.os.Bundle
import android.Manifest
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mylocationapp.ui.theme.MyLocationAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModal: LocationViewModal = viewModel()
            MyLocationAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModal)
                }
            }
        }
    }
}


@Composable
fun MyApp(viewModal: LocationViewModal)
{
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationDisplay(locationUtils = locationUtils,viewModal = viewModal, context = context)
}

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModal: LocationViewModal,
    context : Context
){
    val location = viewModal.loctaion.value

    val address = location?.let {
            locationUtils.reverseGeoCodeLocation(location)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            permissions->
            if(permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true){
                //We have Accessed the location
                locationUtils.requestLocationUpdates123(viewModal = viewModal)
            }
            else{
                //Ask for the permission
                //ActivityCompat.shouldShowRequestPermissionRationale is a method provided by
                // the Android Support Library to determine whether the app should show an
                // explanation to the user for why a permission is needed.

                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context,
                        Manifest.permission.ACCESS_COARSE_LOCATION)

                if(rationaleRequired){
                    Toast.makeText(
                        context,
                        "Location Permission Required",Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    Toast.makeText(
                        context,
                        "Location Permission Required,Enable Location in Setting",Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(location != null){
            Text(text = "Address : ${location.latitude} - ${location.longitude} \n $address")
        }
        else{
            Text(text = "Location Not Available")
        }

        Button(
            onClick = {
            if(locationUtils.hasLocationPermission(context)){
                //Permission Already Granted
                locationUtils.requestLocationUpdates123(viewModal)
            }
            else{
                //Get the Access
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}