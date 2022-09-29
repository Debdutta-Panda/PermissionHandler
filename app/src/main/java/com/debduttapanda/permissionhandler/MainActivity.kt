package com.debduttapanda.permissionhandler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.debduttapanda.permissionhandler.ui.theme.PermissionHandlerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainUI()
                }
            }
        }
    }
}

class MainViewModel: ViewModel(){
    val permissions = listOf(android.Manifest.permission.CAMERA)
    val state = mutableStateOf("")
    val ph = PermissionHandler()
    @OptIn(ExperimentalPermissionsApi::class)
    fun checkPermission() {
        viewModelScope.launch {
            val r = ph.check(*(permissions.toTypedArray()))
            state.value = (r?.permissions?.map {
                "${it.permission}=${it.status}"
            }?.joinToString("\n"))?:"null"
        }
    }

    fun requestPermission() {
        viewModelScope.launch {
            val r = ph.request(*(permissions.toTypedArray()))
            state.value = r?.entries?.map {
                "${it.key}=${it.value}"
            }?.joinToString("\n")?:"null"
        }
    }
}

@Composable
fun MainUI(
    vm: MainViewModel = viewModel()
){
    vm.ph.handlePermission()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(vm.permissions.toString())
            Button(onClick = {
                vm.checkPermission()
            }) {
                Text("Check")
            }
            Button(onClick = {
                vm.requestPermission()
            }) {
                Text("Request")
            }
            Text("Result: ${vm.state.value}")
        }
    }
}