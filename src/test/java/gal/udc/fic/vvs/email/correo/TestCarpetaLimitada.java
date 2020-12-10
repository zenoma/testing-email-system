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

public class TestCarpetaLimitada {
	private static Carpeta carpetaImportantes;
	private static Carpeta carpetaVacia;

	private static CarpetaLimitada carpetaImportantesLimitada;
	private static CarpetaLimitada carpetaVaciaLimitada;

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
		carpetaImportantesLimitada = new CarpetaLimitada(carpetaImportantes, 5);
		carpetaVacia = new Carpeta(Arbitraries.strings().sample());
		carpetaVaciaLimitada = new CarpetaLimitada(carpetaVacia, 0);
	}

	@Property
	public void testAñadirCorreoYBuscarCorreo(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		carpetaImportantesLimitada.añadir(msg);

		for (Object correo : carpetaImportantesLimitada.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isTrue();
	}

	@Property
	public void testAñadirCorreoYBuscarCorreoCarpetaVacia(@ForAll("mensajeProvider") Mensaje msg)
			throws OperacionInvalida {
		boolean result = false;
		carpetaVaciaLimitada.añadir(msg);

		for (Object correo : carpetaVaciaLimitada.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isTrue();
	}

	@Property
	public void testMoverCorreoDeCarpeta(@ForAll("mensajeProvider") Mensaje msg) throws OperacionInvalida {
		boolean result = false;
		carpetaImportantesLimitada.añadir(msg);
		for (Object correo : carpetaImportantesLimitada.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}

		if (result) {
			carpetaVaciaLimitada.añadir(msg);
			for (Object correo : carpetaVaciaLimitada.buscar(msg.obtenerVisualizacion())) {
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
		carpetaImportantesLimitada.añadir(msg);
		carpetaImportantesLimitada.eliminar(msg);
		for (Object correo : carpetaImportantesLimitada.buscar(msg.obtenerVisualizacion())) {
			if (correo instanceof Correo) {
				result |= (((Correo) correo).obtenerVisualizacion() == msg.obtenerVisualizacion());
			}
		}
		Assertions.assertThat(result).isFalse();
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
		Mensaje mensaje = new Mensaje(new Texto("nombre", "contenido"));
		Mensaje mensaje2 = new Mensaje(new Texto("nombre2", "contenido"));
		carpetaImportantesLimitada.añadir(mensaje);
		carpetaImportantesLimitada.añadir(mensaje2);
		Assertions.assertThat(carpetaImportantesLimitada.buscar("contenido").toArray()).hasSize(2);
	}

	@Example
	public void testObtenerNoLeidos(@ForAll("mensajeProvider") Mensaje msg, @ForAll("mensajeProvider") Mensaje msg2)
			throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(msg);
		msg2.establecerLeido(true);
		carpetaImportantesLimitada.añadir(msg2);
		Assertions.assertThat(carpetaImportantesLimitada.obtenerNoLeidos()).isEqualTo(1);
	}

	@Example
	public void testObtenerTamaño(@ForAll("mensajeProvider") Mensaje msg, @ForAll("mensajeProvider") Mensaje msg2)
			throws OperacionInvalida {
		carpetaImportantesLimitada.añadir(msg);
		carpetaImportantesLimitada.añadir(msg2);
		Assertions.assertThat(carpetaImportantesLimitada.obtenerTamaño()).isNotZero();
	}

	@Example
	public void testObtenerTamañoCarpetaVacia() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantesLimitada.obtenerTamaño()).isZero();
	}

	@Example
	public void testObtenerIcono() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantesLimitada.obtenerIcono()).isEqualTo(Correo.ICONO_CARPETA);
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
		carpetaImportantesLimitada.añadir(msg);
		Assertions.assertThat(carpetaImportantesLimitada.obtenerPreVisualizacion())
				.isEqualTo(carpetaImportantesLimitada.obtenerVisualizacion());
	}

	@Property
	public void testObtenerVisualizaciónCarpetaVacia() throws OperacionInvalida {
		Assertions.assertThat(carpetaImportantesLimitada.obtenerPreVisualizacion())
				.isEqualTo(carpetaImportantesLimitada.obtenerVisualizacion());
	}

	@Property
	public void testEstablecerCarpetaLeida(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {
		carpeta.establecerLeido(true);
		Assertions.assertThat(carpeta.obtenerNoLeidos()).isZero();
	}

	@Property
	public void testObtenerRuta(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {
		Assertions.assertThat(carpeta.obtenerRuta()).isEqualTo(carpeta.obtenerVisualizacion());
	}

	@Example
	public void testObtenerHijoCarpetaVacia() throws OperacionInvalida {
		Assertions.assertThatThrownBy(() -> {
			carpetaImportantesLimitada.obtenerHijo(0);
		}).isInstanceOf(ArrayIndexOutOfBoundsException.class);
	}

	@Property
	public void testObtenerPadre(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {
		Assertions.assertThat(carpeta.obtenerPadre()).isNull();
	}

	@Property
	public void testObtenerHijo(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {
		Assertions.assertThat(carpeta.obtenerHijo(0)).isNotNull();
	}

	@Property
	public void testCambiarPadreYObtenerPadre(@ForAll("carpetaProvider") Carpeta carpeta) throws OperacionInvalida {

		carpeta.establecerPadre(carpetaImportantesLimitada);
		Assertions.assertThat(carpeta.obtenerPadre()).isEqualTo(carpetaImportantesLimitada);
	}

}
