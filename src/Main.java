import Model.CentroEducativo;
import Service.CentroEducativoService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Path ruta = Paths.get("src/centros-educacion.csv");
        CentroEducativoService service = new CentroEducativoService(ruta);

        try {
            List<CentroEducativo> centros = service.cargarCentros();
            System.out.println("Total centros cargados: " + centros.size());

            ejecutarConsultas(centros);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ejecutarConsultas(List<CentroEducativo> centros) {

        // Interfaces funcionales reutilizables
        Predicate<CentroEducativo> tieneEmail =
                c -> c.getEmail() != null && !c.getEmail().isBlank();

        Predicate<CentroEducativo> tieneTelefono =
                c -> c.getTelefono() != null && !c.getTelefono().isBlank();

        Predicate<CentroEducativo> tieneNombre =
                c -> c.getNombre() != null && !c.getNombre().isBlank();

        Predicate<CentroEducativo> tieneNombreVia =
                c -> c.getNombreVia() != null && !c.getNombreVia().isBlank();

        Predicate<CentroEducativo> esPublico =
                c -> "PUBLICO".equalsIgnoreCase(c.getCategoriaCentro());

        Predicate<CentroEducativo> esPrivadoConcertado =
                c -> "PRIVADO_CONCERTADO".equalsIgnoreCase(c.getCategoriaCentro());

        Function<CentroEducativo, String> aTipoCentro = CentroEducativo::getTipoCentro;

        Consumer<CentroEducativo> imprimirCentro = System.out::println;

        Supplier<Stream<CentroEducativo>> streamCentros =
                centros::stream;

        UnaryOperator<String> aMayusculas =
                String::toUpperCase;


        // 1) filter, map, sorted, limit, skip, distinct + forEach, count

        System.out.println("\n--- Primeros 5 centros con email (ordenados por nombre) ---");
        streamCentros.get()
                .filter(tieneEmail)
                .sorted(Comparator.comparing(CentroEducativo::getNombre))
                .limit(5)
                .forEach(imprimirCentro);

        long conTelefono = streamCentros.get()
                .filter(tieneTelefono)
                .count();
        System.out.println("\nCentros con teléfono: " + conTelefono);

        System.out.println("\n--- Centros del 11 al 15 ordenados por nombre de vía ---");
        streamCentros.get()
                .filter(tieneNombreVia)
                .sorted(Comparator.comparing(CentroEducativo::getNombreVia))
                .skip(10)// skip
                .limit(5)
                .forEach(imprimirCentro);

        System.out.println("\n--- Distintos tipos de centro ---");
        streamCentros.get()
                .map(aTipoCentro)
                .filter(Objects::nonNull)
                .map(aMayusculas)
                .distinct()
                .sorted()
                .forEach(System.out::println);


        // 2) findFirst, findAny, anyMatch, allMatch, noneMatch

        System.out.println("\n--- Primer centro público que tenga email ---");
        streamCentros.get()
                .filter(esPublico.and(tieneEmail))
                .findFirst()
                .ifPresentOrElse(
                        c -> System.out.println("Encontrado: " + c),
                        () -> System.out.println("No se encontró ninguno")
                );

        System.out.println("\n--- ¿Hay algún centro sin teléfono pero con email? ---");
        boolean algunoSinTelConEmail = streamCentros.get()
                .anyMatch(tieneEmail.and(tieneTelefono.negate()));
        System.out.println("Resultado: " + algunoSinTelConEmail);

        System.out.println("\n--- ¿Todos los centros tienen nombre? ---");
        boolean todosConNombre = streamCentros.get()
                .allMatch(tieneNombre);
        System.out.println("Resultado: " + todosConNombre);

        System.out.println("\n--- ¿Ningún centro tiene tipo 'INEXISTENTE'? ---");
        boolean ningunoTipoRaro = streamCentros.get()
                .noneMatch(c -> "DESCONOCIDO".equalsIgnoreCase(c.getCategoriaCentro()));
        System.out.println("Resultado: " + ningunoTipoRaro);


        // 3) reduce

        System.out.println("\n--- Número de centros públicos ---");
        long totalPublicos = streamCentros.get()
                .filter(esPublico)
                .map(c -> 1L)
                .reduce(0L, Long::sum);
        System.out.println("Total centros públicos: " + totalPublicos);

        System.out.println("\n-- Número de centros privados/concertados --");
        long totalPrivadosConcertados = streamCentros.get()
                .filter(esPrivadoConcertado)
                .map(c -> 1L)
                .reduce(0L, Long::sum);
        System.out.println("Total centros privados/concertados: " + totalPrivadosConcertados);


        // 4) collect + estadísticas por tipo de centro

        System.out.println("\n--- Número de centros por tipo (top 5) ---");
        Map<String, Long> centrosPorTipo = streamCentros.get()
                .map(aTipoCentro)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        centrosPorTipo.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));


        // 5) toList(), toArray()

        System.out.println("\n--- Lista de centros públicos ---");
        List<CentroEducativo> listaPublicos = streamCentros.get()
                .filter(esPublico)
                .toList();
        System.out.println("Total públicos : " + listaPublicos.size());

        System.out.println("\n--- Array con los primeros 10 centros ---");
        CentroEducativo[] arrayPrimeros10 = streamCentros.get()
                .limit(10)
                .toArray(CentroEducativo[]::new);
        System.out.println("Longitud del array: " + arrayPrimeros10.length);
    }
}