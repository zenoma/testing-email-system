package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Archivo;
import gal.udc.fic.vvs.email.archivo.Audio;
import gal.udc.fic.vvs.email.archivo.Imagen;
import gal.udc.fic.vvs.email.archivo.Texto;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestAdjunto {
	private static Texto textoVacio = new Texto("", "");
	private static Archivo archivoVacio = new Imagen("", "");
	private static Mensaje mensajeVacio = new Mensaje(textoVacio);
	private static Adjunto adjuntoVacio = new Adjunto(mensajeVacio, archivoVacio);

	@Provide
	Arbitrary<Texto> textoProvider() {
		Arbitrary<String> texts = Arbitraries.strings().alpha();
		Arbitrary<String> contents = Arbitraries.strings().alpha();
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	@Provide
	Arbitrary<Imagen> imagenProvider() {
		Arbitrary<String> texts = Arbitraries.strings().alpha();
		Arbitrary<String> contents = Arbitraries.strings().alpha();
		return Combinators.combine(texts, contents).as((text, content) -> new Imagen(text, content));
	}

	@Provide
	Arbitrary<Audio> audioProvider() {
		Arbitrary<String> texts = Arbitraries.strings().alpha();
		Arbitrary<String> contents = Arbitraries.strings().alpha();
		return Combinators.combine(texts, contents).as((text, content) -> new Audio(text, content));
	}

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Provide
	Arbitrary<Adjunto> adjuntoProvider() {
		Arbitrary<Mensaje> mensajes = mensajeProvider();
		int rnd = Arbitraries.integers().between(0, 2).sample();
		if (rnd == 0) {
			return Combinators.combine(mensajes, textoProvider())
					.as((mensaje, archivo) -> new Adjunto(mensaje, archivo));
		} else if (rnd == 1) {
			return Combinators.combine(mensajes, imagenProvider())
					.as((mensaje, archivo) -> new Adjunto(mensaje, archivo));
		} else {
			return Combinators.combine(mensajes, audioProvider())
					.as((mensaje, archivo) -> new Adjunto(mensaje, archivo));
		}

	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg     Mensaje para crear adjunto
	 * @param archivo Archivo para crear adjunto
	 */
	@Property
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje msg, @ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerTamaño() + archivo.obtenerTamaño(), adjunto.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg     Mensaje para crear adjunto
	 * @param archivo Archivo para crear adjunto
	 * 
	 */
	@Property
	public void testObtenerVisualizacion(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerVisualizacion() + "\n\nAdxunto: " + archivo.obtenerPreVisualizacion(),
				adjunto.obtenerVisualizacion());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera
	 * </pre>
	 */
	@Example
	public void testObtenerTamañoAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerTamaño() + archivoVacio.obtenerTamaño(), adjuntoVacio.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera
	 * </pre>
	 */
	@Example
	public void testObtenerVisualizacionAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerVisualizacion() + "\n\nAdxunto: " + archivoVacio.obtenerPreVisualizacion(),
				adjuntoVacio.obtenerVisualizacion());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer leido
	 */
	@Property
	public void testEstablecerNoLeidoYObtenerNoLeidos(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(false);
		assertEquals(1, adjunto.obtenerNoLeidos());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer leido
	 */
	@Property
	public void testEstablecerLeidoYObtenerNoLeidos(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(true);
		assertEquals(0, adjunto.obtenerNoLeidos());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer leido
	 */
	@Property
	public void testObtenerIconoAdjunto(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(true);
		assertEquals(Correo.ICONO_MENSAJE, adjunto.obtenerIcono());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer leido
	 */
	@Property
	public void testObtenerIconoNuevoMensaje(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(false);
		assertEquals(Correo.ICONO_NUEVO_MENSAJE, adjunto.obtenerIcono());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer leido
	 */
	@Property
	public void testObtenerPreVisualizacion(@ForAll("adjuntoProvider") Adjunto adjunto) {
		assertTrue(adjunto.obtenerPreVisualizacion().endsWith("..."));
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param txt     Texto para crear el mensaje
	 * @param archivo Para crear el adjunto
	 */
	@Property
	public void testBuscar(@ForAll("textoProvider") Texto txt, @ForAll("imagenProvider") Archivo archivo) {
		Mensaje msg = new Mensaje(txt);
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(1, adjunto.buscar(txt.obtenerContenido()).size());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param txt     Texto para crear el mensaje
	 * @param archivo Para crear el adjunto
	 */
	@Example
	public void testBuscarSinResultado(@ForAll("textoProvider") Texto txt, @ForAll("imagenProvider") Archivo archivo) {
		Mensaje msg = new Mensaje(txt);
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(0, adjunto.buscar(txt.obtenerContenido() + "aaa").size());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg     Mensaje para obtener ruta
	 * @param archivo Para obtener ruta
	 */
	@Property
	public void testObtenerRuta(@ForAll("mensajeProvider") Mensaje msg, @ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerRuta(), adjunto.obtenerRuta());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para explorar
	 */
	@Example
	public void testExplorar(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.explorar();
		}).isInstanceOf(OperacionInvalida.class);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para añadir
	 */
	@Example
	public void testAñadirCorreo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.añadir(null);
		}).isInstanceOf(OperacionInvalida.class);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para eliminar
	 */
	@Example
	public void testEliminarCorreo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.eliminar(null);
		}).isInstanceOf(OperacionInvalida.class);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para obtener hijo
	 */
	@Example
	public void testObtenerHijo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.obtenerHijo(0);
		}).isInstanceOf(OperacionInvalida.class);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para obtener padre
	 */
	@Property
	public void testObtenerPadre(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThat(adjunto.obtenerPadre()).isNull();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param adjunto Adjunto para establecer padre
	 */
	@Property
	public void testEstablecerPadre(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Carpeta nuevaCarpeta = new Carpeta("Nueva");
		adjunto.establecerPadre(nuevaCarpeta);
		assertEquals(nuevaCarpeta, adjunto.obtenerPadre());
	}

}
