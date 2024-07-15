package com.callenger.literatura.repository;

import com.callenger.literatura.model.Autor;
import com.callenger.literatura.model.Idioma;
import com.callenger.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibreriaRepository extends JpaRepository {

    Optional <Autor> findAutorByNombreContaining(String nombre);

    @Query("SELECT l FROM Libro l JOIN l.autor WHERE l.titulo ILIKE %:lTitulo%")
    Optional<Libro> obtenerLibroContainsEqualsIgnoreCaseTitulo(String lTitulo);

    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> findLibrosByAutor();

    @Query("SELECT a FROM Autor a WHERE a.fechaDeFallecimiento > :fecha")
    List<Autor> getAutorByFechaDeFallecimiento(Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> findLibroByIdioma(Idioma idioma);
}
