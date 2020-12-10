package gal.udc.fic.vvs.email.archivador;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestLog {
	@Provide
	Arbitrary<Texto> textoProvider() {
		Arbitrary<String> texts = Arbitraries.strings().alpha();
		Arbitrary<String> contents = Arbitraries.strings().alpha();
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Provide
	Arbitrary<String> stringProvider() {
		return Arbitraries.strings().alpha();
	}

	@Provide
	Arbitrary<Integer> integerProvider() {
		return Arbitraries.integers().greaterOrEqual(10);
	}

	@Property(tries = 100)
	public void testAlmacenarCorreoYObtenerEspacioDisponible(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivador = new ArchivadorSimple(nombre, size);
		Correo msg = new Mensaje(new Texto("a", "b"));
		Log log = new Log(archivador);
		log.almacenarCorreo(msg);
		Assertions.assertThat(log.obtenerEspacioDisponible())
				.isEqualTo(log.obtenerEspacioTotal() - msg.obtenerTamaño());
	}

	@Example
	public void testNoAlmacenarCorreo(@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorPequeño = new ArchivadorSimple("Some name", 0);
		Log logPequeño = new Log(archivadorPequeño);
		Assertions.assertThat(logPequeño.almacenarCorreo(msg)).isFalse();
	}

}
