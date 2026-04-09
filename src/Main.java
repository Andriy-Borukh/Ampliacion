import Model.CentroEducativo;
import Service.CentroEducativoService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path ruta = Paths.get("src/centros-educacion.csv");
        CentroEducativoService service = new CentroEducativoService(ruta);

        try {
            List<CentroEducativo> centros = service.cargarCentros();

            System.out.println("Centros cargados: " + centros.size());
            centros.stream()
                    .limit(5)
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}