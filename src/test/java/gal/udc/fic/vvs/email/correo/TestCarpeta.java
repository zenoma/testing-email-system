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

public class TestCarpeta {

	private static Carpeta carpetaImportantes;
	private static Carpeta carpetaVacia;

	@Provide
	Arbitrary<Mensaje> mensajeProvider() {
		Texto texto = new Texto(Arbitraries.strings().sample(), Arbitraries.strings().alpha().sample());
		return Combinators.withBuilder(() -> new Mensaje(texto)).build();
	}

	@Provide
	Arbitrary<Carpeta> carpetaProvider() {
		Arbitrary<String> nombre = Arbitraries.strings();
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

	@BeforeProperty
	@BeforeExample
	public void setUpTest() {
		carpetaImportantes = new Carpeta(Arbitraries.strings().alpha().sample());
		carpetaVacia = new Carpeta(Arbitraries.strings().sample());
	}

	@Property
	public void testAñadirCorreoYBuscarCorreo(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		carpetaImportantes.añadir(msg);

		for (Object correo : carpetaImportantes.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isTrue();
	}

	@Property
	public void testMoverCorreoDeCarpeta(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		carpetaImportantes.añadir(msg);
		for (Object correo : carpetaImportantes.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}

		if (result) {
			carpetaVacia.añadir(msg);
			for (Object correo : carpetaVacia.buscar(msg.obtenerVisualizacion())) {
				if (correo instanceof Correo) {
					result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
				}
			}
		}
		Assertions.assertThat(result).isTrue();
	}

	@Property
	public void testEliminarCorreoDeCarpeta(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		carpetaImportantes.añadir(msg);
		carpetaImportantes.eliminar(msg);
		for (Object correo : carpetaImportantes.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isFalse();
	}

	@Property
	public void testEliminarCorreoNoExistente(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		carpetaImportantes.eliminar(msg);
		Assertions.assertThat(msg.obtenerPadre()).isNull();
	}

	@Property
	public void testExplorarCarpetaVacia(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		for (Object correo : carpetaVacia.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isFalse();
	}

	@Example
	public void testExplorarCarpetaConElementos() throws OperacionInvalida {
		Mensaje mensaje = new Mensaje(new Texto("nombre", "Algun contenido"));
		Mensaje mensaje2 = new Mensaje(new Texto("nombre2", "Algun contenido"));
		carpetaImportantes.añadir(mensaje);
		carpetaImportantes.añadir(mensaje2);
		Assertions.assertThat(carpetaImportantes.buscar("contenido").toArray()).hasSize(2);
	}

	@Property
	public void testObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje msg, @ForAll("mensajeProvider") Mensaje msg2)
			throws OperacionInvalida {
		carpetaImportantes.añadir(msg);
		msg2.establecerLeido(true);
		carpetaImportantes.añadir(msg2);
		Assertions.assertThat(carpetaImportantes.obtenerNoLeidos()).isEqualTo(1);

	}

	@Example
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje msg, @ForAll("mensajeProvider") Mensaje msg2)
			throws OperacionInvalida {
		carpetaImportantes.añadir(msg);
		carpetaImportantes.añadir(msg2);
		Assertions.assertThat(carpetaImportantes.obtenerTamaño()).isNotZero();

	}

	@Example
	public void testObtenerTamañoCarpetaVacia() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantes.obtenerTamaño()).isZero();
	}

	@Example
	public void testObtenerIcono() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantes.obtenerIcono()).isEqualTo(Correo.ICONO_CARPETA);
	}

	@Example
	public void testObtenerVisualización(@ForAll("mensajeProvider") Mensaje msg,
			@ForAll("mensajeProvider") Mensaje msg2) throws OperacionInvalida {
		Carpeta carpeta = new Carpeta("Importantes");
		carpeta.añadir(msg);
		carpeta.añadir(msg2);
		String expected = "Importantes (" + carpeta.obtenerNoLeidos() + ")";
		Assertions.assertThat(carpeta.obtenerVisualizacion()).isEqualTo(expected);
	}

	@Property
	public void testObtenerPreVisualización(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		carpetaImportantes.añadir(msg);
		Assertions.assertThat(carpetaImportantes.obtenerPreVisualizacion())
				.isEqualTo(carpetaImportantes.obtenerVisualizacion());
	}

	@Property
	public void testObtenerVisualizaciónCarpetaVacia() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantes.obtenerPreVisualizacion())
				.isEqualTo(carpetaImportantes.obtenerVisualizacion());
	}

	@Property
	public void testEstablecerCarpetaLeida(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {
		carpeta.establecerLeido(true);
		Assertions.assertThat(carpeta.obtenerNoLeidos()).isZero();
	}
}
