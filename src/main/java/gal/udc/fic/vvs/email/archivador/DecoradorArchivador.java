package gal.udc.fic.vvs.email.archivador;

import gal.udc.fic.vvs.email.correo.Correo;

public abstract class DecoradorArchivador implements Archivador {

    public DecoradorArchivador(Archivador decorado) {
        _decorado = decorado;
    }

    public String obtenerNombre() {
        return _decorado.obtenerNombre();
    }

    public boolean almacenarCorreo(Correo correo) {
        return _decorado.almacenarCorreo(correo);
    }

    public int obtenerEspacioTotal() {
        return _decorado.obtenerEspacioTotal();
    }

    public int obtenerEspacioDisponible() {
        return _decorado.obtenerEspacioDisponible();
    }

    public Archivador obtenerDelegado() {
        return _decorado.obtenerDelegado();
    }

    public void establecerDelegado(Archivador archivador) {
        _decorado.establecerDelegado(archivador);
    }

    private Archivador _decorado;

}
