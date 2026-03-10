package com.example.literalura.service;

import com.example.literalura.dto.DatosLibro;
import com.example.literalura.model.Autor;
import com.example.literalura.model.Libro;
import com.example.literalura.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorService autorService;
    private final GutendexClient gutendexClient;

    public LibroService(LibroRepository libroRepository,
                        AutorService autorService,
                        GutendexClient gutendexClient) {
        this.libroRepository = libroRepository;
        this.autorService = autorService;
        this.gutendexClient = gutendexClient;
    }

    @Transactional
    public Optional<Libro> buscarYGuardarLibro(String titulo) {
        Optional<Libro> libroExistente = libroRepository
                .findByTituloContainsIgnoreCase(titulo);
        if (libroExistente.isPresent()) {
            System.out.println("⚠ El libro ya está registrado en la base de datos.");
            return libroExistente;
        }

        Optional<DatosLibro> datosLibro = gutendexClient.buscarPrimerLibroPorTitulo(titulo);
        if (datosLibro.isEmpty()) {
            return Optional.empty();
        }

        Optional<Libro> duplicado = libroRepository
                .findByTituloContainsIgnoreCase(datosLibro.get().titulo());
        if (duplicado.isPresent()) {
            System.out.println("⚠ El libro ya está registrado en la base de datos.");
            return duplicado;
        }

        Autor autor = autorService.buscarOCrearAutor(datosLibro.get().autores().get(0));
        return Optional.of(libroRepository.save(new Libro(datosLibro.get(), autor)));
    }

    @Transactional(readOnly = true)
    public List<Libro> listarTodosLosLibros() {
        return libroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }

    @Transactional(readOnly = true)
    public List<Libro> top10LibrosMasDescargados() {
        return libroRepository.findTop10ByOrderByNumeroDescargasDesc();
    }

    @Transactional
    public void cargarLibrosDesdeAPI() {
        System.out.println("⏳ Cargando libros desde la API, espera...");
        int totalGuardados = 0;

        for (int pagina = 1; pagina <= 3; pagina++) {
            List<DatosLibro> resultados;
            try {
                resultados = gutendexClient.obtenerLibrosPorPagina(pagina);
            } catch (RuntimeException e) {
                System.out.println("❌ Error en página " + pagina + ". Abortando.");
                break;
            }

            if (resultados.isEmpty()) break;

            for (DatosLibro datosLibro : resultados) {
                if (datosLibro.autores().isEmpty() || datosLibro.idiomas().isEmpty()) continue;
                if (datosLibro.titulo() == null || datosLibro.titulo().isBlank()) continue;
                if (libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo()).isPresent()) continue;

                try {
                    Autor autor = autorService.buscarOCrearAutor(datosLibro.autores().get(0));
                    libroRepository.save(new Libro(datosLibro, autor));
                    totalGuardados++;
                } catch (Exception e) {
                    System.out.println("⚠ No se pudo guardar: " + datosLibro.titulo());
                }
            }
        }
        System.out.println("✅ Se guardaron " + totalGuardados + " libros nuevos en la base de datos.");
    }
}