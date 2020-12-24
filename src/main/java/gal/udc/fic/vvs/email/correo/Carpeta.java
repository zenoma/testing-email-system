package gal.udc.fic.vvs.email.correo;

import java.util.Collection;
import java.util.Vector;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class Carpeta extends CorreoAbstracto {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public Carpeta(String nombre) {
		_nombre = nombre;
		_hijos = new Vector();
	}

	@Override
	public void establecerLeido(boolean leido) {
		EtmPoint point = etmMonitor.createPoint("Carpeta:establecerLeido");
		try {
			for (int i = 0; i < _hijos.size(); i++) {
				obtenerHijo(i).establecerLeido(leido);
			}
		} catch (OperacionInvalida e) {
		} finally {
			point.collect();
		}
	}

	@Override
	public int obtenerNoLeidos() {
		EtmPoint point = etmMonitor.createPoint("Carpeta:obtenerNoLeidos");
		try {
			int resultado = 0;

			try {
				for (int i = 0; i < _hijos.size(); i++) {
					resultado += obtenerHijo(i).obtenerNoLeidos();
				}
			} catch (OperacionInvalida e) {
			}
			return resultado;
		} finally {
			point.collect();
		}
	}

	@Override
	public int obtenerTamaño() {
		EtmPoint point = etmMonitor.createPoint("Carpeta:obtenerTamaño");
		try {
			int resultado = 0;
			try {
				for (int i = 0; i < _hijos.size(); i++) {
					resultado += obtenerHijo(i).obtenerTamaño();
				}
			} catch (OperacionInvalida e) {
			}
			return resultado;
		} finally

		{
			point.collect();
		}
	}

	@Override
	public Integer obtenerIcono() {
		return Correo.ICONO_CARPETA;
	}

	@Override
	public String obtenerPreVisualizacion() {
		return obtenerVisualizacion();
	}

	@Override
	public String obtenerVisualizacion() {
		String resultado = _nombre;
		if (obtenerNoLeidos() > 0) {
			resultado += " (" + obtenerNoLeidos() + ")";
		}
		return resultado;
	}

	@Override
	public Collection explorar() throws OperacionInvalida {
		return _hijos;
	}

	@Override
	public Collection buscar(String busqueda) {

		EtmPoint point = etmMonitor.createPoint("Carpeta:buscar");
		Vector resultado = new Vector();
		try {
			for (int i = 0; i < _hijos.size(); i++) {
				resultado.addAll(obtenerHijo(i).buscar(busqueda));
			}
		} catch (OperacionInvalida e) {
		} finally {
			point.collect();
		}
		return resultado;
	}

	@Override
	public void añadir(Correo correo) throws OperacionInvalida {
		if (correo.obtenerPadre() != null) {
			correo.obtenerPadre().eliminar(correo);
		}
		((CorreoAbstracto) correo).establecerPadre(this);
		_hijos.addElement(correo);
	}

	@Override
	public void eliminar(Correo correo) throws OperacionInvalida {
		if (_hijos.removeElement(correo)) {
			((CorreoAbstracto) correo).establecerPadre(null);
		}
	}

	@Override
	public Correo obtenerHijo(int n) throws OperacionInvalida {
		return ((Correo) _hijos.elementAt(n));

	}

	private Vector _hijos;
	private String _nombre;

}
