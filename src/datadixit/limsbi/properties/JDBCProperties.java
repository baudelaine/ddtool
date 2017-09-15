/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import sapphire.util.Logger;

/**
 *
 * @author Iqbal
 */
public class JDBCProperties {
	public static String server;
	public static String baseType;
	public static String databaseName;
	public static String user;
	public static String password;
	public static String portNumber;
	public static String instanceName;

	public static String forName;
	public static String conString;

	static {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("/opt/wks/eclipse-4.1.2/ddtool/res/jdbc.properties"));
			server = prop.getProperty("server");
			System.out.println("++++++++++++++++++++++server " + server);
			baseType = prop.getProperty("baseType");
			databaseName = prop.getProperty("databaseName");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			portNumber = prop.getProperty("portNumber");
			instanceName = prop.getProperty("instanceName");

			forName = null;
			conString = null;

			// Build conString & forName
			if (baseType.equals("Oracle")) {
				conString = "jdbc:oracle:thin:@" + server + ":" + databaseName;
				forName = "oracle.jdbc.driver.OracleDriver";
			} else if (baseType.equals("SQLServer")) {
				conString = "jdbc:JSQLConnect://" + server;
				conString += "/database=" + databaseName;
				conString += "/portNumber=" + portNumber;
				conString += "/instanceName=" + instanceName;
				forName = "com.jnetdirect.jsql.JSQLDriver";
			}

		} catch (FileNotFoundException ex) {
			lg(ex.getMessage());
			ex.printStackTrace();
		} catch (IOException ex) {
			lg(ex.getMessage());
			ex.printStackTrace();
		}

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
