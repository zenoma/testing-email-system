package gal.udc.fic.vvs.email;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.Positive;

public class ExampleBTest {

	@Property
	public boolean testSmth(@ForAll @Positive int anInteger) {
		return Math.abs(anInteger) == anInteger;
	}
}
