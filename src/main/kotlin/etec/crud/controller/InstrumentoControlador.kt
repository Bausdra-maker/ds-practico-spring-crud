package etec.crud

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/instrumentos")
class InstrumentoControlador(private val servicio: InstrumentoServicio) {

    // 1. LEER TODOS (GET /api/instrumentos)
    @GetMapping
    fun listar(): ResponseEntity<List<Instrumento>> {
        // ResponseEntity.ok(...) arma una respuesta exitosa (Código 200) 
        // y adentro del body (cuerpo) mete la lista que nos da el servicio.
        return ResponseEntity.ok(servicio.listarTodos())
    }

    // 2. LEER UNO SOLO (GET /api/instrumentos/{id})
    @GetMapping("/{id}")
    fun buscar(@PathVariable id: Long): ResponseEntity<Instrumento> {
        val instrumento = servicio.buscarPorId(id)
        
        return if (instrumento != null) {
            // Si lo encontró, mandamos un 200 OK con el instrumento adentro del cuerpo
            ResponseEntity.ok(instrumento)
        } else {
            // Si no está, build() significa "armá la respuesta ya mismo". 
            // Como un 404 (Not Found) no lleva ningún dato/texto adentro, 
            // simplemente lo fabricamos vacío con .build()
            ResponseEntity.notFound().build()
        }
    }

    // 3. CREAR UNO NUEVO (POST /api/instrumentos)
    @PostMapping
    fun crear(@RequestBody instrumento: Instrumento): ResponseEntity<*> {
        // Validación humana: si se olvidaron de poner el nombre, no avanzamos
        if (instrumento.nombre.isBlank()) {
            // badRequest() manda un error 400. .body(...) nos deja meter un texto 
            // personalizado para avisarle al cliente qué hizo mal.
            return ResponseEntity.badRequest().body("El nombre no puede estar vacío")
        }
        
        val nuevo = servicio.guardar(instrumento)
        // status(...) nos deja elegir un código HTTP específico. 
        // Usamos CREATED (201) porque la teoría dice que al crear algo nuevo se responde eso.
        // Al final le pegamos el .body(nuevo) para devolver el instrumento con su ID ya asignado.
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo)
    }

    // 4. ACTUALIZAR (PUT /api/instrumentos/{id})
    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody instrumento: Instrumento): ResponseEntity<*> {
        // Volvemos a validar que no nos manden un nombre en blanco
        if (instrumento.nombre.isBlank()) {
            return ResponseEntity.badRequest().body("El nombre no puede estar vacío")
        }
        
        val editado = servicio.actualizar(id, instrumento)
        return if (editado != null) {
            // Si existía y se pudo cambiar, devolvemos 200 OK con los datos nuevos
            ResponseEntity.ok(editado)
        } else {
            // Si intentaron editar un ID que no existe, cerramos la pestaña con un 404 vacío
            ResponseEntity.notFound().build()
        }
    }

    // 5. ELIMINAR (DELETE /api/instrumentos/{id})
    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long): ResponseEntity<Void> {
        val eliminado = servicio.eliminar(id)
        
        return if (eliminado) {
            // noContent() fabrica un código 204. Significa: "Todo salió perfecto, 
            // ya lo borré, pero como no existe más, no tengo nada para mostrarte".
            // Como no lleva datos, cerramos con .build()
            ResponseEntity.noContent().build()
        } else {
            // Si el ID no existía para ser borrado, 404 Not Found
            ResponseEntity.notFound().build()
        }
    }
}
