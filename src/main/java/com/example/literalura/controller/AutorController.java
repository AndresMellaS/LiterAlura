package com.example.literalura.controller;

import com.example.literalura.model.Autor;
import com.example.literalura.service.AutorService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }
    @GetMapping
    public ResponseEntity<List<Autor>> listarTodos(){
        List<Autor> autores = autorService.listarTodosLosAutores();
        if (autores.isEmpty()){
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(autores); //200
    }

    @GetMapping("/vivos/{año}")
    public ResponseEntity<?> listarVivosEnAño (@PathVariable Integer  año){
        if(año < 0 || año > 2026){
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ingresa un año válido entre 0 y 2026")); //400
        }
        List<Autor> autores = autorService.listarAutoresVivosEnAño(año);
        if(autores.isEmpty()){
            return ResponseEntity.noContent().build(); //204
        }
        return ResponseEntity.ok(autores); //200
    }










}


