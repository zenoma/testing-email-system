package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestMensaje {

	@Provide
	Arbitrary<Texto> textoProvider() {
		Arbitrary<String> texts = Arbitraries.strings();
		Arbitrary<String> contents = Arbitraries.strings();
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	private static Texto texto;

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Property
	public void testEstablecerNoLeidoYObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(false);
		Assertions.assertThat(mensaje.obtenerNoLeidos()).isEqualTo(1);
	}

	@Property
	public void testEstablecerLeidoYObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(true);
		Assertions.assertThat(mensaje.obtenerNoLeidos()).isZero();
	}

	@Property
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.obtenerVisualizacion().length()).isEqualTo(mensaje.obtenerTamaño());
	}

	@Property
	public void testObtenerIconoMensaje(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(true);
		Assertions.assertThat(mensaje.obtenerIcono()).isEqualTo(Correo.ICONO_MENSAJE);
	}

	@Property
	public void testObtenerIconoNuevoMensaje(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(false);
		assertEquals(Correo.ICONO_NUEVO_MENSAJE, mensaje.obtenerIcono());
	}

	@Property
	public void testObtenerPreVisualizacion(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.obtenerPreVisualizacion()).endsWith("...");
	}

	@Property
	public void testObtenerVisualizacion(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(texto.obtenerContenido()).isEqualTo(mensaje.obtenerVisualizacion());
	}

	@Property
	public void testBuscar(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.buscar(texto.obtenerContenido()).toArray()).isNotEmpty();
	}

	@Property
	public void testBuscarSinResultado(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.buscar("image").toArray()).isEmpty();
	}

}
