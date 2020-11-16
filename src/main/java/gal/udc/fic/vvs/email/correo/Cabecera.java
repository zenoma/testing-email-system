package gal.udc.fic.vvs.email.correo;

public class Cabecera extends DecoradorMensaje {

    public Cabecera(MensajeAbstracto mensaje, String nombre, String valor) {
        super(mensaje);
        _nombre = nombre;
        _valor = valor;
    }

    public int obtenerTamaño() {
        return super.obtenerTamaño() + _nombre.length() + _valor.length();
    }


    public String obtenerVisualizacion() {
        return _nombre + ": " + _valor + "\n" + super.obtenerVisualizacion();
    }

    String _nombre;
    String _valor;

}
