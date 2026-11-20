package cl.duoc.pizzeriaapp.data.models

open class Usuario(val nombre: String, val contacto: String) {
    open fun mostrarInformacion(): String = "Nombre: $nombre | Teléfono: $contacto"
}
