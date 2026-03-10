package com.example.literalura.model;

import com.example.literalura.dto.DatosLibro;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String titulo;
    private String idioma;
    private Integer numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    @JsonBackReference
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibro datoslibro, Autor autor) {
        this.titulo = datoslibro.titulo();
        this.idioma = datoslibro.idiomas().get(0);
        this.numeroDescargas = datoslibro.numeroDescargas();
        this.autor = autor;


    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public Integer getNumeroDescargas() { return numeroDescargas; }
    public void setNumeroDescargas(Integer numeroDescargas) { this.numeroDescargas = numeroDescargas; }
    public Autor getAutor() { return autor; }
    public void setAutor (Autor autor) { this.autor = autor; }


    @Override
    public String toString() {
        return """
                ---- LIBRO ----
                Título: %s
                Autor: %s
                Idioma: %s
                Descargas: %d
                """.formatted(titulo, autor.getNombre(), idioma, numeroDescargas);
    }


}