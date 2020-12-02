package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import gal.udc.fic.vvs.email.archivo.Archivo;
import gal.udc.fic.vvs.email.archivo.Imagen;
import gal.udc.fic.vvs.email.archivo.Texto;

public class TestAdjunto {
	private static Adjunto adjunto;
	private static Mensaje mensaje;
	private static Texto texto;
	private static Archivo archivo;
	private static Adjunto adjuntoVacio;
	private static Mensaje mensajeVacio;
	private static Texto textoVacio;
	private static Archivo archivoVacio;

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

	@Test
	public void testEstablecerNoLeidoYObtenerNoLeidos() {
		adjunto.establecerLeido(false);
		assertEquals(1, adjunto.obtenerNoLeidos());
	}

	@Test
	public void testEstablecerLeidoYObtenerNoLeidos() {
		adjunto.establecerLeido(true);
		assertEquals(0, adjunto.obtenerNoLeidos());
	}

	@Test
	public void testObtenerIconoAdjunto() {
		adjunto.establecerLeido(true);
		assertEquals(Correo.ICONO_MENSAJE, adjunto.obtenerIcono());
	}

	@Test
	public void testObtenerIconoNuevoMensaje() {
		adjunto.establecerLeido(false);
		assertEquals(Correo.ICONO_NUEVO_MENSAJE, adjunto.obtenerIcono());
	}

	@Test
	public void testObtenerPreVisualizacion() {
		assertTrue(adjunto.obtenerPreVisualizacion().endsWith("..."));
	}

	@Test
	public void testBuscar() {
		assertEquals(1, adjunto.buscar(texto.obtenerContenido()).size());
	}

	@Test
	public void testBuscarSinResultado() {
		assertEquals(0, adjunto.buscar("imagen").size());
	}

	@Test
	public void testObtenerRuta() {
		assertEquals(mensaje.obtenerRuta(), adjunto.obtenerRuta());
	}

	@Test(expected = OperacionInvalida.class)
	public void testExplorar() throws OperacionInvalida {
		adjunto.explorar();
	}

	@Test(expected = OperacionInvalida.class)
	public void testAñadirCorreo() throws OperacionInvalida {
		adjunto.añadir(null);
	}

	@Test(expected = OperacionInvalida.class)
	public void testEliminarCorreo() throws OperacionInvalida {
		adjunto.eliminar(null);
	}

	@Test(expected = OperacionInvalida.class)
	public void testObtenerHijo() throws OperacionInvalida {
		adjunto.obtenerHijo(0);
	}

	@Test
	public void testObtenerPadre() throws OperacionInvalida {
		assertNull(adjunto.obtenerPadre());
	}

	@Test
	public void testEstablecerPadre() throws OperacionInvalida {
		Carpeta nuevaCarpeta = new Carpeta("Nueva");
		adjunto.establecerPadre(nuevaCarpeta);
		assertEquals(nuevaCarpeta, adjunto.obtenerPadre());
	}

}
