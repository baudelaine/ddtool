package datadixit.limsbi.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import datadixit.limsbi.pojos.QuerySubject;
import sapphire.util.Logger;

public class JDBCUtil {

	static {

	}

	private static Integer getSingleInteger(Vector V) {
		String N = getSingleString(V);
		Integer M = 0;
		try {
			M = Integer.parseInt(N);
		} catch (NumberFormatException e) {
			M = 0;
		}
		return M;

	}

	private static String getSingleString(Vector V) {
		if (V == null) {
			return "";
		}
		if (V.size() <= 1) {
			return "";
		}
		Vector v = (Vector) V.get(1);
		String res = (String) v.get(0);
		res = res == null ? "" : res;
		return res;

	}

	public static ArrayList getTableHeader(String table) {

		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = "select lower(column_name) from all_tab_columns where upper(table_name) = upper('" + table
					+ "')  order by column_id ";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	private static ArrayList getVector(Vector V) {
		ArrayList<ArrayList<String>> L = new ArrayList<ArrayList<String>>();

		if (V == null) {
			return L;
		}
		if (V.size() <= 1) {
			return L;
		}
		V.remove(0);
		for (Object o : V) {
			Vector v = (Vector) o;
			ArrayList<String> row = new ArrayList<String>();
			for (Object oo : v) {
				String res = (String) oo;
				res = res == null ? "" : res;
				row.add(res);

			}
			L.add(row);
		}
		return L;

	}

	private static ArrayList getSingleArrayList(Vector V) {

		ArrayList<String> L = new ArrayList<String>();
		if (V == null) {
			return L;
		}
		if (V.size() <= 1) {
			return L;
		}

		V.remove(0);
		for (Object o : V) {
			Vector v = (Vector) o;

			String now = (String) v.get(0);
			if (now == null) {
				now = "";
			}
			L.add(now);
		}
		return L;
	}

	public static String getName(String u_name) {
		Vector V = new Vector();
		try {

			String STR = "select distinct u_tableid from u_limsbifields where u_name = '" + u_name + "'";

			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);
	}

	public static String getUName(String u_tableid) {
		Vector V = new Vector();
		try {

			String col = "";
			String STR = "select distinct u_name from u_limsbifields where u_tableid = '" + u_tableid + "' ";
			V = JDBCDriver.query(STR);
		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);

	}

	public static String getUName(String u_sdcid, String u_name, QuerySubject Parent) {
		Vector V = new Vector();
		try {
			String STR;
			if (!u_name.equals("")) {
				if (Parent == null) {
					STR = "select distinct u_name from u_limsbifields where u_tabletype = 'Final' and (u_name = '"
							+ u_name + "' )";
				} else {
					STR = "select distinct u_name from u_limsbifields where u_tabletype = 'RefSDC' and (u_name = '"
							+ u_name + "' )";
				}

			} else {
				if (Parent == null) {
					STR = "select distinct u_name from u_limsbifields where u_tabletype = 'Final' and    u_tableid = (select distinct tableid from sdc where sdcid = '"
							+ u_sdcid + "')";
				} else {
					STR = "select distinct u_name from u_limsbifields where u_tabletype = 'RefSDC' and u_tableid = (select distinct tableid from sdc where sdcid = '"
							+ u_sdcid + "')";
				}
			}

			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);
	}

	public static String getNameFromSDC(String u_sdcid) {
		Vector V = new Vector();
		try {
			String col = "";
			String STR = "select distinct tableid from sdc where sdcid = '" + u_sdcid + "'";

			V = JDBCDriver.query(STR);
		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);

	}

	public static ArrayList<String> SECURITY() {
		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = " select distinct U_Tableid from U_Limsbifields ";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}

		return getSingleArrayList(V);

	}

	public static ArrayList<String> FINALES() {
		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = "select distinct u_name from u_limsbifields where u_tabletype = 'Final' ";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static ArrayList<String> REFSDC() {
		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = "select distinct u_name from u_limsbifields where u_tabletype = 'RefSDC' ";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static ArrayList<String> M2M() {
		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = "select distinct u_linktableid from u_limsbisdclink where u_linktype = 'M' and u_refsdcselectedlink = 'Y'";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static Integer MAX() {
		Vector V = new Vector<Object>();
		try {

			String STR = "select max(C) as MAX from "
					+ "(select count(*) as C  from u_limsbifields where u_traduction = 'Y' and u_name in "
					+ "( select distinct u_name from u_limsbifields where u_name is not null) group by u_name)";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleInteger(V);
	}

	public static String ReorderBefore(String u_name, String u_columnid) {
		Vector V = new Vector();
		try {

			String col = "";
			String STR = "select distinct u_columnid  from u_limsbifields where u_columnsequence = (select distinct u_columnsequence from u_limsbifields where u_columnid = '"
					+ u_columnid + "' and u_name = '" + u_name + "')+1 and u_name = '" + u_name + "'";

			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);

	}

	public static ArrayList<String> Columns(String u_name) {
		Vector V = new Vector<Object>();

		try {
			String STR = "select u_columnid from (select distinct u_columnsequence,u_columnid from u_limsbifields where u_name = '"
					+ u_name + "' order by u_columnsequence)";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static ArrayList<String> PKColumns(String u_name) {
		Vector V = new Vector<Object>();

		try {
			String STR = "select distinct u_columnid from u_limsbifields where u_pkflag = 'Y' and u_name = '" + u_name
					+ "'";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static String PKs(String u_name, Integer N) {
		Vector V = new Vector<Object>();
		try {

			ArrayList L;
			String STR = "";
			STR = "select distinct u_columnid from u_limsbifields where u_pkflag = 'Y' and u_name = '" + u_name
					+ "' and u_columnsequence = " + N + "  order by u_columnsequence";

			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);
	}

	public static ArrayList getLink(String u_name, QuerySubject Parent, String LinkType) {
		Vector V = new Vector<Object>();
		try {

			ArrayList L;
			String STR = "";
			if (Parent == null) {
				STR = "select * from u_limsbisdclink where u_name = '" + u_name
						+ "' and u_tabletype = 'Final' and u_refsdcselectedlink = 'Y' and u_linktype = '" + LinkType
						+ "'";
			} else {
				STR = "select * from u_limsbisdclink where u_name = '" + u_name
						+ "' and u_tabletype = 'RefSDC' and u_refsdcselectedlink = 'Y' and u_linktype = '" + LinkType
						+ "'";
			}

			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getVector(V);
	}

	public static ArrayList getFinalLinks(String u_name, QuerySubject Parent, String LinkType) {
		Vector V = new Vector<Object>();
		try {

			ArrayList L;
			String STR = "";
			if (Parent == null) {
				STR = "select * from u_limsbisdclink where u_name = '" + u_name
						+ "' and u_tabletype = 'Final' and u_finalselectedlink = 'Y' and u_linktype = '" + LinkType
						+ "'";
				V = JDBCDriver.query(STR);
			}

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getVector(V);
	}

	// public static Vector FKs(String BasedOn, String TableType) throws
	// SQLException {
	//
	// Vector V = new Vector<>();
	// String STR
	// = "select * from u_limsbisdclink where u_tableid = '" + BasedOn + "' and
	// u_tabletype = '" + TableType + "' and u_refsdcselectedlink = 'Y' ";
	// V = JDBCDriver.query(STR);
	//
	// return V;
	//
	// }
	public static ArrayList<String> getTranslantedColumns(String u_name, String u_tabletype) {
		Vector V = new Vector<Object>();
		try {
			String STR = "select u_columnid from u_limsbifields where u_name = '" + u_name + "' and u_tabletype = '"
					+ u_tabletype + "' and u_traduction = 'Y' order by u_columnsequence";
			V = JDBCDriver.query(STR);
		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static ArrayList<String> FOLDERS() {
		Vector V = new Vector<Object>();
		try {
			ArrayList<String> L = new ArrayList<String>();
			String STR = "select U_LIMSBIFOLDERSID from U_LIMSBIFOLDERS where U_PARENTFOLDER is null";
			V = JDBCDriver.query(STR);

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleArrayList(V);

	}

	public static ArrayList<ArrayList<String>> getFolders(String name) {
		Vector V = new Vector<Object>();
		try {

			ArrayList L;
			String STR = "";
			if (!name.equals("")) {
				STR = "select * from U_LIMSBIFOLDERS where U_PARENTFOLDER = '" + name + "'  ";
				V = JDBCDriver.query(STR);
			}
			// STR = "select * from u_limsbisdclink where u_name = '" + u_name +
			// "' and u_tabletype = 'RefSDC' and u_refsdcselectedlink = 'Y' and
			// u_linktype = '" + LinkType + "'";

		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getVector(V);
	}

	public static String getFolderFromU_Name(String u_name) {
		Vector V = new Vector();
		try {
			String STR = "select distinct u_folder from u_limsbisdclink where u_name = '" + u_name
					+ "' and u_folder is not null";

			V = JDBCDriver.query(STR);
		} catch (SQLException ex) {
			lg(ex.getMessage());
		}
		return getSingleString(V);

	}

	public static boolean isSecurityON(String table) {

		Vector V = new Vector();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT DISTINCT ");
			sb.append("  Sdc.Accesscontrolledflag ");
			sb.append("FROM U_Limsbifields ");
			sb.append("LEFT JOIN sdc ");
			sb.append("ON u_tableid  = tableid ");
			sb.append("WHERE tableid = '");
			sb.append(table);
			sb.append("' ");
			String STR = sb.toString();

			V = JDBCDriver.query(STR);
		} catch (SQLException ex) {
			lg(ex.getMessage());
		}

		return getSingleString(V).equals("D");

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
