package gal.udc.fic.vvs.email.archivo;

public class Imagen extends Archivo {

    public Imagen(String nombre, String contenido) {
        super(nombre, contenido);
    }

    protected String obtenerMimeType() {
        return "image/png";
    }

}
