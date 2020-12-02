package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestCarpeta {

	private static Carpeta carpetaImportantes;
	private static Carpeta carpetaLeidos;
	private static Carpeta carpetaVacia;
	private static Correo correoNoLeido;
	private static Correo correoLeido;

	@Before
	public void setUpTest() {
		carpetaImportantes = new Carpeta("Importantes");
		carpetaLeidos = new Carpeta("Leidos");
		carpetaVacia = new Carpeta("Vacia");
		carpetaLeidos.establecerLeido(false);
		correoNoLeido = new Mensaje(new Texto("texto", "Contenido del texto"));
		correoNoLeido.establecerLeido(false);
		correoLeido = new Mensaje(new Texto("imagen", "Foto"));
		correoLeido.establecerLeido(true);
	}

	@Test
	public void testAñadirCorreoYBuscarCorreo() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		assertTrue(carpetaImportantes.buscar("texto").contains(correoNoLeido));
	}

	@Test
	public void testMoverCorreoDeCarpeta() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaLeidos.añadir(correoNoLeido);
		assertTrue(carpetaLeidos.buscar("texto").contains(correoNoLeido));
	}

	@Test
	public void testEliminarCorreoDeCarpeta() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.eliminar(correoNoLeido);
		assertNull(correoNoLeido.obtenerPadre());
	}

	@Test
	public void testEliminarCorreoNoExistente() throws OperacionInvalida {
		carpetaImportantes.eliminar(correoNoLeido);
		assertNull(correoNoLeido.obtenerPadre());
	}

	@Test
	public void testExplorarCarpetaVacia() throws OperacionInvalida {
		assertEquals(0, carpetaVacia.explorar().size());
	}

	@Test
	public void testExplorarCarpetaConElementos() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.añadir(correoLeido);
		assertEquals(2, carpetaImportantes.explorar().size());
	}

	@Test
	public void testObtenerNoLeidos() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.añadir(correoLeido);
		assertEquals(1, carpetaImportantes.obtenerNoLeidos());

	}

	@Test
	public void testObtenerTamaño() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.añadir(correoLeido);
		assertNotEquals(0, carpetaImportantes.obtenerTamaño());

	}

	@Test
	public void testObtenerTamañoCarpetaVacia() throws OperacionInvalida {
		assertEquals(0, carpetaVacia.obtenerTamaño());
	}

	@Test
	public void testObtenerIcono() throws OperacionInvalida {
		assertEquals(Correo.ICONO_CARPETA, carpetaLeidos.obtenerIcono());
	}

	@Test
	public void testObtenerVisualización() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.añadir(correoLeido);
		String expected = "Importantes (" + carpetaImportantes.obtenerNoLeidos() + ")";
		assertEquals(expected, carpetaImportantes.obtenerVisualizacion());
	}

	@Test
	public void testObtenerPreVisualización() throws OperacionInvalida {
		carpetaImportantes.añadir(correoNoLeido);
		carpetaImportantes.añadir(correoLeido);
		assertEquals(carpetaImportantes.obtenerPreVisualizacion(), carpetaImportantes.obtenerVisualizacion());
	}

	@Test
	public void testObtenerVisualizaciónCarpetaVacia() throws OperacionInvalida {
		assertEquals("Vacia", carpetaVacia.obtenerVisualizacion());
	}

	@Test
	public void testEstablecerCarpetaLeida() throws OperacionInvalida {
		carpetaLeidos.añadir(correoNoLeido);
		carpetaLeidos.establecerLeido(true);
		assertEquals(0, carpetaLeidos.obtenerNoLeidos());
	}
}
