/**
 * Licensed Materials - Property of IBM
 *
 * IBM Cognos Products: CAMAAA
 *
 * (C) Copyright IBM Corp. 2005, 2010
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with
 * IBM Corp.
 */
package datadixit.limsbi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import datadixit.limsbi.properties.JDBCProperties;
import sapphire.util.Logger;

/**
 *
 * @author ndescamps
 *
 */
public class JDBCDriver {
	/**
	 * ORA_JDBCDriver gives to provider possibility to connect Oracle Database,
	 * it call Oracle odbc14.jar
	 *
	 */
	static {
		try {

			Class.forName(JDBCProperties.forName);

		} catch (Exception e) {
			lg(" JDBCDriver " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(JDBCProperties.conString, JDBCProperties.user, JDBCProperties.password);
	}

	public static Vector<Vector<String>> query(String SQL_QUERY) throws SQLException {
		Vector<Vector<String>> retval = new Vector<Vector<String>>();
		Connection con = JDBCDriver.getConnection();
		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(SQL_QUERY);
		ResultSetMetaData rsmd = result.getMetaData();

		// First row contains the metadata
		Vector<String> metadata = new Vector<String>();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			metadata.add(rsmd.getColumnName(i));
		}
		retval.add(metadata);
		while (result.next()) {
			Vector<String> row = new Vector<String>();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				row.add(result.getString(i));
			}
			retval.add(row);
		}

		stmt.close();
		con.close();

		return retval;
	}

	public static synchronized int execute(String SQL_QUERY) throws SQLException {
		new Vector<Object>();
		Connection con = JDBCDriver.getConnection();
		Statement stmt = con.createStatement();
		int res = stmt.executeUpdate(SQL_QUERY);
		stmt.close();
		con.close();
		return res;
	}

	// public static void dumpData(Vector theData, PrintStream thePrintStream) {
	// for (int i = 0; i < theData.size(); i++) {
	// Vector row = (Vector) theData.elementAt(i);
	// for (int j = 0; j < row.size(); j++) {
	// thePrintStream.print("\t" + row.elementAt(j));
	// }
	// thePrintStream.println();
	// }
	// }
	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
