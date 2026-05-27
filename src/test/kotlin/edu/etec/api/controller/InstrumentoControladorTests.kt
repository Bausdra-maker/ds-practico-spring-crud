package edu.etec.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import edu.etec.api.model.Instrumento
import edu.etec.api.service.InstrumentoServicio
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(InstrumentoControlador::class)
class InstrumentoControladorTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var instrumentoServicio: InstrumentoServicio

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `GET todos los instrumentos debe retornar lista vacia inicialmente`() {
        Mockito.`when`(instrumentoServicio.listarTodos()).thenReturn(emptyList())

        mockMvc.perform(MockMvcRequestBuilders.get("/api/instrumentos"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty)
    }

    @Test
    fun `GET todos los instrumentos debe retornar instrumentos existentes`() {
        val instrumentos = listOf(
            Instrumento(id = 1, nombre = "Guitarra", descripcion = null),
            Instrumento(id = 2, nombre = "Piano", descripcion = "De cola")
        )
        Mockito.`when`(instrumentoServicio.listarTodos()).thenReturn(instrumentos)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/instrumentos"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombre").value("Guitarra"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nombre").value("Piano"))
    }

    @Test
    fun `GET instrumento por id debe retornar instrumento cuando existe`() {
        val instrumento = Instrumento(id = 1, nombre = "Guitarra", descripcion = null)
        Mockito.`when`(instrumentoServicio.buscarPorId(1)).thenReturn(instrumento)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/instrumentos/1"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Guitarra"))
    }

    @Test
    fun `GET instrumento por id debe retornar 404 cuando no existe`() {
        Mockito.`when`(instrumentoServicio.buscarPorId(999)).thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.get("/api/instrumentos/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `POST debe crear nuevo instrumento y retornarlo`() {
        val nuevoInstrumento = Instrumento(id = 1, nombre = "Guitarra", descripcion = null)
        Mockito.`when`(instrumentoServicio.guardar(Mockito.any(Instrumento::class.java))).thenReturn(nuevoInstrumento)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/instrumentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nuevoInstrumento)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Guitarra"))
    }

    @Test
    fun `POST debe retornar 400 cuando nombre esta vacio`() {
        val instrumentoInvalido = Instrumento(id = 0, nombre = "", descripcion = null)

        mockMvc.perform(MockMvcRequestBuilders.post("/api/instrumentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(instrumentoInvalido)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `PUT debe actualizar instrumento existente`() {
        val instrumentoActualizado = Instrumento(id = 1, nombre = "Guitarra Acustica", descripcion = "Fender")
        Mockito.`when`(instrumentoServicio.actualizar(Mockito.eq(1), Mockito.any(Instrumento::class.java)))
            .thenReturn(instrumentoActualizado)

        mockMvc.perform(MockMvcRequestBuilders.put("/api/instrumentos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(instrumentoActualizado)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Guitarra Acustica"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.descripcion").value("Fender"))
    }

    @Test
    fun `PUT debe retornar 404 cuando instrumento no existe`() {
        val instrumentoActualizado = Instrumento(id = 999, nombre = "Test", descripcion = null)
        Mockito.`when`(instrumentoServicio.actualizar(Mockito.eq(999), Mockito.any(Instrumento::class.java)))
            .thenReturn(null)

        mockMvc.perform(MockMvcRequestBuilders.put("/api/instrumentos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(instrumentoActualizado)))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `DELETE debe remover instrumento y retornar 204`() {
        Mockito.`when`(instrumentoServicio.eliminar(1)).thenReturn(true)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/instrumentos/1"))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `DELETE debe retornar 404 cuando instrumento no existe`() {
        Mockito.`when`(instrumentoServicio.eliminar(999)).thenReturn(false)

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/instrumentos/999"))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}