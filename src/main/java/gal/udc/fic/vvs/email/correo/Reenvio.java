package gal.udc.fic.vvs.email.correo;

import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.monitor.EtmPoint;

public class Reenvio extends DecoradorMensaje {
	private static final EtmMonitor etmMonitor = EtmManager.getEtmMonitor();

	public Reenvio(MensajeAbstracto mensaje, Correo correo) {
		super(mensaje);
		_correo = correo;
	}

	@Override
	public int obtenerTamaño() {
		EtmPoint point = etmMonitor.createPoint("Reenvio:obtenerTamaño");
		try {
			return super.obtenerTamaño() + _correo.obtenerTamaño();
		} finally {
			point.collect();
		}
	}

	@Override
	public String obtenerVisualizacion() {
		return super.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n" + _correo.obtenerVisualizacion()
				+ "\n---- Fin correo reenviado ----";
	}

	private Correo _correo;
}
