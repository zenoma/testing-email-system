package gal.udc.fic.vvs.email.correo;

public class Reenvio extends DecoradorMensaje {

    public Reenvio(MensajeAbstracto mensaje, Correo correo) {
        super(mensaje);
        _correo = correo;
    }

    public int obtenerTamaño() {
        return super.obtenerTamaño() + _correo.obtenerTamaño();
    }

    public String obtenerVisualizacion() {
        return super.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n" + _correo.obtenerVisualizacion() + "\n---- Fin correo reenviado ----";
    }

    private Correo _correo;
}
