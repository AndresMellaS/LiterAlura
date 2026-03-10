package com.example.literalura.model;

import com.example.literalura.dto.DatosAutor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;
    private Integer añoNacimiento;
    private Integer añoFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Libro> libros = new ArrayList<>();

    public Autor (){}

    public Autor (DatosAutor datosAutor) {

        this.nombre = datosAutor.nombre ();
        this.añoNacimiento = datosAutor.añoNacimiento ();
        this.añoFallecimiento = datosAutor.añoFallecimiento ();

    }
    public Long getId() { return id; }
    public String getNombre () { return nombre; }
    public void setNombre (String nombre) { this.nombre = nombre; }
    public Integer getAñoNacimiento () { return añoNacimiento; }
    public void setAñoNacimiento (Integer añoNacimiento) {this.añoNacimiento = añoNacimiento; }
    public Integer getAñoFallecimiento () { return añoFallecimiento; }
    public void setAñoFallecimiento (Integer añoFallecimiento) {this.añoFallecimiento = añoFallecimiento; }
    public List<Libro> getLibros() { return libros;}
    public void setLibros (List<Libro> libros) { this.libros = libros; }

    @Override
    public String toString () {
        String nacimiento = añoNacimiento != null? String.valueOf(añoNacimiento) : "Desconocido";
        String fallecimiento = añoFallecimiento != null? String.valueOf(añoFallecimiento) : "Desconocido";
        return """

                 ----Autor-----

                 Nombre: %s
                 Año de nacimiento: %s
                 Año de fallecimiento: %s
                 """.formatted(nombre, nacimiento, fallecimiento);

    }

}
