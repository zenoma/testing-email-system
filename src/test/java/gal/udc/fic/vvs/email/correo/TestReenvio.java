package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Texto;

public class TestReenvio {
	private static Reenvio reenvio;
	private static Correo correoReenviado;
	private static Mensaje mensajeNuevo;
	private static Texto texto;
	private static Texto textoNuevo;
	private static Correo correo;

	private static Reenvio reenvioVacio;
	private static Correo correoReenviadoVacio;
	private static Mensaje mensajeNuevoVacio;
	private static Texto textoVacio;
	private static Texto textoNuevoVacio;
	private static Correo correoVacio;

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
