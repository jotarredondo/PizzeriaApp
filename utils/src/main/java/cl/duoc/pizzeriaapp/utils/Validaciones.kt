package cl.duoc.pizzeriaapp.utils

object Validaciones {

    fun nombreClienteValido(nombre: String): Boolean {
        return nombre.trim().isNotEmpty()
    }

    fun normalizarNombre(nombre: String): String {
        return nombre.trim()
    }
}
