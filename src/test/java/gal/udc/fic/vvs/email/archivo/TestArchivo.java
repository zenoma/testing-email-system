package gal.udc.fic.vvs.email.archivo;

import org.assertj.core.api.Assertions;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
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

	@Property
	public void testObtenerNombre(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		audio = new Audio(nombre, contenido);
		Assertions.assertThat(audio.obtenerNombre()).isEqualTo(nombre);
	}

	@Property
	public void testObtenerContenido(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		Assertions.assertThat(imagen.obtenerContenido()).isEqualTo(contenido);
	}

	@Property
	public void testObtenerTamaño(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		Assertions.assertThat(imagen.obtenerTamaño()).isEqualTo(contenido.length());
	}

	@Property
	public void testObtenerMimeType(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		imagen = new Imagen(nombre, contenido);
		String expected = "image/png";
		Assertions.assertThat(imagen.obtenerMimeType()).isEqualTo(expected);
	}

	@Property
	public void testObtenerAudioMimeType(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		audio = new Audio(nombre, contenido);
		String expected = "audio/ogg";
		Assertions.assertThat(audio.obtenerMimeType()).isEqualTo(expected);
	}

	@Property
	public void testObtenerPreVisualizacion(@ForAll("strings") String nombre, @ForAll("strings") String contenido) {
		texto = new Texto(nombre, contenido);
		Assertions.assertThat(texto.obtenerPreVisualizacion().length()).isGreaterThan(texto.obtenerNombre().length());
	}
}
