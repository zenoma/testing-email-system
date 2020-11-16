package gal.udc.fic.vvs.email.archivador;

import java.util.Vector;
import gal.udc.fic.vvs.email.correo.Correo;

public class ArchivadorSimple implements Archivador {

    public ArchivadorSimple(String nombre, int espacio) {
        _nombre = nombre;
        _espacioTotal = espacio;
        _espacioDisponible = espacio;
        _correos = new Vector();
    }

    public String obtenerNombre() {
        return _nombre;
    }

    public boolean almacenarCorreo(Correo correo) {
        if (correo.obtenerTamaño() < _espacioDisponible) {
            _correos.addElement(correo);
            _espacioDisponible -= correo.obtenerTamaño();
            return true;
        }
        return false;
    }

    public int obtenerEspacioTotal() {
        return _espacioTotal;
    }

    public int obtenerEspacioDisponible() {
        return _espacioDisponible;
    }

    public Archivador obtenerDelegado() {
        return null;
    }

    public void establecerDelegado(Archivador archivador) {
    }

    private String _nombre;
    private int _espacioTotal, _espacioDisponible;
    private Vector _correos;

}
