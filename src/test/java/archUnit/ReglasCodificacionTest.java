package archUnit;

import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.CompositeArchRule;

@AnalyzeClasses(packages = "gal.udc.fic.vvs.email.*")
public class ReglasCodificacionTest {

	@ArchTest
	private final ArchRule noLanzarExcepcionesGenericas = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

	@ArchTest
	private final ArchRule noUsarJavaLoggin = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

	@ArchTest
	private final ArchRule noInjectarAtributos = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

	@ArchTest
	static final ArchRule noLanzarExcepcionesGenericasNiUsarStreams = CompositeArchRule
			.of(NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS).and(NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS);
}
