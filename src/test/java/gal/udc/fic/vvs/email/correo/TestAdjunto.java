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

	@Property
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje msg, @ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerTamaño() + archivo.obtenerTamaño(), adjunto.obtenerTamaño());
	}

	@Property
	public void testObtenerVisualizacion(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerVisualizacion() + "\n\nAdxunto: " + archivo.obtenerPreVisualizacion(),
				adjunto.obtenerVisualizacion());
	}

	@Example
	public void testObtenerTamañoAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerTamaño() + archivoVacio.obtenerTamaño(), adjuntoVacio.obtenerTamaño());
	}

	@Example
	public void testObtenerVisualizacionAdjuntoVacio() {
		assertEquals(mensajeVacio.obtenerVisualizacion() + "\n\nAdxunto: " + archivoVacio.obtenerPreVisualizacion(),
				adjuntoVacio.obtenerVisualizacion());
	}

	@Property
	public void testEstablecerNoLeidoYObtenerNoLeidos(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(false);
		assertEquals(1, adjunto.obtenerNoLeidos());
	}

	@Property
	public void testEstablecerLeidoYObtenerNoLeidos(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(true);
		assertEquals(0, adjunto.obtenerNoLeidos());
	}

	@Property
	public void testObtenerIconoAdjunto(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(true);
		assertEquals(Correo.ICONO_MENSAJE, adjunto.obtenerIcono());
	}

	@Property
	public void testObtenerIconoNuevoMensaje(@ForAll("adjuntoProvider") Adjunto adjunto) {
		adjunto.establecerLeido(false);
		assertEquals(Correo.ICONO_NUEVO_MENSAJE, adjunto.obtenerIcono());
	}

	@Property
	public void testObtenerPreVisualizacion(@ForAll("adjuntoProvider") Adjunto adjunto) {
		assertTrue(adjunto.obtenerPreVisualizacion().endsWith("..."));
	}

	@Property
	public void testBuscar(@ForAll("textoProvider") Texto txt, @ForAll("imagenProvider") Archivo archivo) {
		Mensaje msg = new Mensaje(txt);
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(1, adjunto.buscar(txt.obtenerContenido()).size());
	}

	@Example
	public void testBuscarSinResultado(@ForAll("textoProvider") Texto txt, @ForAll("imagenProvider") Archivo archivo) {
		Mensaje msg = new Mensaje(txt);
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(0, adjunto.buscar(txt.obtenerContenido() + "aaa").size());
	}

	@Property
	public void testObtenerRuta(@ForAll("mensajeProvider") Mensaje msg, @ForAll("imagenProvider") Archivo archivo) {
		Adjunto adjunto = new Adjunto(msg, archivo);
		assertEquals(msg.obtenerRuta(), adjunto.obtenerRuta());
	}

	@Example
	public void testExplorar(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.explorar();
		}).isInstanceOf(OperacionInvalida.class);
	}

	@Example
	public void testAñadirCorreo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.añadir(null);
		}).isInstanceOf(OperacionInvalida.class);
	}

	@Example
	public void testEliminarCorreo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.eliminar(null);
		}).isInstanceOf(OperacionInvalida.class);
	}

	@Example
	public void testObtenerHijo(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			adjunto.obtenerHijo(0);
		}).isInstanceOf(OperacionInvalida.class);
	}

	@Property
	public void testObtenerPadre(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Assertions.assertThat(adjunto.obtenerPadre()).isNull();
	}

	@Property
	public void testEstablecerPadre(@ForAll("adjuntoProvider") Adjunto adjunto) throws OperacionInvalida {
		Carpeta nuevaCarpeta = new Carpeta("Nueva");
		adjunto.establecerPadre(nuevaCarpeta);
		assertEquals(nuevaCarpeta, adjunto.obtenerPadre());
	}

}
