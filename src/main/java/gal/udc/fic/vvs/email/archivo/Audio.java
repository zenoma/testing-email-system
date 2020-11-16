package gal.udc.fic.vvs.email.archivo;

public class Audio extends Archivo {

    public Audio(String nombre, String contenido) {
        super(nombre, contenido);
    }

    protected String obtenerMimeType() {
        return "audio/ogg";
    }

}
