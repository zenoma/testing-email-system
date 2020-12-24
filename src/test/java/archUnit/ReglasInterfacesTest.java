package archUnit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import java.util.HashSet;
import java.util.Set;

import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.core.importer.Locations;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.LocationProvider;
import com.tngtech.archunit.lang.ArchRule;

import gal.udc.fic.vvs.email.archivador.Archivador;
import gal.udc.fic.vvs.email.cliente.Cliente;
import gal.udc.fic.vvs.email.correo.Correo;

@AnalyzeClasses(locations = ReglasInterfacesTest.IntefaceClasses.class)
public class ReglasInterfacesTest {

	@ArchTest
	static final ArchRule interfacesNoAcabanConLaPalabraInterfaz = noClasses().that().areInterfaces().should()
			.haveNameMatching(".*Interfaz");

	@ArchTest
	static final ArchRule interfacesNoContienenLaPalabraInterfaz = noClasses().that().areInterfaces().should()
			.haveSimpleNameContaining("Interfaz");

	@ArchTest
	static final ArchRule interfacesNoEstanEnPaquetesDeImplementacion = noClasses().that().resideInAPackage("..impl..")
			.should().beInterfaces();

	public static class IntefaceClasses implements LocationProvider {
		@Override
		public Set<Location> get(Class<?> testClass) {
			Set<Location> result = new HashSet<>();
			result.addAll(Locations.ofClass(Archivador.class));
			result.addAll(Locations.ofClass(Cliente.class));
			result.addAll(Locations.ofClass(Correo.class));
			return result;
		}
	}
}