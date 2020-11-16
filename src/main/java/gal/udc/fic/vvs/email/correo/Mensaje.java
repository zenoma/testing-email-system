package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Vector;
import gal.udc.fic.vvs.email.archivo.Texto;

public class Mensaje extends MensajeAbstracto {

    public Mensaje(Texto contenido) {
        _contenido = contenido;
        _leido = false;
    }

    public void establecerLeido(boolean leido) {
        _leido = leido;
    }

    public int obtenerNoLeidos() {
        if(_leido) {
            return 0;
        } else {
            return 1;
        }
    }

    public int obtenerTamaño() {
        return _contenido.obtenerTamaño();
    }

    public Integer obtenerIcono() {
        if(_leido) {
            return Correo.ICONO_MENSAJE;
        } else {
            return Correo.ICONO_NUEVO_MENSAJE;
        }
    }

    public String obtenerPreVisualizacion() {
        String visualizacion = obtenerVisualizacion();
        return visualizacion.substring(0, Math.min(visualizacion.length(), TAMAÑO_PREVISUALIZACION)) + "...";
    }

    public String obtenerVisualizacion() {
        return _contenido.obtenerContenido();
    }

    public Collection buscar(String busqueda) {
        Vector resultado = new Vector();
        if (obtenerVisualizacion().toLowerCase().matches(".*" + busqueda.toLowerCase() + ".*")) {
            resultado.addElement(this);
        }
        return resultado;
    }

    private final static int TAMAÑO_PREVISUALIZACION = 32;
    private boolean _leido;
    private Texto _contenido;

}
