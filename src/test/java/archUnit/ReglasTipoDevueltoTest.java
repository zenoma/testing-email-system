package archUnit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "gal.udc.fic.vvs.email", importOptions = { ImportOption.DoNotIncludeTests.class })

public class ReglasTipoDevueltoTest {

	@ArchTest
	public static ArchRule metodosConLaPalabraNombreOContenidoOPreVisualizacionDevuelvenString = methods().that()
			.haveNameMatching(".*Nombre").and().haveNameMatching(".*Contenido").and()
			.haveNameMatching(".*PreVisualizacion").should().haveRawReturnType(String.class);

	@ArchTest
	public static ArchRule metodosConPalabraTamañoDevulevenEntero = methods().that().haveNameMatching(".*Tamaño")
			.should().haveRawReturnType("int");
}