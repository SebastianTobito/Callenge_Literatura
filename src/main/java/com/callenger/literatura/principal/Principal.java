package com.callenger.literatura.principal;

import com.callenger.literatura.model.*;
import com.callenger.literatura.repository.LibreriaRepository;
import com.callenger.literatura.service.ConsumoAPI;
import com.callenger.literatura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos convertidor = new ConvierteDatos();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_SEARCH = "?search=";
    private final LibreriaRepository repository;

    public Principal(LibreriaRepository repository) {
        this.repository = repository;
    }

    public void mostrarMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libros por titulo
                    2 - Buscar libros registrados
                    3 - Mostrar autores registrados
                    4 - Buscar autores en un año en especifico
                    5 - Mostrar libros por idioma 
                                     
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();
            try {
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        buscarTodosLosLibros();
                        break;
                    case 3:
                        mostrarTodosLosAutores();
                        break;
                    case 4:
                        buscarAutoresPorAno();
                        break;
                    case 5:
                        buscarLibrosPorIdioma();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opción invalida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción invalida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que desea buscar");
        var libroTitulo = scanner.nextLine().replace(" ", "%20");
        var json = consumoApi.obtenerDatos(URL_BASE + URL_SEARCH + libroTitulo);
        var libroData = convertidor.obtenerDatos(json, Datos.class);

        Optional<DatosLibro> libroBuscado = libroData.libros().stream()
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println(
                    "\nLibro\n" +
                            "Titulo: " + libroBuscado.get().title() +
                            "\n Autor: " + libroBuscado.get().autores().stream()
                            .map(a -> a.nombre())
                            .limit(1)
                            .collect(Collectors.joining()) +
                            "\n Idioma: " + libroBuscado.get().idiomas().stream()
                            .collect(Collectors.joining()) +
                            "\n Numero de descargas: " + libroBuscado.get().descargas());
            try {
                List<Libro> libroGuardado;
                libroGuardado = libroBuscado.stream()
                        .map(a -> new Libro(a)).collect(Collectors.toList());
                Autor autorApi = libroBuscado.stream()
                        .flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream()
                        .findFirst()
                        .get();
                Optional<Autor> autorBaseDatos = repository.findAutorByNombreContaining(libroBuscado.get().autores().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));
                Optional<Libro> libroOptional = repository.obtenerLibroContainsEqualsIgnoreCaseTitulo(libroTitulo);

                if (libroOptional.isPresent()) {
                    System.out.println("El libro ya está guardado en la base de datos");
                } else {
                    Autor autor;
                    if (autorBaseDatos.isPresent()) {
                        autor = autorBaseDatos.get();
                        System.out.println("El autor ya está guardado en l base de datos");
                    } else {
                        autor = autorApi;
                        repository.save(autor);
                    }
                    autor.setLibros(libroGuardado);
                    repository.save(autor);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void buscarTodosLosLibros() {
        List<Libro> libros = repository.findLibrosByAutor();
        libros.forEach(l -> System.out.println(
                "Libro" +
                        "\nTitulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getIdioma().getIdioma() +
                        "\nNumero de descargas: " + l.getDescargas()
        ));
    }

    private void mostrarTodosLosAutores() {
List<Autor> autores = repository.findAll();
autores.forEach(l-> System.out.println( "Autor" +
        "\nAutor: " + l.getNombre() +
        "\nFecha de nacimiento: " + l.getFechaDeNacimiento()+
        "\nFecha de fallecimiento: " + l.getFechaDeFallecimiento() +
        "\nLibros: " + l.getLibros().stream()
        .map(t -> t.getTitulo()).collect(Collectors.toList())));
    }

    private void buscarAutoresPorAno() {
        System.out.println("Ingrese el año del cual quire buscar el autor");
        try{
            var fecha = Integer.valueOf(scanner.nextLine());
            List<Autor> autores = repository.getAutorByFechaDeFallecimiento(fecha);
            if (!autores.isEmpty()){
                autores.forEach(l-> System.out.println("Autor" +
                        "\nAutor: " + l.getNombre() +
                        "\nFecha de nacimiento: " + l.getFechaDeNacimiento() +
                        "\nFecha de fallecimiento: " + l.getFechaDeFallecimiento() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo())
                        .collect(Collectors.toList())));
            }else{
                System.out.println("No se pudo encontrar el autor en el año" +fecha);
            }
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    private void buscarLibrosPorIdioma() {
        var mapIdiomas = """
                Escoja el lenguaje para buscar los libros
                
                en - Ingles
                es - Español
                fr - Frances
                it - Italiano
                pt- Portugues
                
                """;
        System.out.println(mapIdiomas);
        var lengua = scanner.nextLine().toLowerCase();
        if (lengua.equalsIgnoreCase("es") || lengua.equalsIgnoreCase("en") || lengua.equalsIgnoreCase("fr") || lengua.equalsIgnoreCase("it") || lengua.equalsIgnoreCase("pt")) {
            Idioma idioma = Idioma.fromString(lengua);
            List<Libro> libros = repository.findLibroByIdioma(idioma);
            if (libros.isEmpty()){
                System.out.println("No tenemos libros en este idioma");
            }else{
                libros.forEach(t-> System.out.println("Libro"+
                                "\nTitle: " + t.getTitulo() +
                                "\nAuthor: " + t.getAutor().getNombre() +
                                "\nLanguage: " + t.getIdioma().getIdioma() +
                                "\nNumber of downloads: " + t.getDescargas()));
            }
        }else{
            System.out.println("De un lenguaje valido");
        }

    }
}
