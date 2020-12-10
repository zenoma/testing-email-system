package gal.udc.fic.vvs.email.correo;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivo.Texto;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.lifecycle.BeforeExample;
import net.jqwik.api.lifecycle.BeforeProperty;

public class TestCabecera {

	private final String NOMBRE_VACIO = "";
	private static String VALOR_VACIO = "";

	private static Cabecera cabecera;

	private static Cabecera cabeceraVacia;
	private static Mensaje mensajeVacio;
	private static Texto textoVacio;

	@Provide
	Arbitrary<String> strings() {
		return Arbitraries.strings().alpha();
	}

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = new Texto(Arbitraries.strings().sample(), Arbitraries.strings().alpha().sample());
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@BeforeProperty
	@BeforeExample
	public void setUpTest() {
		textoVacio = new Texto("", "");
		mensajeVacio = new Mensaje(textoVacio);
		cabeceraVacia = new Cabecera(mensajeVacio, NOMBRE_VACIO, VALOR_VACIO);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg    Mensaje para obtener tamaño
	 * @param nombre Nombre para obtener tamaño
	 * @param valor  Valor para obtener tamaño
	 */
	@Property
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje msg, @ForAll("strings") String nombre,
			@ForAll("strings") String valor) {
		cabecera = new Cabecera(msg, nombre, valor);
		Assertions.assertThat(msg.obtenerTamaño() + nombre.length() + valor.length())
				.isEqualTo(cabecera.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos: Generados automáticamente
	 * </pre>
	 * 
	 * @param msg    Mensaje para obtener visualización
	 * @param nombre Nombre para obtener visualización
	 * @param valor  Valor para obtener visualización
	 */
	@Property
	public void testObtenerVisualizacion(@ForAll("mensajeProvider") Mensaje msg, @ForAll("strings") String nombre,
			@ForAll("strings") String valor) {
		cabecera = new Cabecera(msg, nombre, valor);

		Assertions.assertThat(nombre + ": " + valor + "\n" + msg.obtenerVisualizacion())
				.isEqualTo(cabecera.obtenerVisualizacion());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera
	 * </pre>
	 */
	@Example
	public void testObtenerTamañoCabeceraVacio() {

		Assertions.assertThat(mensajeVacio.obtenerTamaño() + NOMBRE_VACIO.length() + VALOR_VACIO.length())
				.isEqualTo(cabeceraVacia.obtenerTamaño());
	}

	/**
	 * <pre>
	 * Nivel de prueba: Unidad 
	 * Categoría: Dinámicas, Caja Blanca, Negativa
	 * Selección de datos: Valores frontera
	 * </pre>
	 */
	@Example
	public void testObtenerVisualizacionCabeceraVacio() {
		Assertions.assertThat(": \n").isEqualTo(cabeceraVacia.obtenerVisualizacion());
	}

}
