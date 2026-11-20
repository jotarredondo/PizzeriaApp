package cl.duoc.pizzeriaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import cl.duoc.pizzeriaapp.data.RegistroPizzeria
import cl.duoc.pizzeriaapp.data.ResumenPizzeria
import cl.duoc.pizzeriaapp.data.models.Cliente
import cl.duoc.pizzeriaapp.data.models.Pedido

import kotlinx.coroutines.launch

class PizzeriaViewModel : ViewModel() {

    // Acceso a los datos del módulo :data
    private val registro = RegistroPizzeria()

    // Estado observable desde la interfaz
    private val _resumen = MutableLiveData(
        registro.obtenerResumen()
    )

    val resumen: LiveData<ResumenPizzeria> = _resumen

    // Mensajes para comunicar errores
    private val _error = MutableLiveData<String?>(null)

    val error: LiveData<String?> = _error

    // Registrar un nuevo cliente
    fun registrarCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                _resumen.value = registro.registrarCliente(cliente)
                _error.value = null
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
            }
        }
    }

    // Registrar un nuevo pedido
    fun registrarPedido(pedido: Pedido) {
        viewModelScope.launch {
            try {
                _resumen.value = registro.registrarPedido(pedido)
                _error.value = null
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
            }
        }
    }
}
