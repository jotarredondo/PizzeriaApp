package cl.duoc.pizzeriaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.pizzeriaapp.data.models.Cliente
import cl.duoc.pizzeriaapp.data.models.DetallePedido
import cl.duoc.pizzeriaapp.data.models.Pedido
import cl.duoc.pizzeriaapp.data.models.PeriodoPromocion
import cl.duoc.pizzeriaapp.data.models.Pizza
import cl.duoc.pizzeriaapp.ui.theme.PizzeriaAppTheme
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PizzeriaAppTheme {
                val pizzeriaViewModel: PizzeriaViewModel = viewModel()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaPizzeria(
                        viewModel = pizzeriaViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaPizzeria(
    viewModel: PizzeriaViewModel,
    modifier: Modifier = Modifier
) {
    // Observar LiveData del ViewModel.
    val resumen by viewModel.resumen.observeAsState()
    val error by viewModel.error.observeAsState()

    // Campos del formulario.
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var cantidadTexto by rememberSaveable { mutableStateOf("2") }
    var mensajeLocal by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Reutilizar la lógica de la semana 4.
    val pedidoDemo = remember { generarPedidoDemo() }

    val moneda = remember {
        NumberFormat.getCurrencyInstance(
            Locale.forLanguageTag("es-CL")
        ).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    fun crearCliente(): Cliente {
        return Cliente(
            nombre = nombre.trim(),
            contacto = telefono.trim(),
            direccion = direccion.trim().ifBlank {
                "No informada"
            },
            correo = correo.trim().ifBlank {
                "No informado"
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Bienvenidos a la mejor pizzería de la ciudad",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Resumen dinámico de la semana 5.
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "RESUMEN DE LA PIZZERÍA",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    "Clientes registrados: " +
                            "${resumen?.totalClientes ?: 0}"
                )

                Text(
                    "Pedidos realizados: " +
                            "${resumen?.totalPedidos ?: 0}"
                )

                Text(
                    "Último cliente: " +
                            (resumen?.ultimoCliente ?: "Sin registros")
                )
            }
        }

        // Formulario de registro.
        Text(
            "Registrar cliente y pedido",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del cliente") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo (opcional)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cantidadTexto,
            onValueChange = { cantidadTexto = it },
            label = { Text("Cantidad de pizzas (1-100)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (nombre.isBlank() || telefono.isBlank()) {
                    mensajeLocal = "Ingresa nombre y teléfono."
                } else {
                    viewModel.registrarCliente(crearCliente())
                    mensajeLocal = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar cliente")
        }

        Button(
            onClick = {
                val cantidad = cantidadTexto.toIntOrNull()

                if (nombre.isBlank() || telefono.isBlank()) {
                    mensajeLocal = "Ingresa nombre y teléfono."
                } else if (cantidad == null || cantidad !in 1..100) {
                    mensajeLocal = "Cantidad inválida. Usa 1 a 100."
                } else {
                    val cliente = crearCliente()

                    val pizza = Pizza(
                        nombre = "Pepperoni",
                        precio = 9990.0,
                        ingredientes = mapOf(
                            "Masa" to 1,
                            "Salsa" to 1,
                            "Queso" to 2,
                            "Pepperoni" to 1
                        )
                    )

                    val pedido = Pedido(
                        idPedido = 1001 +
                                (resumen?.totalPedidos ?: 0),
                        cliente = cliente,
                        detalles = listOf(
                            DetallePedido(pizza, cantidad)
                        ),
                        fecha = LocalDate.now(),
                        periodoPromocion = PeriodoPromocion(
                            LocalDate.of(2026, 11, 1),
                            LocalDate.of(2026, 11, 15)
                        )
                    )

                    viewModel.registrarPedido(pedido)
                    mensajeLocal = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar pedido")
        }

        mensajeLocal?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        // Mantener el ejemplo desarrollado en semana 4.
        Text(
            "PEDIDO DE DEMOSTRACIÓN",
            style = MaterialTheme.typography.titleMedium
        )

        Text("Cliente: ${pedidoDemo.nombreCliente}")
        Text("Producto: ${pedidoDemo.nombreProducto}")
        Text("Cantidad: ${pedidoDemo.cantidad}")
        Text("Subtotal: ${moneda.format(pedidoDemo.subtotal)}")
        Text("Descuento: ${moneda.format(pedidoDemo.descuento)}")
        Text("IVA (19%): ${moneda.format(pedidoDemo.impuesto)}")

        Text(
            "Total: ${moneda.format(pedidoDemo.total)}",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

