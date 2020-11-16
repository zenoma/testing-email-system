package gal.udc.fic.vvs.email.correo;

import java.util.Collection;

public interface Correo {

    public void establecerLeido(boolean leido);
    public int obtenerNoLeidos();
    public int obtenerTamaño();
    public Integer obtenerIcono();
    public String obtenerPreVisualizacion();
    public String obtenerVisualizacion();
    public String obtenerRuta();
    public Collection explorar() throws OperacionInvalida;
    public Collection buscar(String busqueda);

    public void añadir(Correo correo) throws OperacionInvalida;
    public void eliminar(Correo correo) throws OperacionInvalida;
    public Correo obtenerHijo(int n) throws OperacionInvalida;
    public Correo obtenerPadre();

    public static final Integer ICONO_CARPETA = new Integer(1);
    public static final Integer ICONO_MENSAJE = new Integer(2);
    public static final Integer ICONO_NUEVO_MENSAJE = new Integer(3);
    public static final Integer ICONO_MENSAJE_CON_ADJUNTOS = new Integer(4);
    public static final Integer ICONO_NUEVO_MENSAJE_CON_ADJUNTOS = new Integer(5);

}
