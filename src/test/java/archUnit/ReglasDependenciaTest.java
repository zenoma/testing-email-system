package archUnit;

import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "gal.udc.fic.vvs.email.*")
public class ReglasDependenciaTest {

	@ArchTest
	static final ArchRule noAccesoAPaquetesSuperiores = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
}
