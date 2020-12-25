package gal.udc.fic.vvs.email.archivador;

import java.lang.reflect.Field;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Mensaje;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
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
		String nombre = Arbitraries.strings().alpha().injectNull(0).sample();
		int size = Arbitraries.integers().greaterOrEqual(0).injectNull(0).sample();
		return Combinators.withBuilder(() -> new ArchivadorSimple(nombre, size)).build();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg        Mensaje par almacenar
	 * @param archivador Archivador para crear el delegado
	 */
	@Example
	public void testObtenerNombre(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		Assertions.assertThat(delegado.obtenerNombre()).isEqualTo(archivador.obtenerNombre());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg        Mensaje par almacenar
	 * @param archivador Archivador para crear el delegado
	 */
	@Property
	public void testObtenerDelegado(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		Assertions.assertThat(delegado.obtenerDelegado()).isNull();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param archivador Archivador para crear el delegado
	 */
	@Property
	public void testEstablecerDelegadoYObtenerDelegado(@ForAll("archivadorProvider") ArchivadorSimple archivador) {
		Delegado delegado = new Delegado(archivador);
		delegado.establecerDelegado(archivador);
		Assertions.assertThat(delegado.obtenerDelegado()).isEqualTo(archivador);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param archivador Archivador para crear el delegado
	 */
	@Property
	public void testNoAlmacenarCorreo(@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 0);
		Delegado delegadoPequeño = new Delegado(archivador);
		delegadoPequeño.establecerDelegado(archivador);
		Assertions.assertThat(delegadoPequeño.almacenarCorreo(msg)).isFalse();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param msg Mensaje para añadir al delegado
	 */
	@Property
	public void testAlmacenarCorreo(@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Delegado delegadoPequeño = new Delegado(archivador);
		archivador.establecerDelegado(archivador);
		Assertions.assertThat(delegadoPequeño.almacenarCorreo(msg)).isTrue();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 */
	@Example
	public void testObtenerDelegado() {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Delegado delegadoPequeño = new Delegado(archivador);
		Assertions.assertThat(delegadoPequeño.obtenerDelegado()).isEqualTo(archivador.obtenerDelegado());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 */
	@Example
	public void testEstablecerDelegadoNotNull() {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Delegado delegadoPequeño = new Delegado(archivador);
		delegadoPequeño.establecerDelegado(archivador);
		Assertions.assertThat(delegadoPequeño.obtenerDelegado()).isNotNull();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 */
	@Example
	public void testEstablecerNuevoDelegado() {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Archivador nuevoArchivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Delegado delegadoPequeño = new Delegado(archivador);
		delegadoPequeño.establecerDelegado(nuevoArchivador);
		Assertions.assertThat(nuevoArchivador.obtenerDelegado()).isEqualTo(archivador.obtenerDelegado());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * 
	 */
	@Example
	public void testEstablecerNuevoDelegadoYObtener()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Archivador archivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Archivador nuevoArchivador = new ArchivadorSimple(Arbitraries.strings().alpha().sample(), 10000);
		Delegado delegadoPequeño = new Delegado(archivador);

		Field privateField = DecoradorArchivador.class.getDeclaredField("_decorado");
		privateField.setAccessible(true);

		delegadoPequeño.establecerDelegado(nuevoArchivador);
		Archivador fieldValue = (Archivador) privateField.get(delegadoPequeño);

		Assertions.assertThat(fieldValue.obtenerDelegado()).isEqualTo(nuevoArchivador.obtenerDelegado());
	}

}
