package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestCabecera {

	public final String NOMBRE = "nombre";
	public static String VALOR = "valor";

	public final String NOMBRE_VACIO = "";
	public static String VALOR_VACIO = "";

	public static Cabecera cabecera;
	public static Mensaje mensaje;
	public static Texto texto;

	public static Cabecera cabeceraVacia;
	public static Mensaje mensajeVacio;
	public static Texto textoVacio;

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
