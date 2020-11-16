package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Iterator;

public class CarpetaLimitada extends CorreoAbstracto {

    public CarpetaLimitada(Carpeta carpeta, int tamaño) {
        _carpeta = carpeta;
        _tamaño = tamaño;
    }

    public void establecerLeido(boolean leido) {
        _carpeta.establecerLeido(leido);
    }

    public int obtenerNoLeidos() {
        return _carpeta.obtenerNoLeidos();
    }

    public int obtenerTamaño() {
        return _carpeta.obtenerTamaño();
    }

    public Integer obtenerIcono() {
        return _carpeta.obtenerIcono();
    }

    public String obtenerPreVisualizacion() {
        return _carpeta.obtenerPreVisualizacion();
    }

    public String obtenerVisualizacion() {
        return _carpeta.obtenerVisualizacion();
    }

    public String obtenerRuta() {
        return _carpeta.obtenerRuta();
    }

    public Collection explorar() throws OperacionInvalida {
        return _carpeta.explorar();
    }

    public Collection buscar(String busqueda) {
        Collection resultado = _carpeta.buscar(busqueda);
        if (resultado.remove(_carpeta)) {
            resultado.add(this);
        }
        Iterator iResultado = resultado.iterator();
        for(int i=0; iResultado.hasNext(); i++) {
            iResultado.next();
            if (i > _tamaño) {
                iResultado.remove();
            }
        }
        return resultado;
    }

    public void añadir(Correo correo) throws OperacionInvalida {
        _carpeta.añadir(correo);
    }

    public void eliminar(Correo correo) throws OperacionInvalida {
        _carpeta.eliminar(correo);
    }

    public Correo obtenerHijo(int n) throws OperacionInvalida {
        return _carpeta.obtenerHijo(n);
    }

    public Correo obtenerPadre() {
        return _carpeta.obtenerPadre();
    }

    protected void establecerPadre(Correo padre) {
        _carpeta.establecerPadre(padre);
    }

    private Carpeta _carpeta;
    private int _tamaño;

}
