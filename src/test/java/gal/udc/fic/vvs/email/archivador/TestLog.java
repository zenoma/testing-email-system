package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;

public class TestLog {
	private static Log log;
	private static Log logPequeño;
	private static Archivador archivadorSimple;
	private static Archivador archivadorPequeño;
	private static Archivador archivador;
	private static Correo correo;

	private final String NOMBRE = "nombre";
	private final int SIZE = 100;

	@Before
	public void setUpTest() {
		archivador = new ArchivadorSimple(NOMBRE, SIZE);
		archivadorPequeño = new ArchivadorSimple(NOMBRE, 0);
		archivadorSimple = new ArchivadorSimple(NOMBRE, SIZE);
		logPequeño = new Log(archivadorPequeño);
		log = new Log(archivadorSimple);
		correo = new Mensaje(new Texto("texto", "Contenido texto"));
	}

	@Test
	public void testAlmacenarCorreoYObtenerEspacioDisponible() {
		log.almacenarCorreo(correo);
		assertEquals(SIZE - correo.obtenerTamaño(), log.obtenerEspacioDisponible());
	}

	@Test
	public void testNoAlmacenarCorreo() {
		logPequeño.establecerDelegado(archivadorPequeño);
		assertFalse(logPequeño.almacenarCorreo(correo));
	}

}
