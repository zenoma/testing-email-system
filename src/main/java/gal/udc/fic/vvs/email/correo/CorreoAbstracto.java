package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Vector;

public abstract class CorreoAbstracto implements Correo {

    public CorreoAbstracto() {
        _padre = null;
    }

    public String obtenerRuta() {
        if (obtenerPadre() != null) {
            return obtenerPadre().obtenerRuta() + " > " + obtenerPreVisualizacion();
        } else {
            return obtenerPreVisualizacion();
        }
    }

    public Collection explorar() throws OperacionInvalida {
        throw new OperacionInvalida();
    }

    public void añadir(Correo correo) throws OperacionInvalida {
        throw new OperacionInvalida();
    }

    public void eliminar(Correo correo) throws OperacionInvalida {
        throw new OperacionInvalida();
    }

    public Correo obtenerHijo(int n) throws OperacionInvalida {
        throw new OperacionInvalida();
    }

    public Correo obtenerPadre() {
        return _padre;
    }

    protected void establecerPadre(Correo padre) {
        _padre = padre;
    }

    private Correo _padre;

}
