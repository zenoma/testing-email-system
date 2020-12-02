package gal.udc.fic.vvs.email.archivo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestArchivo {
	private static Archivo audio;
	private static Archivo imagen;
	private static Archivo texto;

	@Before
	public void setUpTest() {
		audio = new Audio("audio", "song");
		imagen = new Imagen("imagen", "photo");
		texto = new Texto("texto", "descripcion");
	}

	@Test
	public void testObtenerNombre() {
		String expected = "audio";
		assertEquals(expected, audio.obtenerNombre());
	}

	@Test
	public void testObtenerContenido() {
		String expected = "photo";
		assertEquals(expected, imagen.obtenerContenido());
	}

	@Test
	public void testObtenerTamaño() {
		String expected = "photo";
		assertEquals(expected.length(), imagen.obtenerTamaño());
	}

	@Test
	public void testObtenerMimeType() {
		String expected = "image/png";
		assertEquals(expected, imagen.obtenerMimeType());
	}

	@Test
	public void testObtenerAudioMimeType() {
		String expected = "audio/ogg";
		assertEquals(expected, audio.obtenerMimeType());
	}

	@Test
	public void testObtenerPreVisualizacion() {
		String expected = texto.obtenerNombre() + "(" + texto.obtenerTamaño() + " bytes, " + texto.obtenerMimeType()
				+ ")";
		assertEquals(expected, texto.obtenerPreVisualizacion());
	}
}
