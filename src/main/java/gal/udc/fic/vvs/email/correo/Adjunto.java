package gal.udc.fic.vvs.email.correo;

import gal.udc.fic.vvs.email.archivo.Archivo;

public class Adjunto extends DecoradorMensaje {

    public Adjunto(MensajeAbstracto mensaje, Archivo archivo) {
        super(mensaje);
        _archivo = archivo;
    }

    public int obtenerTamaño() {
        return super.obtenerTamaño() + _archivo.obtenerTamaño();
    }

    public String obtenerVisualizacion() {
        return super.obtenerVisualizacion() + "\n\nAdxunto: " + _archivo.obtenerPreVisualizacion();
    }

    private Archivo _archivo;
}
