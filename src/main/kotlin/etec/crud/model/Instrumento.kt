package etec.crud.model

// El modelo define qué datos tiene un Instrumento en el sistema.
data class Instrumento(
    val id: Long,
    val nombre: String,
    val tipo: String,
    val precio: Double,
    val descripcion: String?
)
