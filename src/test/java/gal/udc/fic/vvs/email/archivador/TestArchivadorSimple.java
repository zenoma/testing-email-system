package gal.udc.fic.vvs.email.archivador;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Vector;

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
		Arbitrary<String> texts = Arbitraries.strings().alpha().ofMinLength(1);
		Arbitrary<String> contents = Arbitraries.strings().alpha().ofMinLength(1);
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param size   Tamaño del archivador
	 */
	@Property
	public void testObtenerNombre(@ForAll("stringProvider") String nombre, @ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		assertEquals(nombre, archivadorSimple.obtenerNombre());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param size   Tamaño del archivador
	 */
	@Property
	public void testObtenerEspacioTotal(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Assertions.assertThat(archivadorSimple.obtenerEspacioTotal()).isEqualTo(size);

	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param size   Tamaño del archivador
	 */
	@Property
	public void testAlmacenarCorreoYObtenerEspacioDisponible(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Correo msg = new Mensaje(new Texto("a", "b"));
		archivadorSimple.almacenarCorreo(msg);
		Assertions.assertThat(archivadorSimple.obtenerEspacioDisponible()).isEqualTo(size - msg.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param size   Tamaño del archivador
	 */
	@Property
	public void testObtenerDelegadoNull(@ForAll("stringProvider") String nombre,
			@ForAll("integerProvider") Integer size) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, size);
		Assertions.assertThat(archivadorSimple.obtenerDelegado()).isNull();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 */
	@Example
	public void testNoAlmacenarCorreoArchivadorSinEspacio(@ForAll("stringProvider") String nombre,
			@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, 0);
		Assertions.assertThat(archivadorSimple.almacenarCorreo(msg)).isFalse();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 */
	@Example
	public void testNoAlmacenarCorreoSinEspacioSuficiente(@ForAll("stringProvider") String nombre,
			@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, 1);
		Assertions.assertThat(archivadorSimple.almacenarCorreo(msg)).isFalse();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 */
	@Example
	public void testAlmacenarCorreo(@ForAll("stringProvider") String nombre, @ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, 10000);
		Assertions.assertThat(archivadorSimple.almacenarCorreo(msg)).isTrue();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@Example
	public void testAlmacenarCorreoYComprobar(@ForAll("stringProvider") String nombre,
			@ForAll("mensajeProvider") Mensaje msg)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		Archivador archivadorSimple = new ArchivadorSimple(nombre, 10000);

		Field privateField = archivadorSimple.getClass().getDeclaredField("_correos");
		archivadorSimple.almacenarCorreo(msg);
		privateField.setAccessible(true);

		Vector fieldValue = (Vector) privateField.get(archivadorSimple);
		Assertions.assertThat(fieldValue.contains(msg)).isTrue();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 */
	@Property
	public void testAlmacenarCorreoYBuscar(@ForAll("stringProvider") String nombre,
			@ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, 10000);
		archivadorSimple.almacenarCorreo(msg);
		Assertions.assertThat(archivadorSimple.obtenerEspacioDisponible())
				.isEqualTo(archivadorSimple.obtenerEspacioTotal() - msg.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera y Generados automáticamente
	 * </pre>
	 * 
	 * @param nombre Nombre dedl archivador
	 * @param msg    Mensaje para almacecnar
	 */
	@Example
	public void testNoAlmacenarCorreo(@ForAll("stringProvider") String nombre, @ForAll("mensajeProvider") Mensaje msg) {
		Archivador archivadorSimple = new ArchivadorSimple(nombre, msg.obtenerTamaño());
		Assertions.assertThat(archivadorSimple.almacenarCorreo(msg)).isFalse();
	}

}
