package gal.udc.fic.vvs.email.correo;

import java.util.Collection;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public abstract class DecoradorMensaje extends MensajeAbstracto {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public DecoradorMensaje(MensajeAbstracto decorado) {
		_decorado = decorado;
	}

	@Override
	public void establecerLeido(boolean leido) {
		_decorado.establecerLeido(leido);
	}

	@Override
	public int obtenerNoLeidos() {
		return _decorado.obtenerNoLeidos();
	}

	@Override
	public int obtenerTamaño() {
		return _decorado.obtenerTamaño();
	}

	@Override
	public Integer obtenerIcono() {
		return _decorado.obtenerIcono();
	}

	@Override
	public String obtenerPreVisualizacion() {
		return _decorado.obtenerPreVisualizacion();
	}

	@Override
	public String obtenerVisualizacion() {
		return _decorado.obtenerVisualizacion();
	}

	@Override
	public String obtenerRuta() {
		return _decorado.obtenerRuta();
	}

	@Override
	public Collection explorar() throws OperacionInvalida {
		return _decorado.explorar();
	}

	@Override
	public Collection buscar(String busqueda) {
		EtmPoint point = etmMonitor.createPoint("DecoradorMensaje:buscar");
		try {
			Collection resultado = _decorado.buscar(busqueda);
			if (resultado.remove(_decorado)) {
				resultado.add(this);
			}
			return resultado;
		} finally {
			point.collect();
		}
	}

	@Override
	public void añadir(Correo correo) throws OperacionInvalida {
		_decorado.añadir(correo);
	}

	@Override
	public void eliminar(Correo correo) throws OperacionInvalida {
		_decorado.eliminar(correo);
	}

	@Override
	public Correo obtenerHijo(int n) throws OperacionInvalida {
		return _decorado.obtenerHijo(n);
	}

	@Override
	public Correo obtenerPadre() {
		return _decorado.obtenerPadre();
	}

	@Override
	protected void establecerPadre(Correo padre) {
		_decorado.establecerPadre(padre);
	}

	private MensajeAbstracto _decorado;

}
