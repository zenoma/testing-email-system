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

	@Before
	public void setUpTest() {
		texto = new Texto("texto", "Contenido del texto");
		textoNuevo = new Texto("texto", "Reenvio el siguiente mensaje");
		mensajeNuevo = new Mensaje(textoNuevo);
		correoReenviado = new Mensaje(texto);
		reenvio = new Reenvio(mensajeNuevo, correoReenviado);
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

}
