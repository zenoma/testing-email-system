package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Iterator;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class CarpetaLimitada extends CorreoAbstracto {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public CarpetaLimitada(Carpeta carpeta, int tamaño) {
		_carpeta = carpeta;
		_tamaño = tamaño;
	}

	@Override
	public void establecerLeido(boolean leido) {
		_carpeta.establecerLeido(leido);
	}

	@Override
	public int obtenerNoLeidos() {
		return _carpeta.obtenerNoLeidos();
	}

	@Override
	public int obtenerTamaño() {
		return _carpeta.obtenerTamaño();
	}

	@Override
	public Integer obtenerIcono() {
		return _carpeta.obtenerIcono();
	}

	@Override
	public String obtenerPreVisualizacion() {
		return _carpeta.obtenerPreVisualizacion();
	}

	@Override
	public String obtenerVisualizacion() {
		return _carpeta.obtenerVisualizacion();
	}

	@Override
	public String obtenerRuta() {
		return _carpeta.obtenerRuta();
	}

	@Override
	public Collection explorar() throws OperacionInvalida {
		return _carpeta.explorar();
	}

	@Override
	public Collection buscar(String busqueda) {
		EtmPoint point = etmMonitor.createPoint("CarpetaLimitada:buscar");
		try {
			Collection resultado = _carpeta.buscar(busqueda);
			if (resultado.remove(_carpeta)) {
				resultado.add(this);
			}
			Iterator iResultado = resultado.iterator();
			for (int i = 0; iResultado.hasNext(); i++) {
				iResultado.next();
				if (i > _tamaño) {
					iResultado.remove();
				}
			}
			return resultado;
		} finally {
			point.collect();
		}

	}

	@Override
	public void añadir(Correo correo) throws OperacionInvalida {
		_carpeta.añadir(correo);
	}

	@Override
	public void eliminar(Correo correo) throws OperacionInvalida {
		_carpeta.eliminar(correo);
	}

	@Override
	public Correo obtenerHijo(int n) throws OperacionInvalida {
		return _carpeta.obtenerHijo(n);
	}

	@Override
	public Correo obtenerPadre() {
		return _carpeta.obtenerPadre();
	}

	@Override
	protected void establecerPadre(Correo padre) {
		_carpeta.establecerPadre(padre);
	}

	private Carpeta _carpeta;
	private int _tamaño;

}
