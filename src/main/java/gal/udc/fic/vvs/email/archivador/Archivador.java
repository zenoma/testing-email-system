package gal.udc.fic.vvs.email.archivador;

import gal.udc.fic.vvs.email.correo.Correo;

public interface Archivador {

    public String obtenerNombre();
    public boolean almacenarCorreo(Correo correo);
    public int obtenerEspacioTotal();
    public int obtenerEspacioDisponible();
    public Archivador obtenerDelegado();
    public void establecerDelegado(Archivador archivador);

}
