package gal.udc.fic.vvs.email.archivador;

import gal.udc.fic.vvs.email.correo.Correo;

public class Delegado extends DecoradorArchivador {

    public Delegado(Archivador decorado) {
        super(decorado);
    }

    public boolean almacenarCorreo(Correo correo) {
        if (!super.almacenarCorreo(correo)) {
            return _delegado.almacenarCorreo(correo);
        }
        return true;
    }

    public Archivador obtenerDelegado() {
        return _delegado;
    }

    public void establecerDelegado(Archivador archivador) {
        _delegado = archivador;
    }

    private Archivador _delegado;

}
