package com.example.literalura.principal;

import com.example.literalura.model.Autor;
import com.example.literalura.model.Libro;
import com.example.literalura.service.AutorService;
import com.example.literalura.service.LibroService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {

    private final LibroService libroService;
    private final AutorService autorService;
    private final Scanner scanner = new Scanner(System.in);

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    \n =======Literatura========
                    1-Buscar libro por título
                    2-Listar todos los libros
                    3-Listar todos los autores
                    4-Listar autores vivos en un año
                    5-Listar libros por idioma
                    6-Top 10 libros más descargados
                    7-Cargar libros populares desde la API
                    0-Salir
                    ============================
                    Elige una opción:""");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Opción inválida, ingresa un número.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos();
                case 5 -> listarPorIdioma();
                case 6 -> top10();
                case 7 -> libroService.cargarLibrosDesdeAPI();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("⚠ Opción no válida.");
            }
        }
    }

    private void buscarLibro() {
        System.out.print("Ingresa el título del libro: ");
        String titulo = scanner.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("⚠ El título no puede estar vacío.");
            return;
        }

        try {
            Optional<Libro> libro = libroService.buscarYGuardarLibro(titulo);
            libro.ifPresentOrElse(
                    l -> System.out.println("\nLibro encontrado:\n" + l),
                    () -> System.out.println("\n❌ Libro no encontrado en la API.")
            );
        } catch (RuntimeException e) {
            System.out.println("❌ Error al conectar con la API: " + e.getMessage());
        }
    }

    private void listarLibros() {
        List<Libro> libros = libroService.listarTodosLosLibros();
        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros registrados aún.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorService.listarTodosLosAutores();
        if (autores.isEmpty()) {
            System.out.println("📭 No hay autores registrados aún.");
        } else {
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.print("Ingresa el año a consultar: ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("⚠ Debes ingresar un año.");
            return;
        }

        try {
            int año = Integer.parseInt(input);
            if (año < 0 || año > 2025) {
                System.out.println("⚠ Ingresa un año válido entre 0 y 2025.");
                return;
            }
            List<Autor> autores = autorService.listarAutoresVivosEnAño(año);
            if (autores.isEmpty()) {
                System.out.println("📭 No se encontraron autores vivos en " + año + ".");
            } else {
                autores.forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠ Año inválido, ingresa solo números.");
        }
    }

    private void listarPorIdioma() {
        System.out.println("""
                IDIOMAS DISPONIBLES:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                zh - Chino
                Ingresa el código del idioma:""");

        String idioma = scanner.nextLine().trim().toLowerCase();
        List<String> idiomasValidos = List.of("es", "en", "fr", "pt", "zh");

        if (idioma.isEmpty() || !idiomasValidos.contains(idioma)) {
            System.out.println("⚠ Código de idioma no válido. Usa: es, en, fr, pt o zh.");
            return;
        }

        List<Libro> libros = libroService.listarLibrosPorIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros en ese idioma.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    private void top10() {
        List<Libro> top = libroService.top10LibrosMasDescargados();
        if (top.isEmpty()) {
            System.out.println("📭 No hay libros registrados aún. Usa la opción 7 para cargar desde la API.");
        } else {
            System.out.println("\n🏆 TOP 10 LIBROS MÁS DESCARGADOS:");
            top.forEach(System.out::println);
        }
    }
}







