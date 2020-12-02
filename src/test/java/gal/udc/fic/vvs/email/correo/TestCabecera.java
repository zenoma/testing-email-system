package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestCabecera {

	private final String NOMBRE = "nombre";
	private static String VALOR = "valor";

	private final String NOMBRE_VACIO = "";
	private static String VALOR_VACIO = "";

	private static Cabecera cabecera;
	private static Mensaje mensaje;
	private static Texto texto;

	private static Cabecera cabeceraVacia;
	private static Mensaje mensajeVacio;
	private static Texto textoVacio;

	@Before
	public void setUpTest() {
		texto = new Texto("texto", "Contenido del texto");
		mensaje = new Mensaje(texto);
		cabecera = new Cabecera(mensaje, NOMBRE, VALOR);

		textoVacio = new Texto("", "");
		mensajeVacio = new Mensaje(texto);
		cabeceraVacia = new Cabecera(mensaje, NOMBRE_VACIO, VALOR_VACIO);
	}

	@Test
	public void testObtenerTamaño() {
		assertEquals(mensaje.obtenerTamaño() + NOMBRE.length() + VALOR.length(), cabecera.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacion() {
		assertEquals("nombre: valor\n" + "Contenido del texto", cabecera.obtenerVisualizacion());
	}

	@Test
	public void testObtenerTamañoCabeceraVacio() {
		assertEquals(mensajeVacio.obtenerTamaño() + NOMBRE_VACIO.length() + VALOR_VACIO.length(),
				cabeceraVacia.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacionCabeceraVacio() {
		assertEquals(": \n" + "Contenido del texto", cabeceraVacia.obtenerVisualizacion());
	}

}
