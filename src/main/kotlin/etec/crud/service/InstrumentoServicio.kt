package etec.crud.service

import etec.crud.model.Instrumento
import org.springframework.stereotype.Service

@Service
class InstrumentoServicio {
    // Lista en memoria para guardar los datos temporalmente
    private val instrumentos = mutableListOf<Instrumento>()

    // C: CREATE - Guardar un instrumento
    fun guardar(instrumento: Instrumento): Instrumento {
        instrumentos.add(instrumento)
        return instrumento
    }

    // R: READ - Listar todos los instrumentos
    fun listarTodos(): List<Instrumento> {
        return instrumentos
    }

    // R: READ - Buscar un instrumento por su ID
    fun buscarPorId(id: Long): Instrumento? {
        return instrumentos.find { it.id == id }
    }

    // U: UPDATE - Modificar un instrumento existente
    fun actualizar(id: Long, instrumentoActualizado: Instrumento): Instrumento? {
        val indice = instrumentos.indexOfFirst { it.id == id }
        if (indice != -1) {
            instrumentos[indice] = instrumentoActualizado
            return instrumentoActualizado
        }
        return null // Si no lo encuentra, devuelve null de forma segura
    }

    // D: DELETE - Borrar un instrumento
    fun eliminar(id: Long): Boolean {
        return instrumentos.removeIf { it.id == id }
    }
}