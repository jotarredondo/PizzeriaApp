package cl.duoc.pizzeriaapp

import cl.duoc.pizzeriaapp.data.models.Cliente
import cl.duoc.pizzeriaapp.data.models.DetallePedido
import cl.duoc.pizzeriaapp.data.models.Pedido
import cl.duoc.pizzeriaapp.data.models.PeriodoPromocion
import cl.duoc.pizzeriaapp.data.models.Pizza
import cl.duoc.pizzeriaapp.data.models.Usuario
import java.time.LocalDate

/** Datos de demostración, independientes de Android y de la consola. */
data class ResumenPedido(
    val nombreCliente: String,
    val nombreProducto: String,
    val cantidad: Int,
    val subtotal: Double,
    val descuento: Double,
    val impuesto: Double,
    val total: Double
)

fun generarPedidoDemo(): ResumenPedido {
    val pizza = Pizza(
        nombre = "Pepperoni",
        precio = 9990.0,
        ingredientes = mapOf("Masa" to 1, "Salsa" to 1, "Queso" to 2, "Pepperoni" to 1)
    )
    val cliente = Cliente(
        nombre = "Cliente de demostración",
        contacto = "+56912345678",
        direccion = "Dirección de ejemplo",
        correo = "cliente@example.com"
    )
    val usuario: Usuario = cliente
    val pedido = Pedido(
        idPedido = 1001,
        cliente = cliente,
        detalles = listOf(DetallePedido(pizza, 2)),
        fecha = LocalDate.of(2026, 10, 1), // Fuera del período promocional.
        periodoPromocion = PeriodoPromocion(
            inicio = LocalDate.of(2026, 11, 1),
            fin = LocalDate.of(2026, 11, 15)
        )
    )
    return ResumenPedido(
        nombreCliente = usuario.nombre,
        nombreProducto = pizza.nombre,
        cantidad = pedido.detalles.sumOf { it.cantidad },
        subtotal = pedido.calcularSubtotal(),
        descuento = pedido.calcularDescuento(),
        impuesto = pedido.calcularImpuesto(),
        total = pedido.calcularTotal()
    )
}
