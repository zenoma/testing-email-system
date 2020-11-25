package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestMensaje {
	public static Mensaje mensaje;
	public static Texto texto;

	@BeforeClass
	public static void setup() {
		texto = new Texto("texto", "Contenido del texto");
		mensaje = new Mensaje(texto);
	}

	@Test
	public void testEstablecerLeidoYObtenerNoLeidos() {
		mensaje.establecerLeido(false);
		assertEquals(1, mensaje.obtenerNoLeidos());
	}

	@Test
	public void testObtenerTamaño() {
		assertEquals(texto.obtenerTamaño(), mensaje.obtenerTamaño());
	}

	@Test
	public void testObtenerIcono() {
		mensaje.establecerLeido(true);
		assertEquals(Correo.ICONO_MENSAJE, mensaje.obtenerIcono());
	}

	@Test
	public void testObtenerPreVisualizacion() {
		assertTrue(mensaje.obtenerPreVisualizacion().endsWith("..."));
	}

	@Test
	public void testObtenerVisualizacion() {
		assertEquals(texto.obtenerContenido(), mensaje.obtenerVisualizacion());
	}

	@Test
	public void testBuscar() {
		assertEquals(1, mensaje.buscar(texto.obtenerContenido()).size());
	}

}
