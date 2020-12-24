package gal.udc.fic.vvs.email.correo;

import java.util.Collection;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public abstract class CorreoAbstracto implements Correo {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public CorreoAbstracto() {
		_padre = null;
	}

	@Override
	public String obtenerRuta() {
		EtmPoint point = etmMonitor.createPoint("CorreoAbstracto:obtenerRuta");
		try {
			if (obtenerPadre() != null) {
				return obtenerPadre().obtenerRuta() + " > " + obtenerPreVisualizacion();
			} else {
				return obtenerPreVisualizacion();
			}
		} finally {
			point.collect();
		}
	}

	@Override
	public Collection explorar() throws OperacionInvalida {
		throw new OperacionInvalida();
	}

	@Override
	public void añadir(Correo correo) throws OperacionInvalida {
		throw new OperacionInvalida();
	}

	@Override
	public void eliminar(Correo correo) throws OperacionInvalida {
		throw new OperacionInvalida();
	}

	@Override
	public Correo obtenerHijo(int n) throws OperacionInvalida {
		throw new OperacionInvalida();
	}

	@Override
	public Correo obtenerPadre() {
		return _padre;
	}

	protected void establecerPadre(Correo padre) {
		EtmPoint point = etmMonitor.createPoint("CorreoAbstracto:obtenerRuta");
		_padre = padre;
		point.collect();
	}

	private Correo _padre;

}
