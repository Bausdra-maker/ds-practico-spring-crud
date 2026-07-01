package etec.crud

data class Instrumento(
    val id: Long,
    val nombre: String,
    val tipo: String,
    val precio: Double,
    val descripcion: String?
)
