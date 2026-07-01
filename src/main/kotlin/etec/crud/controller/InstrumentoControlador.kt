package etec.crud.controller

// Importamos tanto el modelo como el servicio porque el controlador conecta a ambos con la web
import etec.crud.model.Instrumento
import etec.crud.service.InstrumentoServicio
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/instrumentos")
class InstrumentoControlador(private val servicio: InstrumentoServicio) {

    // 1. LEER TODOS (GET /api/instrumentos)
    @GetMapping
    fun listar(): ResponseEntity<List<Instrumento>> {
        // ResponseEntity.ok(...) arma un 200 OK y mete la lista adentro del body (cuerpo)
        return ResponseEntity.ok(servicio.listarTodos())
    }

    // 2. LEER UNO (GET /api/instrumentos/{id})
    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): ResponseEntity<Instrumento> {
        val instrumento = servicio.buscarPorId(id)
        return if (instrumento != null) {
            ResponseEntity.ok(instrumento)
        } else {
            // .build() significa "Listo, fabricá la respuesta vacía" (Código 404)
            ResponseEntity.notFound().build()
        }
    }

    // 3. CREAR (POST /api/instrumentos)
    @PostMapping
    fun crear(@RequestBody instrumento: Instrumento): ResponseEntity<*> {
        if (instrumento.nombre.isBlank()) {
            // badRequest() devuelve un error 400 con un texto explicativo en el cuerpo
            return ResponseEntity.badRequest().body("El nombre no puede estar vacío")
        }
        val nuevo = servicio.guardar(instrumento)
        // Usamos status 201 (CREATED) porque la teoría dice que al crear algo nuevo se responde eso
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo)
    }

    // 4. ACTUALIZAR (PUT /api/instrumentos/{id})
    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody instrumento: Instrumento): ResponseEntity<*> {
        if (instrumento.nombre.isBlank()) {
            return ResponseEntity.badRequest().body("El nombre no puede estar vacío")
        }
        val editado = servicio.actualizar(id, instrumento)
        return if (editado != null) {
            ResponseEntity.ok(editado)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    // 5. ELIMINAR (DELETE /api/instrumentos/{id})
    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        val eliminado = servicio.eliminar(id)
        return if (eliminado) {
            // noContent() fabrica un 204: "Todo bien, ya lo borré... pero no tengo nada para mostrarte"
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
 }   
