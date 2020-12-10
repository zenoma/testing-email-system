package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;

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

public class TestArchivadorSimple {

	@Provide
	Arbitrary<String> stringProvider() {
		return Arbitraries.strings().alpha();
	}

	@Provide
	Arbitrary<Integer> integerProvider() {
		return Arbitraries.integers().greaterOrEqual(10);
	}

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

	@Property
	public void testObtenerNombre(@ForAll("stringProvider") String nombre, @ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		assertEquals(nombre, archivadorSimple.obtenerNombre());
	}

	@Property
	public void testObtenerEspacioTotal(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Assertions.assertThat(archivadorSimple.obtenerEspacioTotal()).isEqualTo(size);

	}

	@Property
	public void testAlmacenarCorreoYObtenerEspacioDisponible(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Correo msg = new Mensaje(new Texto("a", "b"));
		archivadorSimple.almacenarCorreo(msg);
		Assertions.assertThat(archivadorSimple.obtenerEspacioDisponible()).isEqualTo(size - msg.obtenerTamaño());
	}

	@Property
	public void testObtenerDelegadoNull(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Assertions.assertThat(archivadorSimple.obtenerDelegado()).isNull();
	}

	@Example
	public void testNoAlmacenarCorreo(@ForAll("stringProvider") String nombre, @ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, 0);
		Assertions.assertThat(archivadorSimple.almacenarCorreo(msg)).isFalse();
	}

}
