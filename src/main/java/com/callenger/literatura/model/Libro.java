package com.callenger.literatura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "Libros")
public class Libro {
    @Id
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private String copyright;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro(){

    }

    public Libro(DatosLibro datosLibro) {
        this.id = datosLibro.id();
        this.titulo = datosLibro.title();
        this.idioma = Idioma.fromString(datosLibro.idiomas().stream()
                .limit(1).collect(Collectors.joining()));;
        this.copyright = datosLibro.copyright();
        this.descargas = datosLibro.descargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return
                "id=" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma + '\'' +
                ", copyright='" + copyright + '\'' +
                ", descargas=" + descargas + '\'' +
                ", autor=" + autor;
    }
}
