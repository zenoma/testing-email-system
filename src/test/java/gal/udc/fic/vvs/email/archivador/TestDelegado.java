package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;

public class TestDelegado {
	private static Delegado delegado;
	private static Delegado delegadoPequeño;
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
		delegadoPequeño = new Delegado(archivadorPequeño);
		delegado = new Delegado(archivadorSimple);
		correo = new Mensaje(new Texto("texto", "Contenido texto"));
	}

	@Test
	public void testAlmacenarCorreoYObtenerEspacioDisponible() {
		delegado.almacenarCorreo(correo);
		assertEquals(SIZE - correo.obtenerTamaño(), delegado.obtenerEspacioDisponible());
	}

	@Test
	public void testObtenerDelegado() {
		assertNull(delegado.obtenerDelegado());
	}

	@Test
	public void testEstablecerDelegadoYObtenerDelegado() {
		delegado.establecerDelegado(archivador);
		assertEquals(archivador, delegado.obtenerDelegado());
	}

	@Test
	public void testNoAlmacenarCorreo() {
		delegadoPequeño.establecerDelegado(archivadorPequeño);
		assertFalse(delegadoPequeño.almacenarCorreo(correo));
	}

}
