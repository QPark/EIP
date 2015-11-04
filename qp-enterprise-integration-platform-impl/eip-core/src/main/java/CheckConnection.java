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
import java.util.Properties;

/**
 * @author bhausen
 */
public class CheckConnection {
	static class Dotter implements Runnable {
		boolean doit = true;;

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

	private static ArrayList<Properties> databaseProperties = new ArrayList<Properties>();
	private static final String DRIVER = "DRIVER";
	private static final String DATASOURCE = "DATASOURCE";

	private static final String DRIVER_CLASS_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String DRIVER_CLASS_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String PWD = "PWD";

	private static ArrayList<Entry<String, Integer>> servers = new ArrayList<Entry<String, Integer>>();

	private static final String URL = "URL";

	private static final String USER = "USER";

	private static final String VALIDATION_QUERY_ORACLE = "SELECT 1 FROM DUAL";

	private static final String VALIDATION_QUERY_SQLSERVER = "SELECT 1";

	private static void doCheckDatabase(final Properties p) {
		p.setProperty(DRIVER, setupDrivername(p.getProperty(DRIVER), p.getProperty(URL)));
		if (p.getProperty(DATASOURCE) != null) {
			System.out.println("\tDatasource             : " + p.getProperty(DATASOURCE));
		}
		System.out.println("\tUrl                    : " + p.getProperty(URL));
		System.out.println("\tUsername               : " + p.getProperty(USER));
		System.out.println("\tPassword               : " + p.getProperty(PWD));
		System.out.println("\tDriver                 : " + p.getProperty(DRIVER));
		boolean valid = doCheckDatabaseMetaData(p.getProperty(DRIVER), p.getProperty(URL), p.getProperty(USER),
				p.getProperty(PWD));
		if (valid) {
			doCheckDatabaseValidtationQuery(p.getProperty(DRIVER), p.getProperty(URL), p.getProperty(USER),
					p.getProperty(PWD));
		}
	}

	private static boolean doCheckDatabaseMetaData(final String driver, final String url, final String user,
			final String pwd) {
		boolean valid = false;
		Connection connection = null;
		try {
			connection = getConnection(driver, url, user, pwd);
			DatabaseMetaData metadata = connection.getMetaData();
			System.out.println("\t\tDatabaseProductName    : " + metadata.getDatabaseProductName());
			System.out.println("\t\tDatabaseProductVersion : " + metadata.getDatabaseProductVersion());
			System.out.println("\t\tMeta-Url               : " + metadata.getURL());
			System.out.println("\t\tMeta-UserName          : " + metadata.getUserName());
			System.out.println("\t\tMeta-DriverName        : " + metadata.getDriverName());
			System.out.println("\t\tMeta-DriverVersion     : " + metadata.getDriverVersion());
			valid = true;
		} catch (Exception e) {
			doPrintError(e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
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
				ResultSet rs = statement.executeQuery(validationQuery);
				System.out.println("\t\tExecute query          : " + validationQuery);
				if (rs != null && rs.getMetaData() != null) {
					int columns = rs.getMetaData().getColumnCount();
					System.out.println("\t\tResult columns         : " + columns);
					if (columns > 0 && rs.next()) {
						System.out.println("\t\tResult column 1        : " + String.valueOf(rs.getObject(1)));
					}
				}
			}
		} catch (Exception e) {
			doPrintError(e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// be quiet.
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// be quiet.
				}
			}
		}
	}

	private static void doCheckSocketConnection(final Entry<String, Integer> server) {
		Dotter d = new Dotter();
		Thread t = new Thread(d);
		try {
			System.out.print("\tConnecting to          : " + server.getKey() + ":" + server.getValue() + "\t\t");
			t.start();
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(server.getKey(), server.getValue()), 10000);
			d.doit = false;
			try {
				t.join();
				socket.close();
			} catch (InterruptedException ex) {
				socket.close();
			}
			System.out.println("\tconnected");
			try {
				InetAddress address = InetAddress.getByName(server.getKey());
				if (isIpAddress(server.getKey()) && !server.getKey().equals(address.getHostName())) {
					System.out.println("\tConneced to            : " + address.getHostName());
				} else {
					System.out.println("\tConneced to            : " + address.getHostAddress());
				}
			} catch (Exception e) {
				// nothing
			}
		} catch (Exception e) {
			d.doit = false;
			try {
				t.join();
			} catch (InterruptedException ex) {
			}
			System.out.println();
			doPrintError(e.getMessage());
		}
	}

	private static boolean isIpAddress(final String s) {
		boolean address = false;
		address = s != null && s.split("\\.").length == 4 && s.replaceAll("\\.", "").matches("^\\d*?$");
		return address;
	}

	private static void doPrintLocalhost() {
		try {
			InetAddress localaddr = InetAddress.getLocalHost();
			System.out.println("Local IP address       : " + localaddr.getHostAddress());
			System.out.println("Local hostname         : " + localaddr.getHostName());
		} catch (UnknownHostException e) {
			doPrintError("Can't detect localhost! " + e.getMessage());
		}
	}

	private static void doPrintSeparator() {
		System.out.println("----------------------------------------");
	}

	private static void doPrintError(final String message) {
		System.out.println("##### ERROR: " + message);
		CheckConnection.sleep(900);
	}

	private static void doPrintUsage() {
		String s = CheckConnection.class.getName();
		StringBuffer sb = new StringBuffer(1024);

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
		Connection connection = DriverManager.getConnection(url, user, pwd);
		return connection;
	}

	public static void main(final String[] args) {
		try {
			if (args == null || args.length == 0) {
				doPrintUsage();
			} else if (args.length == 1) {
				String s = args[0].trim();
				File f = new File(s);
				if (f.exists() && f.isDirectory()) {
					parseDirectory(f);
				} else if (f.exists() && f.isFile()) {
					parseFile(f);
				} else if (s.indexOf(':') > 0 && s.indexOf(':') < s.length()) {
					if (s.startsWith("server:")) {
						doListenOnPort(s.substring(s.indexOf(':') + 1));
					} else {
						parseUrl(s);
					}
				}
			} else if (args.length == 2) {
				if (args[0].trim().equals("server")) {
					doListenOnPort(args[1]);
				} else {
					parseUrl(args[0].trim() + ":" + args[1].trim());
				}
			} else if (args.length == 3) {
				Properties p = new Properties();
				databaseProperties.add(p);
				p.setProperty(URL, args[0].trim());
				p.setProperty(USER, args[1].trim());
				p.setProperty(PWD, args[2].trim());
			} else if (args.length == 4) {
				Properties p = new Properties();
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
				for (Properties p : databaseProperties) {
					doPrintSeparator();
					doCheckDatabase(p);
				}
			}
			if (!servers.isEmpty()) {
				doPrintSeparator();
				doPrintSeparator();
				System.out.println("Check servers");
				for (Entry<String, Integer> server : servers) {
					doPrintSeparator();
					doCheckSocketConnection(server);
				}
			}
		} catch (Exception e) {
			doPrintError(e.getMessage());
		}
	}

	/**
	 * @param string
	 */
	private static void doListenOnPort(final String string) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void parseDirectory(final File dir) throws Exception {
		if (dir.isDirectory()) {
			for (File f : dir.listFiles()) {
				if (f.isDirectory()) {
					parseDirectory(f);
				} else if (f.isFile()) {
					parseFile(f);
				}
			}
		}
	}

	private static void parseFile(final File f) throws Exception {
		if (f.exists()) {
			if (f.getName().endsWith(".properties")) {
				Properties properties = new Properties();
				properties.load(new FileInputStream(f));
				String key;
				String value;

				for (Entry<Object, Object> en : properties.entrySet()) {
					key = String.valueOf(en.getKey());
					value = String.valueOf(en.getValue());
					if (key.endsWith(".host")) {
						String hostDesc = key.substring(0, key.indexOf(".host"));
						for (Entry<Object, Object> second : properties.entrySet()) {
							String keyPort = String.valueOf(second.getKey());
							if (keyPort.startsWith(hostDesc) && keyPort.endsWith(".port")) {
								parseUrl(value + ":" + String.valueOf(second.getValue()));
								break;
							}
						}
					} else if (value.contains("http://") || value.contains("https://") || value.contains("ftp://")
							|| value.contains("sftp://") || value.contains("ldap://") || value.contains("ldaps://")) {
						parseUrl(value);
					}
				}

				Properties p = new Properties();
				boolean add = false;
				for (Entry<Object, Object> en : properties.entrySet()) {
					key = String.valueOf(en.getKey());
					value = String.valueOf(en.getValue());
					if (key.toLowerCase().contains("user")) {
						p.setProperty(USER, value);
					} else if (key.toLowerCase().contains("password") || key.toLowerCase().contains("pwd")) {
						p.setProperty(PWD, value);
					} else if (key.toLowerCase().contains("url") && value.toLowerCase().startsWith("jdbc")) {
						p.setProperty(URL, value);
						add = true;
					} else if (key.toLowerCase().contains("driver")) {
						p.setProperty(DRIVER, value);
					}
				}
				if (add) {
					databaseProperties.add(p);
				}

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
								Properties p = new Properties();
								databaseProperties.add(p);
								p.setProperty(URL, url);
								p.setProperty(USER, user);
								p.setProperty(PWD, pwd);
								p.setProperty(DRIVER, driver);
								p.setProperty(DATASOURCE, datasource);
							}
						}
						index = xml.indexOf("<Resource");
					}
				}
			}
		}
	}

	private static int parsePort(final String portString) {
		int port = 0;
		String s = portString;
		if (s != null) {
			if (s.indexOf('/') > 0) {
				s = s.substring(0, s.indexOf('/'));
			}
			try {
				port = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				doPrintError("Can not parse '" + s + "' to a port number!");
			}
		}
		return port;
	}

	private static void parseUrl(final String url) {
		String host = null;
		Integer port = null;
		if (url != null) {
			String s = url.trim();
			if (s.contains("://")) {
				s = s.substring(s.indexOf("://") + 3);
			}
			if (s.indexOf(':') > 0 && s.indexOf(':') < s.length()) {
				host = s.substring(0, s.indexOf(':'));
				s = s.substring(s.indexOf(':') + 1);
				port = parsePort(s);
			}
		}
		if (host != null && port != null) {
			boolean add = true;
			for (Entry<String, Integer> x : servers) {
				if (x.getKey().equals(host) && x.getValue().equals(port)) {
					add = false;
				}
			}
			if (add) {
				Entry<String, Integer> server = new ServerEntry(host, port);
				servers.add(server);
			}
		}
	}

	private static String readFile(final File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();

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
				}
			}
		}
		if (s == null || s.trim().length() == 0) {
			s = DRIVER_CLASS_ORACLE;
		}
		return s;
	}

	private static void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
}
