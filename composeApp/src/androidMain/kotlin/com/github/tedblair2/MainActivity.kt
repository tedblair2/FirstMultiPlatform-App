package com.github.tedblair2

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.github.tedblair2.viewmodel.AppViewModel
import com.github.tedblair2.viewmodel.CountriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel:AppViewModel by viewModel()
    private val countriesViewModel:CountriesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val counterState by viewModel.counterState.collectAsState()
            val countryScreenState by countriesViewModel.countryState.collectAsState()

            App(
                countryScreenState = countryScreenState,
                onEvent = countriesViewModel::onEvent
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}