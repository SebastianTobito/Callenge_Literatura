package com.callenger.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(@JsonAlias("id") Long id,
                         @JsonAlias("title") String title,
                         @JsonAlias("authors")List<DatosAutor> autores,
                         @JsonAlias("languages")List<String> idiomas,
                         @JsonAlias("copyright") String copyright,
                         @JsonAlias("dowload_count")Integer descargas){
}
