import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ ConfigA.class })
public class ConfigA2 {
	@Autowired
	private ConfigA configA;

	public ConfigA2() {
	}

	@PostConstruct
	private void init() {
		System.out.println("INIT in ConfigA2");
		this.configA.setA("This the A string set.");
	}
}
