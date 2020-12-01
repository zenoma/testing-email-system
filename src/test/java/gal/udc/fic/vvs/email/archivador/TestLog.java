package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;

public class TestLog {
	public static Log log;
	public static Log logPequeño;
	public static Archivador archivadorSimple;
	public static Archivador archivadorPequeño;
	public static Archivador archivador;
	public static Correo correo;

	public final String NOMBRE = "nombre";
	public final int SIZE = 100;

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
