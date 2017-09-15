/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.svc;

import java.util.ArrayList;

import datadixit.limsbi.db.JDBCUtil;
import datadixit.limsbi.pojos.F;
import datadixit.limsbi.pojos.I;
import datadixit.limsbi.pojos.QuerySubject;
import datadixit.limsbi.pojos.RF;
import datadixit.limsbi.pojos.RI;
import datadixit.limsbi.pojos.V;

/**
 *
 * @author splims
 */
public class LinkSVC {

	public static String srcNameSpace;
	public static String dstNameSpace;
	public static String prntNameSpace;
	public static QuerySubject CurrentNode;

	static {

	}

	// make in memory
	public static void makeF(QuerySubject THIS) {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(THIS.U_Name, THIS.Parent, "F");
		ArrayList<ArrayList<String>> FKKs = JDBCUtil.getFinalLinks(THIS.U_Name, THIS.Parent, "F");
		ArrayList<ArrayList<String>> AllFK = FKs;
		AllFK.addAll(FKKs);

		for (ArrayList<String> ROW : AllFK) {

			/* F2F or F2R */
			F f = new F("F");
			QuerySubject Child = null;
			boolean isF2F = false;
			if (ROW.get(Header.indexOf("u_finalselectedlink")).equals("Y")) {
				isF2F = true;
			}

			if (isF2F) {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), null));
				f.LinkedFinale = THIS;
				f.isF2F = true;

			} else {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), THIS));
				Child.Parent = THIS;
			}

			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {

				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(THIS.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				f.CommunColumns = Commun;
				f.isUseFieldName = true;

				Child.Folder = THIS.Folder + "." + f.CommunColumns.get(0).toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = THIS.QuerySubject + "_" + f.CommunColumns.get(0) + "_" + Child.QuerySubject;

			} else {
				f.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));
				f.SDCCOLUMNID2 = ROW.get(Header.indexOf("u_sdccolumnid2"));
				f.SDCCOLUMNID3 = ROW.get(Header.indexOf("u_sdccolumnid3"));

				f.KeyID = JDBCUtil.PKs(Child.U_Name, 1);
				f.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
				f.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);

				Child.Folder = THIS.Folder + "." + f.SDCCOLUMNID.toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = THIS.QuerySubject + "_" + f.SDCCOLUMNID + "_" + Child.QuerySubject;

			}
			Child.L = f;
			if (isF2F) {
				THIS.LinkedFinales.add(Child);

			} else {
				THIS.Children.add(Child);

			}

		}

	}

	public static void makeRF(QuerySubject THIS) {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(THIS.U_Name, THIS.Parent, "RF");
		ArrayList<ArrayList<String>> FKKs = JDBCUtil.getFinalLinks(THIS.U_Name, THIS.Parent, "RF");
		ArrayList<ArrayList<String>> All = FKs;
		All.addAll(FKKs);

		for (ArrayList<String> ROW : All) {

			/* F2F or F2R */
			RF rf = new RF("RF");
			QuerySubject Child = null;
			boolean isF2F = false;
			if (ROW.get(Header.indexOf("u_finalselectedlink")).equals("Y")) {
				isF2F = true;
			}

			if (isF2F) {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), null));
				rf.LinkedFinale = THIS;
				rf.isF2F = true;

			} else {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), THIS));
				Child.Parent = THIS;
			}
			/* link logique here */
			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {

				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(THIS.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				rf.CommunColumns = Commun;
				rf.isUseFieldName = true;

				Child.Folder = THIS.Folder + "." + rf.CommunColumns.get(0).toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = THIS.QuerySubject + "_" + rf.CommunColumns.get(0) + "_" + Child.QuerySubject;

			} else {
				rf.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));
				rf.SDCCOLUMNID2 = ROW.get(Header.indexOf("u_sdccolumnid2"));
				rf.SDCCOLUMNID3 = ROW.get(Header.indexOf("u_sdccolumnid3"));

				rf.KeyID = JDBCUtil.PKs(THIS.U_Name, 1);
				rf.KeyID2 = JDBCUtil.PKs(THIS.U_Name, 2);
				rf.KeyID3 = JDBCUtil.PKs(THIS.U_Name, 3);

				Child.Folder = THIS.Folder + "." + rf.SDCCOLUMNID.toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = THIS.QuerySubject + "_" + rf.SDCCOLUMNID + "_" + Child.QuerySubject;

			}

			Child.L = rf;
			if (isF2F) {
				THIS.LinkedFinales.add(Child);

			} else {
				THIS.Children.add(Child);

			}

		}

	}

	public static void makeI(QuerySubject THIS) {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(THIS.U_Name, THIS.Parent, "I");
		ArrayList<ArrayList<String>> FKKs = JDBCUtil.getFinalLinks(THIS.U_Name, THIS.Parent, "I");
		ArrayList<ArrayList<String>> All = FKs;
		All.addAll(FKKs);

		for (ArrayList<String> ROW : All) {

			/* F2F or F2R */
			I ii = new I("I");
			QuerySubject Child = null;
			boolean isF2F = false;
			if (ROW.get(Header.indexOf("u_finalselectedlink")).equals("Y")) {
				isF2F = true;
			}

			if (isF2F) {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), null));
				ii.LinkedFinale = THIS;
				ii.isF2F = true;
				System.err.println("Warning: You are not supposed to do 'I' links & F2F");

			} else {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), THIS));
				Child.Parent = THIS;
			}
			/* link logique here */

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			ii.TableID = ROW.get(Header.indexOf("u_linktableid"));
			ii.SdcID = ROW.get(Header.indexOf("u_linksdcid"));
			// parent keyids to do <Parent PK1> = "KEYID1"
			ii.KeyID1 = JDBCUtil.PKs(Child.U_Name, 1);
			ii.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			ii.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);
			// ii.isF2F = true;

			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {
				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(THIS.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				ii.CommunColumns = Commun;
				ii.isUseFieldName = true;

			}
			Child.L = ii;
			// Child.Folder = this.Folder + "." + ii.SdcID.toLowerCase();
			Child.Folder = THIS.Folder + "." + Child.U_Name.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = THIS.QuerySubject + "_" + Child.QuerySubject;
			if (isF2F) {
				THIS.LinkedFinales.add(Child);
				// System.err.println("Warning: You are not supposed to do 'I'
				// links & F2F");

			} else {
				THIS.Children.add(Child);

			}

		}

	}

	public static void makeRI(QuerySubject THIS) {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(THIS.U_Name, THIS.Parent, "RI");
		ArrayList<ArrayList<String>> FKKs = JDBCUtil.getFinalLinks(THIS.U_Name, THIS.Parent, "XX");
		ArrayList<ArrayList<String>> All = FKs;
		All.addAll(FKKs);

		for (ArrayList<String> ROW : All) {

			/* F2F or F2R */
			RI ri = new RI("RI");
			QuerySubject Child = null;
			boolean isF2F = false;
			if (ROW.get(Header.indexOf("u_finalselectedlink")).equals("Y")) {
				isF2F = true;
			}

			if (isF2F) {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), null));
				ri.LinkedFinale = THIS;
				ri.isF2F = true;

			} else {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), THIS));
				Child.Parent = THIS;
			}
			/* link logique here */

			Child.L = ri;
			// Child.Folder = this.Folder + "." + ii.SdcID.toLowerCase();
			Child.Folder = THIS.Folder + "." + Child.U_Name.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = THIS.QuerySubject + "_" + Child.QuerySubject;
			if (isF2F) {
				THIS.LinkedFinales.add(Child);

			} else {
				THIS.Children.add(Child);

			}

		}

	}

	public static void makeV(QuerySubject THIS) {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(THIS.U_Name, THIS.Parent, "V");
		ArrayList<ArrayList<String>> FKKs = JDBCUtil.getFinalLinks(THIS.U_Name, THIS.Parent, "V");
		ArrayList<ArrayList<String>> All = FKs;
		All.addAll(FKKs);

		for (ArrayList<String> ROW : All) {

			/* F2F or F2R */
			V v = new V("V");
			QuerySubject Child = null;
			boolean isF2F = false;
			if (ROW.get(Header.indexOf("u_finalselectedlink")).equals("Y")) {
				isF2F = true;
			}

			if (isF2F) {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), null));
				v.LinkedFinale = THIS;
				v.isF2F = true;
				System.err.println("");
			} else {
				Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
						ROW.get(Header.indexOf("u_linktablename")), THIS));
				Child.Parent = THIS;
			}
			/* link logique here */

			Child.L = v;
			// Child.Folder = this.Folder + "." + ii.SdcID.toLowerCase();
			Child.Folder = THIS.Folder + "." + Child.U_Name.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = THIS.QuerySubject + "_" + Child.QuerySubject;
			if (isF2F) {
				THIS.LinkedFinales.add(Child);

			} else {
				THIS.Children.add(Child);

			}

		}

	}
	// make in model
	// create subfolders
}
