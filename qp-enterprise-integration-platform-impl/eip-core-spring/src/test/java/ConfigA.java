import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigA {
	public static final String DEFAULT_A = "DEFAULT_A";
	private String a;

	public ConfigA(final String a) {
		this.a = a;
	}

	public ConfigA() {
		this(DEFAULT_A);
	}

	@Bean(name = "ConfigAStringBean")
	public String getStringBean() {
		return new StringBuffer().append(this.a).toString();
	}

	public String getA() {
		return this.a;
	}

	public void setA(final String a) {
		this.a = a;
	}
}
