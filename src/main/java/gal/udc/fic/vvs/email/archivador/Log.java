package gal.udc.fic.vvs.email.archivador;

import gal.udc.fic.vvs.email.correo.Correo;

public class Log extends DecoradorArchivador {

    public Log(Archivador decorado) {
        super(decorado);
    }

    public boolean almacenarCorreo(Correo correo) {
        boolean resultado = super.almacenarCorreo(correo);
        System.out.println("Mensaxe de log");
        return resultado;
    }

}
