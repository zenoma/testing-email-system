package gal.udc.fic.vvs.email;

import org.assertj.core.api.Assertions;

import gal.udc.fic.vvs.email.archivador.Archivador;
import gal.udc.fic.vvs.email.archivador.ArchivadorSimple;
import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Carpeta;
import gal.udc.fic.vvs.email.correo.Correo;
import gal.udc.fic.vvs.email.correo.Mensaje;
import gal.udc.fic.vvs.email.correo.OperacionInvalida;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

/**
 * Integration test for simple App.
 */
public class AppIntegrationTest {

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = new Texto(Arbitraries.strings().sample(), Arbitraries.strings().alpha().sample());
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Provide
	Arbitrary<Carpeta> carpetaProvider() {
		Arbitrary<String> nombre = Arbitraries.strings().alpha();
		return Combinators.withBuilder(() -> {
			Carpeta carpeta = new Carpeta(nombre.sample());
			for (int i = 0; i < Arbitraries.integers().between(1, 5).sample(); i++) {
				Mensaje msg = mensajeProvider().sample();
				try {
					msg.establecerLeido(Arbitraries.forType(Boolean.class).sample());
					carpeta.añadir(msg);
				} catch (OperacionInvalida e) {
					e.printStackTrace();
				}
			}
			return carpeta;
		}).build();
	}

	@Provide
	Arbitrary<Carpeta> carpetaVaciaProvider() {
		Arbitrary<String> nombre = Arbitraries.strings().alpha();
		return Combinators.withBuilder(() -> {
			Carpeta carpeta = new Carpeta(nombre.sample());
			return carpeta;
		}).build();
	}

	@Provide
	Arbitrary<Carpeta> carpetasAnidadaProvider() {
		return Combinators.withBuilder(() -> {
			Carpeta carpeta = carpetaVaciaProvider().sample();
			for (int i = 0; i < Arbitraries.integers().between(2, 7).sample(); i++) {
				Carpeta carpetaInterna = carpetaProvider().sample();
				try {
					carpeta.añadir(carpetaInterna);
				} catch (OperacionInvalida e) {
					e.printStackTrace();
				}
			}
			return carpeta;
		}).build();

	}

	@Provide
	Arbitrary<ArchivadorSimple> archivadorProvider() {
		String nombre = Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(15).sample();
		int size = Arbitraries.integers().greaterOrEqual(0).sample();
		return Combinators.withBuilder(() -> {
			ArchivadorSimple archivador = new ArchivadorSimple(nombre, size);
			return archivador;
		}).build();

	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados automáticamente
	 * </pre>
	 * 
	 */
	@Property
	public void testCrearCarpetaYObtenerRuta(@ForAll("carpetaVaciaProvider") Carpeta carpeta) {
		Assertions.assertThat(carpetaProvider().sample().obtenerRuta()).isNotEmpty();

	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados automáticamente
	 * </pre>
	 * 
	 */
	@Property
	public void testCrearCarpetaConCorreos(@ForAll("carpetaProvider") Carpeta carpeta) {
		Assertions.assertThat(carpeta.obtenerNoLeidos()).isNotNegative();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados automáticamente
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Property
	public void testCrearAnidadaConCorreos(@ForAll("carpetasAnidadaProvider") Carpeta carpeta)
			throws OperacionInvalida {
		Assertions.assertThat(carpeta.explorar()).isNotEmpty();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Example
	public void testBuscarTodosLosCorreos(@ForAll("carpetaVaciaProvider") Carpeta carpeta) throws OperacionInvalida {
		carpeta.añadir(new Mensaje(new Texto("Correo", "Contenido del mensaje de test")));
		carpeta.añadir(new Mensaje(new Texto("Mensaje", "Contenido del mensaje de test")));
		carpeta.añadir(new Mensaje(new Texto("Prueba", "Contenido de test")));
		Assertions.assertThat(carpeta.buscar("")).hasSize(3);
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@Property
	public void testMarcarCarpetaLeida(@ForAll("carpetasAnidadaProvider") Carpeta carpeta) throws OperacionInvalida {
		carpeta.establecerLeido(true);
		Assertions.assertThat(carpeta.obtenerNoLeidos()).isZero();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración 
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@Property
	public void testTamañoNotEmpty(@ForAll("carpetasAnidadaProvider") Carpeta carpeta) throws OperacionInvalida {
		Assertions.assertThat(carpeta.obtenerTamaño()).isNotZero();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Example
	public void testVaciarCarpetaVacia(@ForAll("carpetaVaciaProvider") Carpeta carpeta) throws OperacionInvalida {
		Correo c1 = new Mensaje(new Texto("Correo", "Contenido del mensaje de test"));
		carpeta.eliminar(c1);

		Assertions.assertThat(carpeta.explorar()).isEmpty();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@Property
	public void testDecoradorNoVacio(@ForAll("carpetasAnidadaProvider") Carpeta carpeta,
			@ForAll("archivadorProvider") Archivador archivador) throws OperacionInvalida {
		archivador.almacenarCorreo(carpeta);

		Assertions.assertThat(archivador.obtenerEspacioTotal()).isNotNegative();
	}

	/**
	 * <pre>
	 * Nivel de prueba: Integración
	 * Categoría: Dinámicas, Caja Blanca, Positiva
	 * Selección de datos:  Generados
	 * </pre>
	 * 
	 * @throws OperacionInvalida
	 * 
	 */
	@Property
	public void testLlenarDecorador(@ForAll("carpetasAnidadaProvider") Carpeta carpeta,
			@ForAll("archivadorProvider") Archivador archivador) throws OperacionInvalida {
		int total = archivador.obtenerEspacioTotal();
		archivador.almacenarCorreo(carpeta);

		Assertions.assertThat(total).isGreaterThanOrEqualTo(archivador.obtenerEspacioDisponible());
	}

}
