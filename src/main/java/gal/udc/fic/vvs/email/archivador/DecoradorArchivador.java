package gal.udc.fic.vvs.email.archivador;

import gal.udc.fic.vvs.email.correo.Correo;

public abstract class DecoradorArchivador implements Archivador {

	public DecoradorArchivador(Archivador decorado) {
		_decorado = decorado;
	}

	@Override
	public String obtenerNombre() {
		return _decorado.obtenerNombre();
	}

	@Override
	public boolean almacenarCorreo(Correo correo) {
		return _decorado.almacenarCorreo(correo);
	}

	@Override
	public int obtenerEspacioTotal() {
		return _decorado.obtenerEspacioTotal();
	}

	@Override
	public int obtenerEspacioDisponible() {
		return _decorado.obtenerEspacioDisponible();
	}

	@Override
	public Archivador obtenerDelegado() {
		return _decorado.obtenerDelegado();
	}

	@Override
	public void establecerDelegado(Archivador archivador) {
		_decorado.establecerDelegado(archivador);
	}

	private Archivador _decorado;

}
