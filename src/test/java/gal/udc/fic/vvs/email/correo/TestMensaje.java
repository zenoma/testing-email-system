package gal.udc.fic.vvs.email.correo;

import static org.junit.Assert.assertEquals;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class TestMensaje {

	@Provide
	Arbitrary<Texto> textoProvider() {
		Arbitrary<String> texts = Arbitraries.strings().alpha();
		Arbitrary<String> contents = Arbitraries.strings().alpha();
		return Combinators.combine(texts, contents).as((text, content) -> new Texto(text, content));
	}

	private static Texto texto;

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		texto = textoProvider().sample();
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para establecer no leido
	 */
	@Property
	public void testEstablecerNoLeidoYObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(false);
		Assertions.assertThat(mensaje.obtenerNoLeidos()).isEqualTo(1);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para establecer leido
	 */
	@Property
	public void testEstablecerLeidoYObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(true);
		Assertions.assertThat(mensaje.obtenerNoLeidos()).isZero();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para obtener tamaño
	 */
	@Property
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.obtenerVisualizacion().length()).isEqualTo(mensaje.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para obtener icono
	 */
	@Example
	public void testObtenerIconoMensaje(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(true);
		Assertions.assertThat(mensaje.obtenerIcono()).isEqualTo(Correo.ICONO_MENSAJE);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para obtener icono
	 */
	@Example
	public void testObtenerIconoNuevoMensaje(@ForAll("mensajeProvider") Mensaje mensaje) {
		mensaje.establecerLeido(false);
		assertEquals(Correo.ICONO_NUEVO_MENSAJE, mensaje.obtenerIcono());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para obtener previsualización
	 */
	@Property
	public void testObtenerPreVisualizacion(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.obtenerPreVisualizacion()).endsWith("...");
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para obtener visualizacion
	 */
	@Property
	public void testObtenerVisualizacion(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(texto.obtenerContenido()).isEqualTo(mensaje.obtenerVisualizacion());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para buscar
	 */
	@Property
	public void testBuscar(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.buscar(texto.obtenerContenido()).toArray()).isNotEmpty();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para buscar
	 */
	@Example
	public void testBuscarSinResultado(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.buscar("image").toArray()).isEmpty();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para buscar
	 */
	@Example
	public void testObtenerRuta(@ForAll("mensajeProvider") Mensaje mensaje) {
		Assertions.assertThat(mensaje.obtenerRuta()).isEqualTo(mensaje.obtenerPreVisualizacion());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos:  Generación automática
	 * </pre>
	 * 
	 * @param mensaje Mensaje para buscar
	 */
	@Example
	public void testObtenerRutaConPadre(@ForAll("mensajeProvider") Mensaje mensaje,
			@ForAll("mensajeProvider") Mensaje padre) {
		mensaje.establecerPadre(padre);
		Assertions.assertThat(mensaje.obtenerRuta())
				.isEqualTo(padre.obtenerPreVisualizacion() + " > " + mensaje.obtenerPreVisualizacion());
	}

}
