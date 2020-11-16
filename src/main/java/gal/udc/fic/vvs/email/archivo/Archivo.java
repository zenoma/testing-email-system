package gal.udc.fic.vvs.email.archivo;

public abstract class Archivo {

    public Archivo(String nombre, String contenido) {
        _nombre = nombre;
        _contenido = contenido;
    }

    public String obtenerNombre() {
        return _nombre;
    }

    public String obtenerContenido() {
        return _contenido;
    }

    public int obtenerTamaño() {
        return _contenido.length();
    }

    public String obtenerPreVisualizacion() {
        return _nombre + "(" + obtenerTamaño() + " bytes, " + obtenerMimeType() + ")";
    }

    protected abstract String obtenerMimeType();

    private String _nombre;
    private String _contenido;

}
