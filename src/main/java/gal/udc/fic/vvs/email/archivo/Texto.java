package gal.udc.fic.vvs.email.archivo;

public class Texto extends Archivo {

    public Texto(String nombre, String contenido) {
        super(nombre, contenido);
    }

    protected String obtenerMimeType() {
        return "text/plain";
    }

}
