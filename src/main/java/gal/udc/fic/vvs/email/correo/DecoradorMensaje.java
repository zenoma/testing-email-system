package gal.udc.fic.vvs.email.correo;

import java.util.Collection;

public abstract class DecoradorMensaje extends MensajeAbstracto {

    public DecoradorMensaje(MensajeAbstracto decorado) {
        _decorado = decorado;
    }

    public void establecerLeido(boolean leido) {
        _decorado.establecerLeido(leido);
    }

    public int obtenerNoLeidos() {
        return _decorado.obtenerNoLeidos();
    }

    public int obtenerTamaño() {
        return _decorado.obtenerTamaño();
    }

    public Integer obtenerIcono() {
        return _decorado.obtenerIcono();
    }

    public String obtenerPreVisualizacion() {
        return _decorado.obtenerPreVisualizacion();
    }

    public String obtenerVisualizacion() {
        return _decorado.obtenerVisualizacion();
    }

    public String obtenerRuta() {
        return _decorado.obtenerRuta();
    }

    public Collection explorar() throws OperacionInvalida {
        return _decorado.explorar();
    }

    public Collection buscar(String busqueda) {
        Collection resultado = _decorado.buscar(busqueda);
        if (resultado.remove(_decorado)) {
            resultado.add(this);
        }
        return resultado;
    }

    public void añadir(Correo correo) throws OperacionInvalida {
        _decorado.añadir(correo);
    }

    public void eliminar(Correo correo) throws OperacionInvalida {
        _decorado.eliminar(correo);
    }

    public Correo obtenerHijo(int n) throws OperacionInvalida {
        return _decorado.obtenerHijo(n);
    }

    public Correo obtenerPadre() {
        return _decorado.obtenerPadre();
    }

    protected void establecerPadre(Correo padre) {
        _decorado.establecerPadre(padre);
    }

    private MensajeAbstracto _decorado;

}
