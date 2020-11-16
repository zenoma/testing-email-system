package gal.udc.fic.vvs.email.correo;

import java.util.Vector;
import java.util.Collection;

public class Carpeta extends CorreoAbstracto {

    public Carpeta(String nombre) {
        _nombre = nombre;
        _hijos = new Vector();
    }

    public void establecerLeido(boolean leido) {
        try {
            for (int i = 0; i < _hijos.size(); i++) {
                obtenerHijo(i).establecerLeido(leido);
            }
        } catch (OperacionInvalida e) { }
    }

    public int obtenerNoLeidos() {
        int resultado = 0;
        try {
            for (int i = 0; i < _hijos.size(); i++) {
                resultado += obtenerHijo(i).obtenerNoLeidos();
            }
        } catch (OperacionInvalida e) { }
        return resultado;
    }

    public int obtenerTamaño() {
        int resultado = 0;
        try {
            for (int i = 0; i < _hijos.size(); i++) {
                resultado += obtenerHijo(i).obtenerTamaño();
            }
        } catch (OperacionInvalida e) { }
        return resultado;
    }

    public Integer obtenerIcono() {
        return Correo.ICONO_CARPETA;
    }

    public String obtenerPreVisualizacion() {
        return obtenerVisualizacion();
    }

    public String obtenerVisualizacion() {
        String resultado = _nombre;
        if (obtenerNoLeidos() > 0) {
            resultado += " (" + obtenerNoLeidos() + ")";
        }
        return resultado;
    }

    public Collection explorar() throws OperacionInvalida {
        return _hijos;
    }

    public Collection buscar(String busqueda) {
        Vector resultado = new Vector();
        try {
            for (int i = 0; i < _hijos.size(); i++) {
                resultado.addAll(obtenerHijo(i).buscar(busqueda));
            }
        } catch (OperacionInvalida e) { }
        return resultado;
    }

    public void añadir(Correo correo) throws OperacionInvalida {
	if (correo.obtenerPadre() != null) {
	    correo.obtenerPadre().eliminar(correo);
	}
        ((CorreoAbstracto) correo).establecerPadre(this);
        _hijos.addElement(correo);
    }

    public void eliminar(Correo correo) throws OperacionInvalida {
        if (_hijos.removeElement(correo)) {
            ((CorreoAbstracto) correo).establecerPadre(null);
        }
    }

    public Correo obtenerHijo(int n) throws OperacionInvalida {
	return ((Correo)_hijos.elementAt(n));

    }

    private Vector _hijos;
    private String _nombre;

}
