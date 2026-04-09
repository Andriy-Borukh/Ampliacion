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
        return tipoCentro;
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
