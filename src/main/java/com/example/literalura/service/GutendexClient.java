package com.example.literalura.service;

import com.example.literalura.dto.DatosLibro;
import com.example.literalura.dto.DatosResultados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

@Service
public class GutendexClient {

    private static final String URL_BASE = "https://gutendex.com/books/";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<DatosLibro> buscarPrimerLibroPorTitulo(String titulo) {
        String json = get(URL_BASE + "?search=" + titulo.replace(" ", "+"));
        DatosResultados datos = parsear(json);
        return datos.resultados().stream()
                .filter(l -> !l.autores().isEmpty() && !l.idiomas().isEmpty())
                .findFirst();
    }

    public List<DatosLibro> obtenerLibrosPorPagina(int pagina) {
        String json = get(URL_BASE + "?page=" + pagina);
        return parsear(json).resultados();
    }

    private String get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al conectar con Gutendex: " + e.getMessage());
        }
    }

    private DatosResultados parsear(String json) {
        try {
            return objectMapper.readValue(json, DatosResultados.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar respuesta de Gutendex: " + e.getMessage());
        }
    }
}
