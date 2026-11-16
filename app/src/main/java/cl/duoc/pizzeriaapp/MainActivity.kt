package cl.duoc.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.pizzeriaapp.ui.theme.PizzeriaAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PizzeriaAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {

    val pedido = remember { generarPedidoDemo() }

    val formatoMoneda = remember {
        NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("es-CL")
        ).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Bienvenidos a la mejor pizzería de la ciudad",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "PEDIDO DE DEMOSTRACIÓN",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Cliente: ${pedido.nombreCliente}")
        Text("Producto: ${pedido.nombreProducto}")
        Text("Cantidad: ${pedido.cantidad}")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Subtotal: ${formatoMoneda.format(pedido.subtotal)}")
        Text("Descuento: ${formatoMoneda.format(pedido.descuento)}")
        Text("IVA (19%): ${formatoMoneda.format(pedido.impuesto)}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total: ${formatoMoneda.format(pedido.total)}",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PizzeriaAppTheme {
        Greeting()
    }
}
