package gal.udc.fic.vvs.email.correo;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

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

	@Provide
	Arbitrary<Texto> textoProvider() {
		Arbitrary<String> texts = Arbitraries.strings();
		Arbitrary<String> contents = Arbitraries.strings();
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Property
	public void testObtenerTamaño(@ForAll("textoProvider") Texto texto, @ForAll("textoProvider") Texto nuevoTexto) {
		correoReenviado = new Mensaje(texto);
		mensajeNuevo = new Mensaje(nuevoTexto);
		reenvio = new Reenvio(mensajeNuevo, correoReenviado);
		Assertions.assertThat(texto.obtenerTamaño() + nuevoTexto.obtenerTamaño()).isEqualTo(reenvio.obtenerTamaño());
	}

	@Property
	public void testObtenerVisualizacion(@ForAll("textoProvider") Texto texto,
			@ForAll("textoProvider") Texto nuevoTexto) {
		correoReenviado = new Mensaje(texto);
		mensajeNuevo = new Mensaje(nuevoTexto);
		reenvio = new Reenvio(mensajeNuevo, correoReenviado);
		Assertions
				.assertThat(mensajeNuevo.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n"
						+ correoReenviado.obtenerVisualizacion() + "\n---- Fin correo reenviado ----")
				.isEqualTo(reenvio.obtenerVisualizacion());

	}

	@Property
	public void testObtenerTamañoReenvioVacio() {
		textoVacio = new Texto("", "");
		textoNuevoVacio = new Texto("", "");
		mensajeNuevoVacio = new Mensaje(textoNuevoVacio);
		correoReenviadoVacio = new Mensaje(textoVacio);
		reenvioVacio = new Reenvio(mensajeNuevoVacio, correoReenviadoVacio);
		Assertions.assertThat(textoVacio.obtenerTamaño() + textoNuevoVacio.obtenerTamaño())
				.isEqualTo(reenvioVacio.obtenerTamaño());
	}

	@Property
	public void testObtenerVisualizacionReenvioVacio() {
		textoVacio = new Texto("", "");
		textoNuevoVacio = new Texto("", "");
		mensajeNuevoVacio = new Mensaje(textoNuevoVacio);
		correoReenviadoVacio = new Mensaje(textoVacio);
		reenvioVacio = new Reenvio(mensajeNuevoVacio, correoReenviadoVacio);
		Assertions
				.assertThat(mensajeNuevoVacio.obtenerVisualizacion() + "\n\n---- Correo reenviado ----\n\n"
						+ correoReenviadoVacio.obtenerVisualizacion() + "\n---- Fin correo reenviado ----")
				.isEqualTo(reenvioVacio.obtenerVisualizacion());
	}

}
