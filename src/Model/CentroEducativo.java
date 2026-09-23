package Model;

public class CentroEducativo {

    private final String id;
    private final String nombre;
    private final String email;
    private final String tipoVia;
    private final String nombreVia;
    private final String telefono;
    private final String url;
    private final String tipoCentro;

    public CentroEducativo(String id,
                           String nombre,
                           String tipoCentro,
                           String tipoVia,
                           String nombreVia,
                           String telefono,
                           String url,
                           String email) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCentro = tipoCentro;
        this.tipoVia = tipoVia;
        this.nombreVia = nombreVia;
        this.telefono = telefono;
        this.url = url;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTipoVia() {
        return tipoVia;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUrl() {
        return url;
    }

    public String getTipoCentro() {
        String t = tipoCentro;

        //elimina las utlimas comillas
        if (t.endsWith("\"") && t.length() > 1) {
            t = t.substring(0, t.length() - 1);
        }

        //extrae la ultima palabra que es la que contiene el tipo de centro
        int ultimaPalabra = t.lastIndexOf("/");
        t = t.substring(ultimaPalabra + 1);

        return t;
    }

    public String getCategoriaCentro() {
        if (tipoCentro == null) {
            return "DESCONOCIDO";
        }
        String t = tipoCentro.toLowerCase();

        if (t.contains("colegiospublicos")) {
            return "PUBLICO";
        }
        if (t.contains("colegiosprivadosconcertados") || t.contains("colegiosprivados")) {
            return "PRIVADO_CONCERTADO";
        }
        if (t.contains("centroseducacionadultos")) {
            return "ADULTOS";
        }
        if (t.contains("escuelasinfantiles")) {
            return "INFANTIL";
        }

        return "OTRO";
    }

    @Override
    public String toString() {
        return "CentroEducativo{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", tipoVia='" + tipoVia + '\'' +
                ", nombreVia='" + nombreVia + '\'' +
                ", telefono='" + telefono + '\'' +
                ", url='" + url + '\'' +
                ", tipoCentro='" + tipoCentro + '\'' +
                '}';
    }
}
