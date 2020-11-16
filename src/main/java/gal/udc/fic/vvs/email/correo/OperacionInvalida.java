package gal.udc.fic.vvs.email.correo;

public class OperacionInvalida extends Exception {

    public OperacionInvalida() {
    }

    public OperacionInvalida(String descripcion) {
        super(descripcion);
    }

}
