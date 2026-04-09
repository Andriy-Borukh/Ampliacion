package Service;

import Model.CentroEducativo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CentroEducativoService {

    private final Path ruta;

    public CentroEducativoService(Path ruta) {
        this.ruta = ruta;
    }

    //Devuelve la lista de centros
    public List<CentroEducativo> cargarCentros() throws IOException {
        List<String> lineas = Files.readAllLines(ruta, StandardCharsets.ISO_8859_1);

        //Si la linea que lee está vacía devuevle un arrayList vacío
        if (lineas.isEmpty()) {
            return List.of();
        }

        //La primera línea del csv en el que se encuentran los campos que contienen los centros
        String cabecera = lineas.getFirst();
        //Separa el nombre de los campos en un array de Strings
        String[] nombresCampo = cabecera.split(";", -1);

        //Construye un map en el que se guarda el nombre del campo con su posición
        Map<String, Integer> indicePorNombre = MapIndices(nombresCampo);

        //Devuelve todos los centros saltando la primera línea y con los campos que queremos mostrar
        return lineas.stream()
                .skip(1)
                .map(linea -> parse(linea, indicePorNombre))
                .collect(Collectors.toList());
    }

    //Convierte una línea del csv en un objeto CentroEducativo
    private CentroEducativo parse(String linea, Map<String, Integer> indicePorNombre) {
        //Separa los campos
        String[] campos = linea.split(";", -1);

        //Guarda en variables el contenido del campo correspondiente
        String id = getCampos(campos, indicePorNombre, "PK");
        String nombre = getCampos(campos, indicePorNombre, "NOMBRE");
        String url = getCampos(campos, indicePorNombre, "CONTENT-URL");
        String tipoVia = getCampos(campos, indicePorNombre, "CLASE-VIAL");
        String nombreVia = getCampos(campos, indicePorNombre, "NOMBRE-VIA");
        String telefono = getCampos(campos, indicePorNombre, "TELEFONO");
        String email = getCampos(campos, indicePorNombre, "EMAIL");
        String tipoCentro = getCampos(campos, indicePorNombre, "TIPO");

        return new CentroEducativo(id,nombre, tipoCentro, tipoVia, nombreVia, telefono, url, email);
    }

    //Construye el map para saber que posicion tiene cada campo
    private Map<String, Integer> MapIndices (String[] campos) {
        Map<String, Integer> indicePorNombre = new HashMap<>();
        for (int i = 0; i < campos.length; i++) {
            String nombre = campos[i].trim();
            if (!nombre.isEmpty())
                indicePorNombre.put(nombre, i);
        }
        return indicePorNombre;
    }

    //Devuelve el contenido del campo
    private String getCampos(String[] campos, Map<String, Integer> indicePorNombre, String nombreCampo) {
        Integer indice = indicePorNombre.get(nombreCampo);
        if (indice == null || indice < 0 || indice >= campos.length)
            return "";

        return campos[indice];
    }

}
