package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Archivo;
import gal.udc.fic.vvs.email.archivo.Imagen;
import gal.udc.fic.vvs.email.archivo.Texto;

public class TestAdjunto {
	public static Adjunto adjunto;
	public static Mensaje mensaje;
	public static Texto texto;
	public static Archivo archivo;
	public static Adjunto adjuntoVacio;
	public static Mensaje mensajeVacio;
	public static Texto textoVacio;
	public static Archivo archivoVacio;

	@Before
	public void setUpTest() {
		archivo = new Imagen("imagen", "Foto");
		texto = new Texto("texto", "Contenido del texto");
		mensaje = new Mensaje(texto);
		adjunto = new Adjunto(mensaje, archivo);

		archivoVacio = new Imagen("", "");
		textoVacio = new Texto("", "");
		mensajeVacio = new Mensaje(textoVacio);
		adjuntoVacio = new Adjunto(mensajeVacio, archivoVacio);

	}

	@Test
	public void testObtenerTamaño() {
		assertEquals(mensaje.obtenerTamaño() + archivo.obtenerTamaño(), adjunto.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacion() {
		assertEquals(mensaje.obtenerVisualizacion() + "\n\nAdxunto: " + archivo.obtenerPreVisualizacion(),
				adjunto.obtenerVisualizacion());
	}

	@Test
	public void testObtenerTamañoAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerTamaño() + archivoVacio.obtenerTamaño(), adjuntoVacio.obtenerTamaño());
	}

	@Test
	public void testObtenerVisualizacionAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerVisualizacion() + "\n\nAdxunto: " + archivoVacio.obtenerPreVisualizacion(),
				adjuntoVacio.obtenerVisualizacion());
	}

}
