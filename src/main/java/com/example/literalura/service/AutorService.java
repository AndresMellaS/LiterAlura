package com.example.literalura.service;

import com.example.literalura.dto.DatosAutor;
import com.example.literalura.model.Autor;
import com.example.literalura.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodosLosAutores() {
        return autorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutoresVivosEnAño(Integer año) {
        return autorRepository.findAutoresVivosEnAño(año);
    }

    // Busca el autor en BD o lo crea si no existe
    @Transactional
    public Autor buscarOCrearAutor(DatosAutor datosAutor) {
        return autorRepository
                .findByNombreContainsIgnoreCase(datosAutor.nombre())
                .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));
    }
}
