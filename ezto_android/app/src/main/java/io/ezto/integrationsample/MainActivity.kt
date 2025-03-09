package io.ezto.integrationsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ezto.integrationsample.model.PostResponse
import io.ezto.integrationsample.ui.theme.IntegrationSampleTheme
import io.ezto.integrationsample.viewmodel.MainViewModel
import io.ezto.verify.sdk.Ezto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntegrationSampleTheme {
                PostRequestScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PostRequestScreen(viewModel: MainViewModel = MainViewModel()) {
    var resultText by remember { mutableStateOf("Click the button to send request") }

    val postResult: PostResponse? by viewModel.postResult.observeAsState(null)
    val errorMessage: String? by viewModel.error.observeAsState(null)


    postResult?.let { response ->
        val payload = mapOf(Pair("payload", response.qr_payload))
        Ezto.getInstance().onPushReceived(context = LocalContext.current,
            listener = EztoListener(), payload)
    }

    errorMessage?.let { error ->
        resultText = "Error: $error"
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = {
                viewModel.sendPostRequest()
            }
        ) {
            Text("Send POST Request")
        }

        Text(text = resultText, style = MaterialTheme.typography.bodyLarge)
    }
}