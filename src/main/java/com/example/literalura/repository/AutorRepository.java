package com.example.literalura.repository;

import com.example.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);

    @Query("""
            SELECT a FROM Autor a
            WHERE a.añoNacimiento IS NOT NULL
            AND a.añoFallecimiento IS NOT NULL
            AND a.añoNacimiento <= :año
            AND a.añoFallecimiento >= :año
            """)
    List<Autor> findAutoresVivosEnAño(@Param("año") Integer año);
}