package com.example.mylocationapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModal : ViewModel() {

    private val _location = mutableStateOf<LocationData?>(null)
    val loctaion : State<LocationData?> = _location

    fun updateLocation(newLocation:LocationData){
        _location.value = newLocation
    }

}