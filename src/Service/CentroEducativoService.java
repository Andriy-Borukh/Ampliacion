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
        String[] nombresCampo = splitLinea(cabecera);

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
        String[] campos = splitLinea(linea);

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

        return campos[indice].trim();
    }

    //Esta funcion sirve para separar bien las lineas ya que hay campos que tienen dobles comillas y el programa separa
    // mal las cosas
    private String[] splitLinea(String linea) {
        //Lista de los campos completos
        List<String> campos = new ArrayList<>();
        //String que se acumula al leer la linea debido a que hay campos con ; y tengo que identificar
        // que ; es un separador y cual no
        StringBuilder actual = new StringBuilder();
        //Para saber si esta dentro de comillas
        boolean dentroComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);

            if (c == '"') {
                //Si entramos dentro de comillas cambiamos el estado y empezamos a guardar caracter por caracter
                // el contenido del campo
                dentroComillas = !dentroComillas;
                actual.append(c);
            } else if (c == ';' && !dentroComillas) {
                //Si no esta dentro de comillas y se encuentra un ; significa que es un separador
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                //En cambio si hay ; dentro de comillas significa que no es un separador
                actual.append(c);
            }
        }
        //Para añadir el úlitmo campo
        campos.add(actual.toString());

        return campos.toArray(new String[0]);
    }

}
