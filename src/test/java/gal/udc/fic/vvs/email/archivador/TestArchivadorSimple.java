package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;

public class TestArchivadorSimple {
	public static Archivador archivadorSimple;
	public static Archivador archivadorPequeño;
	public static Correo correo;

	public final String NOMBRE = "nombre";
	public final int SIZE = 100;

	@Before
	public void setUpTest() {
		archivadorPequeño = new ArchivadorSimple(NOMBRE, 0);
		archivadorSimple = new ArchivadorSimple(NOMBRE, SIZE);
		correo = new Mensaje(new Texto("texto", "Contenido texto"));
	}

	@Test
	public void testObtenerNombre() {
		assertEquals(NOMBRE, archivadorSimple.obtenerNombre());
	}

	@Test
	public void testObtenerEspacioTotal() {
		assertEquals(SIZE, archivadorSimple.obtenerEspacioTotal());

	}

	@Test
	public void testAlmacenarCorreoYObtenerEspacioDisponible() {
		archivadorSimple.almacenarCorreo(correo);
		assertEquals(SIZE - correo.obtenerTamaño(), archivadorSimple.obtenerEspacioDisponible());
	}

	@Test
	public void testObtenerDelegadoNull() {
		assertNull(archivadorSimple.obtenerDelegado());
	}

	@Test
	public void testNoAlmacenarCorreo() {
		assertFalse(archivadorPequeño.almacenarCorreo(correo));
	}

}
