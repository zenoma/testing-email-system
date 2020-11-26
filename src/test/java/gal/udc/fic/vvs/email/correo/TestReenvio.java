package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestReenvio {
	public static Reenvio reenvio;
	public static Correo correoReenviado;
	public static Mensaje mensajeNuevo;
	public static Texto texto;
	public static Texto textoNuevo;
	public static Correo correo;

	public static Reenvio reenvioVacio;
	public static Correo correoReenviadoVacio;
	public static Mensaje mensajeNuevoVacio;
	public static Texto textoVacio;
	public static Texto textoNuevoVacio;
	public static Correo correoVacio;

	@Before
	public void setUpTest() {
		texto = new Texto("texto", "Contenido del texto");
		textoNuevo = new Texto("texto", "Reenvio el siguiente mensaje");
		mensajeNuevo = new Mensaje(textoNuevo);
		correoReenviado = new Mensaje(texto);
		reenvio = new Reenvio(mensajeNuevo, correoReenviado);

		textoVacio = new Texto("", "");
		textoNuevoVacio = new Texto("", "");
		mensajeNuevoVacio = new Mensaje(textoNuevoVacio);
		correoReenviadoVacio = new Mensaje(textoVacio);
		reenvioVacio = new Reenvio(mensajeNuevoVacio, correoReenviadoVacio);
	}

	@Test
	public void testObtenerTamaño() {
		assertEquals(texto.obtenerTamaño() + textoNuevo.obtenerTamaño(), reenvio.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacion() {
		assertEquals(
				mensajeNuevo.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n"
						+ correoReenviado.obtenerVisualizacion() + "\n---- Fin correo reenviado ----",
				reenvio.obtenerVisualizacion());
	}

	@Test
	public void testObtenerTamañoReenvioVacio() {
		assertEquals(textoVacio.obtenerTamaño() + textoNuevoVacio.obtenerTamaño(), reenvioVacio.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacionReenvioVacio() {
		assertEquals(
				mensajeNuevoVacio.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n"
						+ correoReenviadoVacio.obtenerVisualizacion() + "\n---- Fin correo reenviado ----",
				reenvioVacio.obtenerVisualizacion());
	}

}
