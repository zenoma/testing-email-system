package jetm;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import etm.core.configuration.BasicEtmConfigurator;
import etm.core.configuration.EtmManager;
import etm.core.monitor.EtmMonitor;
import etm.core.renderer.SimpleTextRenderer;
import gal.udc.fic.vvs.email.archivo.Texto;
import gal.udc.fic.vvs.email.correo.Cabecera;
import gal.udc.fic.vvs.email.correo.Carpeta;
import gal.udc.fic.vvs.email.correo.CarpetaLimitada;
import gal.udc.fic.vvs.email.correo.Mensaje;
import gal.udc.fic.vvs.email.correo.OperacionInvalida;
import gal.udc.fic.vvs.email.correo.Reenvio;

public class JetmRunTest {
	private static EtmMonitor monitor;

	@Test
	public void appRunTest() throws OperacionInvalida {
		// configure measurement framework
		setup();

		// execute business logic
		try {
			Carpeta global = new Carpeta("O meu correo");
			Carpeta personal = new Carpeta("Persoal");
			Carpeta personalAmigos = new Carpeta("Amizades");
			Carpeta personalFamilia = new Carpeta("Familia");
			Carpeta trabajo = new Carpeta("Traballo");
			Carpeta trabajoVVS = new Carpeta("VVS");
			Carpeta trabajoDepartamento = new Carpeta("Departamento");
			Carpeta trabajoGeneral = new Carpeta("Xeral");
			Carpeta listas = new Carpeta("Listas");
			Carpeta listasPF = new Carpeta("Programación Funcional");
			Carpeta listasVA = new Carpeta("Validación Automática");

			global.añadir(listas);
			global.añadir(trabajo);
			global.añadir(new CarpetaLimitada(personal, 3));

			listas.añadir(listasPF);
			listas.añadir(listasVA);

			trabajo.añadir(trabajoVVS);
			trabajo.añadir(trabajoDepartamento);
			trabajo.añadir(trabajoGeneral);

			personal.añadir(personalAmigos);
			personal.añadir(personalFamilia);
			personal.añadir(new Mensaje(new Texto("contido", "Contido do correo electrónico.")));

			personalFamilia.añadir(new Reenvio(
					new Cabecera(
							new Cabecera(new Mensaje(new Texto("contido", "Contido do correo electrónico persoal 1.")),
									"Para", "lcastro@udc.es"),
							"De", "vvs@fic.udc.es"),
					new Mensaje(new Texto("contido", "Contido do correo electrónico familiar 1."))));
			personalFamilia.añadir(new Mensaje(new Texto("contido", "Contido do correo electrónico familiar 1.")));
			personalFamilia.añadir(new Mensaje(new Texto("contido", "Contido do correo electrónico familiar 2.")));
			personalFamilia.añadir(new Mensaje(new Texto("contido", "Contido do correo electrónico familiar 3.")));
			personalFamilia.añadir(new Mensaje(new Texto("contido", "Contido do correo electrónico familiar 4.")));

			personalFamilia.buscar("");
			personal.buscar("");
			trabajo.buscar("");
			listas.buscar("");
			global.buscar("");
			trabajoVVS.buscar("");

			Assertions.assertThat(monitor.isCollecting()).isTrue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// visualize results
		monitor.render(new SimpleTextRenderer());

		// shutdown measurement framework
		tearDown();
	}

	private static void setup() {
		BasicEtmConfigurator.configure(true);
		monitor = EtmManager.getEtmMonitor();
		monitor.start();
	}

	private static void tearDown() {
		monitor.stop();
	}

}
