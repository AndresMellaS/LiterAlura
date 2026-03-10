package com.example.literalura.controller;

import com.example.literalura.model.Libro;
import com.example.literalura.service.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public ResponseEntity<List<Libro>> listarTodos() {
        List<Libro> libros = libroService.listarTodosLosLibros();
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(libros); // 200
    }

    @GetMapping("/idioma/{idioma}")
    public ResponseEntity<?> listarPorIdioma(@PathVariable String idioma) {
        List<String> idiomasValidos = List.of("es", "en", "fr", "pt", "zh");
        if (!idiomasValidos.contains(idioma.toLowerCase())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Idioma no válido. Usa: es, en, fr, pt o zh.")); // 400
        }
        List<Libro> libros = libroService.listarLibrosPorIdioma(idioma.toLowerCase());
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(libros); // 200
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Libro>> top10() {
        List<Libro> libros = libroService.top10LibrosMasDescargados();
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(libros); // 200
    }

    @PostMapping("/buscar")
    public ResponseEntity<?> buscarYGuardar(@RequestBody Map<String, String> body) {
        String titulo = body.get("titulo");
        if (titulo == null || titulo.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El campo 'titulo' es obligatorio.")); // 400
        }
        try {
            Optional<Libro> libro = libroService.buscarYGuardarLibro(titulo.trim());
            return libro
                    .<ResponseEntity<?>>map(ResponseEntity::ok) // 200
                    .orElse(ResponseEntity.notFound().build());  // 404
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al conectar con la API externa.")); // 500
        }
    }

    @PostMapping("/cargar")
    public ResponseEntity<Map<String, String>> cargarDesdeAPI() {
        libroService.cargarLibrosDesdeAPI();
        return ResponseEntity.ok(Map.of("mensaje", "Carga desde API completada.")); // 200
    }
}