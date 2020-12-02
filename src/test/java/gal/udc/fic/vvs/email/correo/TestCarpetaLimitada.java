package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestCarpetaLimitada {
	private static Carpeta carpetaImportantes;
	private static Carpeta carpetaLeidos;
	private static Carpeta carpetaVacia;

	private static CarpetaLimitada carpetaImportantesLimitada;
	private static CarpetaLimitada carpetaLeidosLimitada;
	private static CarpetaLimitada carpetaVaciaLimitada;

	private static Correo correoNoLeido;
	private static Correo correoLeido;

	@Before
	public void setUpTest() {
		carpetaImportantes = new Carpeta("Importantes");
		carpetaImportantesLimitada = new CarpetaLimitada(carpetaImportantes, 5);
		carpetaLeidos = new Carpeta("Leidos");
		carpetaLeidosLimitada = new CarpetaLimitada(carpetaLeidos, 5);
		carpetaVacia = new Carpeta("Vacia");
		carpetaVaciaLimitada = new CarpetaLimitada(carpetaVacia, 0);

		correoNoLeido = new Mensaje(new Texto("texto", "Contenido del texto"));
		correoNoLeido.establecerLeido(false);
		correoLeido = new Mensaje(new Texto("imagen", "Foto"));
		correoLeido.establecerLeido(true);
	}

	@Test
	public void testAñadirCorreoYBuscarCorreo() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		assertTrue(carpetaImportantesLimitada.buscar("texto").contains(correoNoLeido));
	}

	@Test
	public void testAñadirCorreoYBuscarCorreoCarpetaVacia() throws OperacionInvalida {
		carpetaVaciaLimitada.añadir(correoNoLeido);
		assertTrue(carpetaVaciaLimitada.buscar("texto").contains(correoNoLeido));
	}

	@Test
	public void testMoverCorreoDeCarpeta() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaLeidosLimitada.añadir(correoNoLeido);
		assertTrue(carpetaLeidosLimitada.buscar("texto").contains(correoNoLeido));
	}

	@Test
	public void testEliminarCorreoDeCarpeta() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.eliminar(correoNoLeido);
		assertNull(correoNoLeido.obtenerPadre());
	}

	@Test
	public void testEliminarCorreoNoExistente() throws OperacionInvalida {
		carpetaImportantesLimitada.eliminar(correoNoLeido);
		assertNull(correoNoLeido.obtenerPadre());
	}

	@Test
	public void testExplorarCarpetaVacia() throws OperacionInvalida {
		assertEquals(0, carpetaVaciaLimitada.explorar().size());
	}

	@Test
	public void testExplorarCarpetaConElementos() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.añadir(correoLeido);
		assertEquals(2, carpetaImportantesLimitada.explorar().size());
	}

	@Test
	public void testObtenerNoLeidos() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.añadir(correoLeido);
		assertEquals(1, carpetaImportantesLimitada.obtenerNoLeidos());

	}

	@Test
	public void testObtenerTamaño() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.añadir(correoLeido);
		assertNotEquals(0, carpetaImportantesLimitada.obtenerTamaño());

	}

	@Test
	public void testObtenerTamañoCarpetaVacia() throws OperacionInvalida {
		assertEquals(0, carpetaVaciaLimitada.obtenerTamaño());
	}

	@Test
	public void testObtenerIcono() throws OperacionInvalida {
		assertEquals(Correo.ICONO_CARPETA, carpetaImportantesLimitada.obtenerIcono());
	}

	@Test
	public void testObtenerVisualización() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.añadir(correoLeido);
		String expected = "Importantes (" + carpetaImportantesLimitada.obtenerNoLeidos() + ")";
		assertEquals(expected, carpetaImportantesLimitada.obtenerVisualizacion());
	}

	@Test
	public void testObtenerPreVisualización() throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(correoNoLeido);
		carpetaImportantesLimitada.añadir(correoLeido);
		assertEquals(carpetaImportantesLimitada.obtenerPreVisualizacion(),
				carpetaImportantesLimitada.obtenerVisualizacion());
	}

	@Test
	public void testObtenerVisualizaciónCarpetaVacia() throws OperacionInvalida {
		assertEquals("Vacia", carpetaVaciaLimitada.obtenerVisualizacion());
	}

	@Test
	public void testEstablecerCarpetaLeida() throws OperacionInvalida {
		carpetaLeidosLimitada.añadir(correoNoLeido);
		carpetaLeidosLimitada.establecerLeido(true);
		assertEquals(0, carpetaLeidosLimitada.obtenerNoLeidos());
	}

	@Test
	public void testObtenerRuta() throws OperacionInvalida {
		assertEquals("Leidos", carpetaLeidosLimitada.obtenerRuta());
	}

	@Test
	public void testObtenerHijo() throws OperacionInvalida {
		carpetaLeidosLimitada.añadir(correoNoLeido);
		assertEquals(correoNoLeido, carpetaLeidosLimitada.obtenerHijo(0));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testObtenerHijoCarpetaVacia() throws OperacionInvalida {
		carpetaLeidosLimitada.obtenerHijo(0);
	}

	@Test
	public void testObtenerPadre() throws OperacionInvalida {
		assertNull(carpetaLeidosLimitada.obtenerPadre());
	}

	@Test
	public void testCambiarPadreYObtenerPadre() throws OperacionInvalida {
		carpetaVaciaLimitada.establecerPadre(carpetaImportantesLimitada);
		assertEquals(carpetaImportantesLimitada, carpetaVaciaLimitada.obtenerPadre());
	}
}
