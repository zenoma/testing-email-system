package gal.udc.fic.vvs.email.archivo;

import org.assertj.core.api.Assertions;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestArchivo {

	private Audio audio;

	private Imagen imagen;

	private Texto texto;

	@Provide
	Arbitrary<String> strings() {
		return Arbitraries.strings().alpha();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Audio
	 * @param contenido Contenido para construir un Audio
	 */
	@Property
	public void testObtenerNombre(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		audio = new Audio(nombre, contenido);
		Assertions.assertThat(audio.obtenerNombre()).isEqualTo(nombre);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Property
	public void testObtenerContenido(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		Assertions.assertThat(imagen.obtenerContenido()).isEqualTo(contenido);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Property
	public void testObtenerTamaño(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		Assertions.assertThat(imagen.obtenerTamaño()).isEqualTo(contenido.length());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Example
	public void testObtenerMimeType(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		String expected = "image/png";
		Assertions.assertThat(imagen.obtenerMimeType()).isEqualTo(expected);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Example
	public void testObtenerAudioMimeType(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		audio = new Audio(nombre, contenido);
		String expected = "audio/ogg";
		Assertions.assertThat(audio.obtenerMimeType()).isEqualTo(expected);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Example
	public void testObtenerTextoMimeType(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		texto = new Texto(nombre, contenido);
		String expected = "text/plain";
		Assertions.assertThat(texto.obtenerMimeType()).isEqualTo(expected);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positivas
	 * Selección de datos: Generación automática de datos
	 * </pre>
	 * 
	 * @param nombre    Nombre para construir un Imagen
	 * @param contenido Contenido para construir un Imagen
	 */
	@Property
	public void testObtenerPreVisualizacion(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		texto = new Texto(nombre, contenido);
		Assertions.assertThat(texto.obtenerPreVisualizacion().length()).isGreaterThan(texto.obtenerNombre().length());
	}
}
