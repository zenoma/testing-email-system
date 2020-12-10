package gal.udc.fic.vvs.email.archivador;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Mensaje;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestDelegado {

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
	Arbitrary<ArchivadorSimple> archivadorProvider() {
		String nombre = Arbitraries.strings().sample();
		int size = Arbitraries.integers().greaterOrEqual(0).sample();
		return Combinators.withBuilder(() -> new ArchivadorSimple(nombre, size)).build();
	}

	@Property
	public void testAlmacenarCorreoYObtenerEspacioDisponible(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		delegado.almacenarCorreo(msg);
		Assertions.assertThat(delegado.obtenerEspacioDisponible())
				.isEqualTo(delegado.obtenerEspacioTotal() - msg.obtenerTamaño());
	}

	@Property
	public void testObtenerDelegado(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		Assertions.assertThat(delegado.obtenerDelegado()).isNull();
	}

	@Property
	public void testEstablecerDelegadoYObtenerDelegado(@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		delegado.establecerDelegado(archivador);
		Assertions.assertThat(delegado.obtenerDelegado()).isEqualTo(archivador);
	}

	@Property
	public void testNoAlmacenarCorreo(@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 0);
		Delegado delegadoPequeño = new Delegado(archivador);
		delegadoPequeño.establecerDelegado(archivador);
		Assertions.assertThat(delegadoPequeño.almacenarCorreo(msg)).isFalse();
	}

}
