package cl.duoc.pizzeriaapp.data

import cl.duoc.pizzeriaapp.data.models.Cliente
import cl.duoc.pizzeriaapp.data.models.Pedido
import cl.duoc.pizzeriaapp.utils.Validaciones

data class ResumenPizzeria(
    val totalClientes: Int,
    val totalPedidos: Int,
    val ultimoCliente: String
)

class RegistroPizzeria {

    private val clientes = mutableListOf<Cliente>()
    private val pedidos = mutableListOf<Pedido>()

    fun registrarCliente(cliente: Cliente): ResumenPizzeria {
        require(Validaciones.nombreClienteValido(cliente.nombre)) {
            "El nombre del cliente no puede estar vacío."
        }

        // Evitar duplicar clientes mediante su contacto.
        if (clientes.none { it.contacto == cliente.contacto }) {
            clientes.add(cliente)
        }

        return obtenerResumen()
    }

    fun registrarPedido(pedido: Pedido): ResumenPizzeria {

        // Asegurar que el cliente también esté registrado.
        registrarCliente(pedido.cliente)

        pedidos.add(pedido)

        return obtenerResumen()
    }

    fun obtenerResumen(): ResumenPizzeria {
        return ResumenPizzeria(
            totalClientes = clientes.size,
            totalPedidos = pedidos.size,
            ultimoCliente = clientes.lastOrNull()?.let {
                Validaciones.normalizarNombre(it.nombre)
            } ?: "Sin registros"
        )
    }
}
