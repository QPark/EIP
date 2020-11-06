import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.TreeSet;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 * @author bhausen
 */
public class CheckConnection {
	static class Dotter implements Runnable {
		boolean doit = true;

		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			while (this.doit) {
				System.out.print(".");
				CheckConnection.sleep(500);
			}
		}
	}

	static class ServerEntry implements Entry<String, Integer> {
		private String host;
		private Integer port;

		public ServerEntry(final String host, final Integer port) {
			this.host = host;
			this.port = port;

		}

		/**
		 * @see java.util.Map.Entry#getKey()
		 */
		@Override
		public String getKey() {
			return this.host;
		}

		/**
		 * @see java.util.Map.Entry#getValue()
		 */
		@Override
		public Integer getValue() {
			return this.port;
		}

		/**
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		@Override
		public Integer setValue(final Integer value) {
			return this.port;
		}

	}

	private static ArrayList<Properties> databaseProperties = new ArrayList<>();
	private static final String DRIVER = "DRIVER";
	private static final String DATASOURCE = "DATASOURCE";
	private static final String DRIVER_CLASS_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String DRIVER_CLASS_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DRIVER_CLASS_POSTGRES = "org.postgresql.Driver";
	private static final String PWD = "PWD";
	private static ArrayList<Entry<String, Integer>> servers = new ArrayList<>();
	private static final String URL = "URL";
	private static final String USER = "USER";
	private static final String VALIDATION_QUERY_ORACLE = "SELECT 1 FROM DUAL";
	private static final String VALIDATION_QUERY_SQLSERVER = "SELECT 1";
	public static String EIP_ENCRYPTOR_PWD_PROPERTY_NAME = "eip_jasypt_encryptor_password";
	private static final TreeSet<String> failed = new TreeSet<>();
	private static final TreeSet<String> success = new TreeSet<>();
	private static final String USE_DEFAULT_PORT = "USE_DEFAULT_PORT";

	private static void doCheckDatabase(final Properties p, final Properties fileProperties) {
		p.setProperty(DRIVER, setupDrivername(p.getProperty(DRIVER), p.getProperty(URL)));
		if (p.getProperty(DATASOURCE) != null) {
			System.out.println("\tDatasource             : " + p.getProperty(DATASOURCE));
		}
		System.out.println("\tUrl                    : " + p.getProperty(URL));
		System.out.println("\tUsername               : " + p.getProperty(USER));
		System.out.println("\tPassword               : " + p.getProperty(PWD));
		System.out.println("\tDriver                 : " + p.getProperty(DRIVER));
		try {
			String pwd = String.valueOf(p.getProperty(PWD));
			if (pwd != null && pwd.startsWith("ENC(") && pwd.endsWith(")")) {
				StrongTextEncryptor enc = new StrongTextEncryptor();
				enc.setPassword(getEncryptorPassword(fileProperties));
				pwd = enc.decrypt(pwd.substring(0, pwd.length() - 1).replace("ENC(", ""));
			}
			final boolean valid = doCheckDatabaseMetaData(p.getProperty(DRIVER), p.getProperty(URL),
					p.getProperty(USER), pwd);
			if (valid) {
				doCheckDatabaseValidtationQuery(p.getProperty(DRIVER), p.getProperty(URL), p.getProperty(USER), pwd);
			}
		} catch (EncryptionOperationNotPossibleException e) {
			doPrintError("Cannot decrypt password '" + p.getProperty(PWD) + "' of user " + p.getProperty(USER));
			parseUrl(p.getProperty(URL)).ifPresent(server -> doCheckSocketConnection(server));
		}
	}

	private static boolean doCheckDatabaseMetaData(final String driver, final String url, final String user,
			final String pwd) {
		boolean valid = false;
		Connection connection = null;
		try {
			connection = getConnection(driver, url, user, pwd);
			final DatabaseMetaData metadata = connection.getMetaData();
			System.out.println("\t\tDatabaseProductName    : " + metadata.getDatabaseProductName());
			System.out.println("\t\tDatabaseProductVersion : " + metadata.getDatabaseProductVersion());
			System.out.println("\t\tMeta-Url               : " + metadata.getURL());
			System.out.println("\t\tMeta-UserName          : " + metadata.getUserName());
			System.out.println("\t\tMeta-DriverName        : " + metadata.getDriverName());
			System.out.println("\t\tMeta-DriverVersion     : " + metadata.getDriverVersion());
			valid = true;
		} catch (final Exception e) {
			doPrintError(e.getMessage());
			parseUrl(url).ifPresent(server -> doCheckSocketConnection(server));
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException e) {
					// be quiet.
				}
			}
		}
		return valid;
	}

	private static void doCheckDatabaseValidtationQuery(final String driver, final String url, final String user,
			final String pwd) {
		Connection connection = null;
		Statement statement = null;
		try {
			String validationQuery = null;
			if (url != null && url.trim().length() > 0) {
				if (url.toLowerCase().contains("oracle")) {
					validationQuery = VALIDATION_QUERY_ORACLE;
				} else if (url.toLowerCase().contains("sqlserver")) {
					validationQuery = VALIDATION_QUERY_SQLSERVER;
				}
			}
			if (validationQuery != null) {
				connection = getConnection(driver, url, user, pwd);
				statement = connection.createStatement();
				final ResultSet rs = statement.executeQuery(validationQuery);
				System.out.println("\t\tExecute query          : " + validationQuery);
				if (rs != null && rs.getMetaData() != null) {
					final int columns = rs.getMetaData().getColumnCount();
					System.out.println("\t\tResult columns         : " + columns);
					if (columns > 0 && rs.next()) {
						System.out.println("\t\tResult column 1        : " + String.valueOf(rs.getObject(1)));
					}
				}
			}
		} catch (final Exception e) {
			doPrintError(e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (final SQLException e) {
					// be quiet.
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (final SQLException e) {
					// be quiet.
				}
			}
		}
	}

	private static void doCheckSocketConnection(final Entry<String, Integer> server) {
		final Dotter d = new Dotter();
		final Thread t = new Thread(d);
		try {
			System.out.print("\tConnecting to          : " + server.getKey() + ":" + server.getValue() + "\t\t");
			t.start();
			final Socket socket = new Socket();
			socket.connect(new InetSocketAddress(server.getKey(), server.getValue()), 10000);
			d.doit = false;
			try {
				t.join();
				socket.close();
			} catch (final InterruptedException ex) {
				socket.close();
			}
			System.out.println("\tconnected");
			try {
				final InetAddress address = InetAddress.getByName(server.getKey());
				if (isIpAddress(server.getKey()) && !server.getKey().equals(address.getHostName())) {
					System.out.println("\tConneced to            : " + address.getHostName());
				} else {
					System.out.println("\tConneced to            : " + address.getHostAddress());
				}
			} catch (final Exception e) {
				// nothing
			}
			success.add(String.format("%s:%d", server.getKey(), server.getValue()));
		} catch (final Exception e) {
			d.doit = false;
			try {
				t.join();
			} catch (final InterruptedException ex) {
			}
			System.out.println();
			failed.add(String.format("%s:%d", server.getKey(), server.getValue()));
			doPrintError(e.getMessage());
		}
	}

	/**
	 * @param string
	 */
	private static void doListenOnPort(final String string) {
		try {

		} catch (final Exception e) {
			// TODO: handle exception
		}
	}

	private static void doPrintError(final String message) {
		System.out.println("##### ERROR: " + message);
		CheckConnection.sleep(900);
	}

	private static void doPrintLocalhost() {
		try {
			final InetAddress localaddr = InetAddress.getLocalHost();
			System.out.println("Local IP address       : " + localaddr.getHostAddress());
			System.out.println("Local hostname         : " + localaddr.getHostName());
		} catch (final UnknownHostException e) {
			doPrintError("Can't detect localhost! " + e.getMessage());
		}
	}

	private static void doPrintSeparator() {
		System.out.println("----------------------------------------");
	}

	private static void doPrintUsage() {
		final String s = CheckConnection.class.getName();
		final StringBuffer sb = new StringBuffer(1024);

		sb.append("Usage:\n");
		sb.append("\t\tjava ").append(s).append(" <file>\n");
		sb.append("\tor\n");
		sb.append("\t\tjava ").append(s).append(" <server>:<port>\n");
		sb.append("\tor\n");
		sb.append("\t\tjava ").append(s).append(" <server> <port>\n");
		sb.append("\tor\n");
		sb.append("\t\tjava ").append(s).append(" <url> <username> <password>\n");
		sb.append("\tor\n");
		sb.append("\t\tjava ").append(s).append(" <url> <username> <password> <driverclassname>");
		System.out.println(sb);
	}

	private static Connection getConnection(final String driver, final String url, final String user, final String pwd)
			throws Exception {
		Class.forName(driver);
		final Connection connection = DriverManager.getConnection(url, user, pwd);
		return connection;
	}

	private static int getDefaultPort(final String url) {
		int port = Optional.ofNullable(url).map(u -> {
			if (u.trim().startsWith("https")) {
				return 443;
			} else if (u.trim().startsWith("http")) {
				return 80;
			} else if (u.trim().startsWith("sftp") || u.trim().startsWith("ssh")) {
				return 22;
			} else if (u.trim().contains("jdbc")) {
				if (u.trim().toLowerCase().contains("postgres")) {
					return 5432;
				} else if (u.trim().toLowerCase().contains("sqlserver")) {
					return 1433;
				} else if (u.trim().toLowerCase().contains("oracle")) {
					return 1521;
				} else if (u.trim().toLowerCase().contains("mysql")) {
					return 3306;
				}
			} else if (u.trim().startsWith("ldaps")) {
				return 636;
			} else if (u.trim().startsWith("ldap")) {
				return 389;
			} else if (u.trim().startsWith("redis")) {
				return 6379;
			}
			return 0;
		}).orElse(0);
		return port;
	}

	/**
	 * Get the encryptor password from Environment, system properties or properties.
	 *
	 * @param properties
	 *                       the {@link Properties}.
	 * @return the password.
	 */
	public static String getEncryptorPassword(final Properties properties) {
		String pwd = Optional.ofNullable(System.getenv(EIP_ENCRYPTOR_PWD_PROPERTY_NAME)).orElse(null);
		if (Objects.isNull(pwd)) {
			pwd = System.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME);
			if (Objects.isNull(pwd)) {
				pwd = Optional.ofNullable(properties).map(p -> p.getProperty(EIP_ENCRYPTOR_PWD_PROPERTY_NAME))
						.orElse(null);
			}
		}
		if (Objects.isNull(pwd)) {
			pwd = "eip";
		}
		return pwd;
	}

	private static boolean isIpAddress(final String s) {
		boolean address = false;
		address = s != null && s.split("\\.").length == 4 && s.replaceAll("\\.", "").matches("^\\d*?$");
		return address;
	}

	public static void main(final String[] args) {
		try {
			Properties allProperties = new Properties();
			if (args == null || args.length == 0) {
				doPrintUsage();
			} else if (args.length == 1) {
				final String s = args[0].trim();
				final File f = new File(s);
				if (f.exists() && f.isDirectory()) {
					allProperties.putAll(parseDirectory(f));
				} else if (f.exists() && f.isFile()) {
					allProperties.putAll(parseFile(f));
				} else if (s.indexOf(':') > 0 && s.indexOf(':') < s.length()) {
					if (s.startsWith("server:")) {
						doListenOnPort(s.substring(s.indexOf(':') + 1));
					} else {
						parseUrlAndCheck(s).ifPresent(server -> servers.add(server));
					}
				}
			} else if (args.length == 2) {
				if (args[0].trim().equals("server")) {
					doListenOnPort(args[1]);
				} else {
					parseUrlAndCheck(args[0].trim() + ":" + args[1].trim()).ifPresent(server -> servers.add(server));
				}
			} else if (args.length == 3) {
				final Properties p = new Properties();
				databaseProperties.add(p);
				p.setProperty(URL, args[0].trim());
				p.setProperty(USER, args[1].trim());
				p.setProperty(PWD, args[2].trim());
			} else if (args.length == 4) {
				final Properties p = new Properties();
				databaseProperties.add(p);
				p.setProperty(URL, args[0].trim());
				p.setProperty(USER, args[1].trim());
				p.setProperty(PWD, args[2].trim());
				p.setProperty(DRIVER, args[3].trim());
			} else {
				doPrintUsage();
			}
			doPrintSeparator();
			doPrintLocalhost();
			if (databaseProperties.size() > 0) {
				doPrintSeparator();
				doPrintSeparator();
				System.out.println("Check databases");
				databaseProperties.stream().forEach(dbp -> {
					doPrintSeparator();
					doCheckDatabase(dbp, allProperties);
				});
			}
			if (!servers.isEmpty()) {
				doPrintSeparator();
				doPrintSeparator();
				System.out.println("Check servers");
				for (final Entry<String, Integer> server : servers) {
					doPrintSeparator();
					doCheckSocketConnection(server);
				}
			}
		} catch (final Exception e) {
			doPrintError(e.getMessage());
		}
		if (success.size() > 0) {
			doPrintSeparator();
			doPrintSeparator();
			System.out.println("Successfully connected:");
			doPrintSeparator();
			success.stream().forEach(s -> System.out.println(String.format("\t%s", s)));
		}
		if (failed.size() > 0) {
			doPrintSeparator();
			doPrintSeparator();
			System.out.println("Failed connection attempt:");
			doPrintSeparator();
			failed.stream().forEach(s -> System.out.println(String.format("\t%s", s)));
		}
		doPrintSeparator();
	}

	private static Properties parseDirectory(final File dir) throws Exception {
		final Properties properties = new Properties();
		if (dir.isDirectory()) {
			for (final File f : dir.listFiles()) {
				if (f.isDirectory()) {
					properties.putAll(parseDirectory(f));
				} else if (f.isFile()) {
					properties.putAll(parseFile(f));
				}
			}
		}
		return properties;
	}

	private static Properties parseFile(final File f) throws Exception {
		final Properties properties = new Properties();
		if (f.exists()) {
			if (f.getName().endsWith(".properties")) {
				properties.load(new FileInputStream(f));
				String key;
				String value;

				for (final Entry<Object, Object> en : properties.entrySet()) {
					key = String.valueOf(en.getKey());
					value = String.valueOf(en.getValue());
					if (key.endsWith(".host")) {
						final String hostDesc = key.substring(0, key.indexOf(".host"));
						for (final Entry<Object, Object> second : properties.entrySet()) {
							final String keyPort = String.valueOf(second.getKey());
							if (keyPort.startsWith(hostDesc) && keyPort.endsWith(".port")) {
								parseUrlAndCheck(value + ":" + String.valueOf(second.getValue()))
										.ifPresent(server -> servers.add(server));
								break;
							}
						}
					} else if (value.contains("://")) {
						parseUrlAndCheck(value).ifPresent(server -> servers.add(server));
					}
				}

				properties.entrySet().stream().filter(p -> p.getKey() != null && p.getValue() != null
						&& String.valueOf(p.getValue()).contains("jdbc")).forEach(px -> {
							final Properties dbp = new Properties();
							dbp.setProperty(URL, String.valueOf(px.getValue()));
							final String kx = String.valueOf(px.getKey());
							if (kx.toLowerCase().endsWith(".url")) {
								final String keyStart = kx.toLowerCase().replace(".url", "");
								properties.entrySet().stream()
										.filter(p -> p.getKey() != null && p.getValue() != null
												&& String.valueOf(p.getKey()).toLowerCase().startsWith(keyStart)
												&& String.valueOf(p.getKey()).toLowerCase().contains("driver"))
										.findAny().map(p -> dbp.setProperty(DRIVER, String.valueOf(p.getValue())))
										.orElse("");
								properties.entrySet().stream()
										.filter(p -> p.getKey() != null && p.getValue() != null
												&& String.valueOf(p.getKey()).toLowerCase().startsWith(keyStart)
												&& (String.valueOf(p.getKey()).toLowerCase().contains("username")
														|| String.valueOf(p.getKey()).toLowerCase().contains("user")))
										.findAny().map(p -> dbp.setProperty(USER, String.valueOf(p.getValue())))
										.orElse("");
								properties.entrySet().stream()
										.filter(p -> p.getKey() != null && p.getValue() != null
												&& String.valueOf(p.getKey()).toLowerCase().startsWith(keyStart)
												&& (String.valueOf(p.getKey()).toLowerCase().contains("password")
														|| String.valueOf(p.getKey()).toLowerCase().contains("pwd")))
										.findAny().map(p -> dbp.setProperty(PWD, String.valueOf(p.getValue()).trim()))
										.orElse("");
								databaseProperties.add(dbp);
							}
						});

			} else if (f.getName().endsWith("server.xml")) {
				String xml = readFile(f);
				int index = xml.indexOf("<Resource");
				int i, j;
				if (index > 0) {
					String resource;
					String user;
					String pwd;
					String url;
					String driver;
					String datasource;
					while (index > 0) {
						resource = null;
						user = null;
						pwd = null;
						url = null;
						driver = null;
						datasource = null;
						xml = xml.substring(index + 1);
						index = xml.indexOf("/>");
						if (index > 0) {
							resource = xml.substring(0, index);
							i = resource.indexOf(" username=\"");
							if (i > 0) {
								i += " username=\"".length();
								j = resource.indexOf('"', i + 1);
								user = resource.substring(i, j);
							}
							i = resource.indexOf(" password=\"");
							if (i > 0) {
								i += " password=\"".length();
								j = resource.indexOf('"', i + 1);
								pwd = resource.substring(i, j);
							}
							i = resource.indexOf(" url=\"");
							if (i > 0) {
								i += " url=\"".length();
								j = resource.indexOf('"', i + 1);
								url = resource.substring(i, j);
							}
							i = resource.indexOf(" driverClassName=\"");
							if (i > 0) {
								i += " driverClassName=\"".length();
								j = resource.indexOf('"', i + 1);
								driver = resource.substring(i, j);
							}
							i = resource.indexOf(" name=\"");
							if (i > 0) {
								i += " name=\"".length();
								j = resource.indexOf('"', i + 1);
								datasource = resource.substring(i, j);
							}
							if (url != null) {
								final Properties p = new Properties();
								databaseProperties.add(p);
								p.setProperty(URL, url);
								p.setProperty(USER, user);
								p.setProperty(PWD, pwd.trim());
								p.setProperty(DRIVER, driver);
								p.setProperty(DATASOURCE, datasource);
								parseUrlAndCheck(url);
							}
						}
						index = xml.indexOf("<Resource");
					}
				}
			}
		}
		return properties;
	}

	private static int parsePort(final String portString, final int defaultPort) {
		int port = defaultPort;
		String s = portString;
		if (s != null && !s.equals(USE_DEFAULT_PORT)) {
			if (s.indexOf('/') > 0) {
				s = s.substring(0, s.indexOf('/'));
			} else if (s.indexOf(';') > 0) {
				s = s.substring(0, s.indexOf(';'));
			} else if (s.indexOf('?') > 0) {
				s = s.substring(0, s.indexOf('?'));
			}
			try {
				port = Integer.parseInt(s);
			} catch (final NumberFormatException e) {
				doPrintError("Can not parse '" + s + "' to a port number!");
			}
		}
		return port;
	}

	private static Optional<Entry<String, Integer>> parseUrl(final String url) {
		String host = null;
		Integer port = null;
		if (url != null) {
			String s = url.trim();
			if (s.contains("://")) {
				s = s.substring(s.indexOf("://") + 3);
			}
			if (s.indexOf(':') > 0 && s.indexOf(':') < s.length()) {
				host = s.substring(0, s.indexOf(':'));
				if (host.indexOf(';') > 0) {
					host = host.substring(0, host.indexOf(';'));
				}
				s = s.substring(s.indexOf(':') + 1);
				port = parsePort(s, getDefaultPort(url));
			} else if (s.indexOf('/') > 0 && s.indexOf('/') <= s.length()) {
				host = s.substring(0, s.indexOf('/'));
				port = parsePort(USE_DEFAULT_PORT, getDefaultPort(url));
			} else if (s.indexOf(';') > 0 && s.indexOf(';') <= s.length()) {
				host = s.substring(0, s.indexOf(';'));
				port = parsePort(USE_DEFAULT_PORT, getDefaultPort(url));
			} else {
				host = s;
				port = parsePort(USE_DEFAULT_PORT, getDefaultPort(url));
			}
		}
		if (host != null && port != null) {
			return Optional.of(new ServerEntry(host, port));
		}
		return Optional.empty();
	}

	private static Optional<Entry<String, Integer>> parseUrlAndCheck(final String url) {
		Optional<Entry<String, Integer>> server = parseUrl(url);
		if (server.isPresent()) {
			if (!servers.stream().filter(s -> s.getKey().equals(server.get().getKey()))
					.filter(s -> s.getValue().equals(server.get().getValue())).findAny().isPresent()) {
				return server;
			}
		}
		return Optional.empty();
	}

	private static String readFile(final File file) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		final StringBuilder stringBuilder = new StringBuilder();

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(" ");
		}
		reader.close();
		String s = stringBuilder.toString();
		s = s.replaceAll("\t", " ");
		while (s.indexOf("  ") > 0) {
			s = s.replaceAll("  ", " ");
		}
		return s;
	}

	private static String setupDrivername(final String driver, final String url) {
		String s = driver;
		if (s == null || s.trim().length() == 0) {
			if (url != null && url.trim().length() > 0) {
				if (url.toLowerCase().contains("oracle")) {
					s = DRIVER_CLASS_ORACLE;
				} else if (url.toLowerCase().contains("sqlserver")) {
					s = DRIVER_CLASS_SQLSERVER;
				} else if (url.toLowerCase().contains("postgresql")) {
					s = DRIVER_CLASS_POSTGRES;
				}
			}
		}
		if (s == null || s.trim().length() == 0) {
			s = DRIVER_CLASS_POSTGRES;
		}
		return s;
	}

	private static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
		}
	}
}
