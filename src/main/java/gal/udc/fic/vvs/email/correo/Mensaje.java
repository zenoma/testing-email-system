package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Vector;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;
import gal.udc.fic.vvs.email.archivo.Texto;

public class Mensaje extends MensajeAbstracto {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public Mensaje(Texto contenido) {
		_contenido = contenido;
		_leido = false;
	}

	@Override
	public void establecerLeido(boolean leido) {
		_leido = leido;
	}

	@Override
	public int obtenerNoLeidos() {
		if (_leido) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int obtenerTamaño() {
		EtmPoint point = etmMonitor.createPoint("Mensaje:obtenerTamaño");
		try {
			return _contenido.obtenerTamaño();
		} finally {
			point.collect();
		}
	}

	@Override
	public Integer obtenerIcono() {
		if (_leido) {
			return Correo.ICONO_MENSAJE;
		} else {
			return Correo.ICONO_NUEVO_MENSAJE;
		}
	}

	@Override
	public String obtenerPreVisualizacion() {
		String visualizacion = obtenerVisualizacion();
		return visualizacion.substring(0, Math.min(visualizacion.length(), TAMAÑO_PREVISUALIZACION)) + "...";
	}

	@Override
	public String obtenerVisualizacion() {
		return _contenido.obtenerContenido();
	}

	@Override
	public Collection buscar(String busqueda) {
		EtmPoint point = etmMonitor.createPoint("Mensaje:buscar");
		try {
			Vector resultado = new Vector();

			if (obtenerVisualizacion().toLowerCase().matches(".*" + busqueda.toLowerCase() + ".*")) {
				resultado.addElement(this);
			}
			return resultado;
		} finally {
			point.collect();
		}
	}

	private final static int TAMAÑO_PREVISUALIZACION = 32;
	private boolean _leido;
	private Texto _contenido;

}
