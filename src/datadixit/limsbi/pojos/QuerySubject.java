/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.pojos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import datadixit.limsbi.db.JDBCUtil;
import sapphire.util.Logger;

/**
 *
 * @author Iqbal
 */
@JsonIgnoreProperties({ "Parent" })
public class QuerySubject {

	public String QuerySubject;
	public String U_Name;
	public Link L;
	public QuerySubject Parent;
	public String Folder;
	public String FinalFolder;
	public String QueryItemPrefix;
	public List<QuerySubject> Children;
	public List<QuerySubject> LinkedFinales;

	public QuerySubject(String UU_Name) {
		QuerySubject = UU_Name.toUpperCase();
		U_Name = UU_Name;
		Parent = null;
		L = null;
		Children = new ArrayList<QuerySubject>();
		LinkedFinales = new ArrayList<QuerySubject>();
		Folder = "";
		FinalFolder = JDBCUtil.getFolderFromU_Name(UU_Name);
		QueryItemPrefix = "";

	}

	public void Make() {
		Integer MAX_RECURSION = 2;
		Integer RECURSION = 0;
		QuerySubject TMP = this;

		while (TMP.Parent != null) {
			if (TMP.U_Name.equals(this.U_Name)) {
				RECURSION++;
			}
			TMP = TMP.Parent;
		}

		/* NOUVEAU */
		// LinkSVC.makeF(this);
		// LinkSVC.makeRF(this);
		// LinkSVC.makeI(this);
		// LinkSVC.makeRI(this);
		/**
		 * ****************
		 */
		// Make ALL Types
		// Final to RefsDC
		MakeF();
		MakeRF();
		MakeFS();
		MakeRS();
		MakeM2M();
		MakeV();
		MakeD();
		MakeI();
		MakeRI();
		MakeS();

		// Final to Final
		MakeF_F();
		MakeF_FS();
		MakeF_DE();
		MakeF_C();

		// watch out for loops
		if (RECURSION < MAX_RECURSION) {

			for (QuerySubject C : this.Children) {
				C.Make();
			}
		} else {
			System.out.println("MAX_RECURSION HAS BEEN REACHED!");
		}

	}

	private void MakeF() {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(U_Name, this.Parent, "F");

		for (ArrayList<String> ROW : FKs) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			F f = new F("F");
			f.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));
			f.SDCCOLUMNID2 = ROW.get(Header.indexOf("u_sdccolumnid2"));
			f.SDCCOLUMNID3 = ROW.get(Header.indexOf("u_sdccolumnid3"));

			f.KeyID = JDBCUtil.PKs(Child.U_Name, 1);
			f.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			f.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);

			Child.L = f;
			Child.Folder = this.Folder + "." + f.SDCCOLUMNID.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + f.SDCCOLUMNID + "_" + Child.QuerySubject;

			// first child
			// if (this.Parent == null) {
			//
			// }
			this.Children.add(Child);

		}

	}

	private void MakeRF() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(U_Name, this.Parent, "RF");

		for (ArrayList<String> ROW : FKs) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			RF rf = new RF("RF");

			// use field names ou non
			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {

				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				rf.CommunColumns = Commun;
				rf.isUseFieldName = true;

				Child.Folder = this.Folder + "." + rf.CommunColumns.get(0).toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = this.QuerySubject + "_" + rf.CommunColumns.get(0) + "_" + Child.QuerySubject;

			} else {
				rf.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));
				rf.SDCCOLUMNID2 = ROW.get(Header.indexOf("u_sdccolumnid2"));
				rf.SDCCOLUMNID3 = ROW.get(Header.indexOf("u_sdccolumnid3"));

				rf.KeyID = JDBCUtil.PKs(this.U_Name, 1);
				rf.KeyID2 = JDBCUtil.PKs(this.U_Name, 2);
				rf.KeyID3 = JDBCUtil.PKs(this.U_Name, 3);

				Child.Folder = this.Folder + "." + rf.SDCCOLUMNID.toLowerCase();
				Child.QueryItemPrefix = Child.Folder + ".";
				Child.QuerySubject = this.QuerySubject + "_" + rf.SDCCOLUMNID + "_" + Child.QuerySubject;

			}
			Child.L = rf;

			this.Children.add(Child);
		}

	}

	private void MakeFS() {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(U_Name, this.Parent, "FS");

		for (ArrayList<String> ROW : FKs) {
			// JOptionPane.showMessageDialog(null, "thank you for using java");

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			FS fs = new FS("FS");
			// U_FOREIGNSDIUSEDKEYS
			fs.Prefix = ROW.get(Header.indexOf("u_foreignsdiusedkeys"));
			fs.Prefix = fs.Prefix.replace("sdcid", "");
			fs.Prefix = fs.Prefix.replace("keyid1", "");
			fs.Prefix = fs.Prefix.replace("keyid2", "");
			fs.Prefix = fs.Prefix.replace("keyid3", "");

			fs.SDCID = ROW.get(Header.indexOf("u_linksdcid"));
			fs.keyID1 = JDBCUtil.PKs(Child.U_Name, 1);
			fs.keyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			fs.keyID3 = JDBCUtil.PKs(Child.U_Name, 3);

			Child.L = fs;
			Child.Folder = this.Folder + "." + fs.Prefix.concat("sdcid").toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + fs.Prefix.concat("sdcid") + "_" + Child.QuerySubject;

			// first child
			// if (this.Parent == null) {
			//
			// }
			this.Children.add(Child);

		}

	}

	private void MakeRS() {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getLink(U_Name, this.Parent, "RS");

		for (ArrayList<String> ROW : FKs) {

			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			RS rs = new RS("RS");
			// U_FOREIGNSDIUSEDKEYS
			rs.Prefix = ROW.get(Header.indexOf("u_foreignsdiusedkeys"));
			rs.Prefix = rs.Prefix.replace("sdcid", "");
			rs.Prefix = rs.Prefix.replace("keyid1", "");
			rs.Prefix = rs.Prefix.replace("keyid2", "");
			rs.Prefix = rs.Prefix.replace("keyid3", "");

			rs.SDCID = ROW.get(Header.indexOf("u_sdcid"));
			rs.keyID1 = JDBCUtil.PKs(this.U_Name, 1);
			rs.keyID2 = JDBCUtil.PKs(this.U_Name, 2);
			rs.keyID3 = JDBCUtil.PKs(this.U_Name, 3);

			Child.L = rs;
			Child.Folder = this.Folder + "." + rs.Prefix.concat("sdcid").toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + rs.Prefix.concat("sdcid") + "_" + Child.QuerySubject;

			// first child
			// if (this.Parent == null) {
			//
			// }
			this.Children.add(Child);

		}

	}

	private void MakeV() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> Vs = JDBCUtil.getLink(U_Name, this.Parent, "V");

		for (ArrayList<String> ROW : Vs) {
			QuerySubject Child = new QuerySubject("refvalue");
			Child.Parent = this;
			V v = new V("V");
			v.RefType = ROW.get(Header.indexOf("u_reftypeid"));
			v.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));

			Child.L = v;
			// Child.Folder = this.Folder + "." + f.SDCCOLUMNID.toLowerCase();
			// Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + v.SDCCOLUMNID + "_" + v.RefType;
			Child.Folder = this.Folder + "." + v.SDCCOLUMNID.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			this.Children.add(Child);

		}
	}

	private void MakeM2M() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> M2Ms = JDBCUtil.getLink(U_Name, this.Parent, "M");

		for (ArrayList<String> ROW : M2Ms) {

			QuerySubject Child = new QuerySubject(ROW.get(Header.indexOf("u_linktableid")));
			QuerySubject ChildOfChild = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));

			// preparer le lien M2M
			// EDIT: logique change
			// ici M1 ne crée plus un dossier portant le nom de la table de
			// jointure
			// il crée le dossier de son enfant
			// voir
			// Child.Folder = this.Folder + "." +
			// ChildOfChild.U_Name.toLowerCase();;
			M m1 = new M("M1");

			m1.KeyID1 = JDBCUtil.PKs(this.U_Name, 1);
			m1.KeyID2 = JDBCUtil.PKs(this.U_Name, 2);
			m1.KeyID3 = JDBCUtil.PKs(this.U_Name, 3);

			Child.Parent = this;
			Child.L = m1;
			Child.QuerySubject = this.QuerySubject + "_" + Child.U_Name.toUpperCase();
			// Child.Folder = this.Folder + "." + Child.U_Name.toLowerCase();;
			Child.Folder = this.Folder + "." + Child.U_Name.toLowerCase();
			;
			Child.QueryItemPrefix = Child.Folder + ".";

			// it's just a ghost node :/ gotta have the M2M table somewhere
			// & logic changed
			// Child.QuerySubject = this.QuerySubject;
			// Child.Folder = this.Folder;
			// Child.QueryItemPrefix = Child.Folder;
			this.Children.add(Child);

			M m2 = new M("M2");

			m2.KeyID1 = JDBCUtil.PKs(ChildOfChild.U_Name, 1);
			m2.KeyID2 = JDBCUtil.PKs(ChildOfChild.U_Name, 2);
			m2.KeyID3 = JDBCUtil.PKs(ChildOfChild.U_Name, 3);

			ChildOfChild.Parent = Child;
			ChildOfChild.L = m2;
			ChildOfChild.QuerySubject = Child.QuerySubject + "_" + ChildOfChild.U_Name.toUpperCase();
			ChildOfChild.Folder = Child.Folder + "." + ChildOfChild.U_Name.toLowerCase();
			;
			ChildOfChild.QueryItemPrefix = ChildOfChild.Folder + ".";
			Child.Children.add(ChildOfChild);

		}
	}

	private void MakeS() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> Ss = JDBCUtil.getLink(U_Name, this.Parent, "S");

		for (ArrayList<String> ROW : Ss) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			S s = new S("S");

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			s.TableID = ROW.get(Header.indexOf("u_tableid"));

			// parent keyids to do <Parent PK1> = "KEYID1"
			s.KeyID1 = JDBCUtil.PKs(this.U_Name, 1);
			s.KeyID2 = JDBCUtil.PKs(this.U_Name, 2);
			s.KeyID3 = JDBCUtil.PKs(this.U_Name, 3);

			// void si le parent est une sdi
			String ParentPrefix = s.TableID.substring(0, 3).toLowerCase();
			Boolean isSDI = ParentPrefix.equals("sdi");
			// System.out.println("TABLEID = " + s.TableID);
			if (isSDI) {
				// sdi-sdi
				// get parent & child columns
				// do intersectino to keep commun columns

				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				s.CommunColumns = Commun;
				// System.out.println(" PK PARENT = " + ParentColumns);

			} else {
				// if not an sdi, then you must specify which sdc
				s.SdcID = ROW.get(Header.indexOf("u_sdcid"));
			}
			Child.L = s;
			Child.Folder = this.Folder + "." + Child.U_Name;
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + Child.QuerySubject;

			this.Children.add(Child);

		}

	}

	private void MakeD() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> Ds = JDBCUtil.getLink(U_Name, this.Parent, "D");
		for (ArrayList<String> ROW : Ds) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			D d = new D("D");

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			// s.TableID = ROW.get(Header.indexOf("u_tableid"));
			// parent keyids to do <Parent PK1> = "KEYID1"
			d.KeyID1 = JDBCUtil.PKs(this.U_Name, 1);
			d.KeyID2 = JDBCUtil.PKs(this.U_Name, 2);
			d.KeyID3 = JDBCUtil.PKs(this.U_Name, 3);

			Child.L = d;
			Child.Folder = this.Folder + "." + Child.U_Name;
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + Child.QuerySubject;

			this.Children.add(Child);

		}

	}

	private void MakeI() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> Is = JDBCUtil.getLink(U_Name, this.Parent, "I");

		for (ArrayList<String> ROW : Is) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			I ii = new I("I");

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			ii.TableID = ROW.get(Header.indexOf("u_linktableid"));
			ii.SdcID = ROW.get(Header.indexOf("u_linksdcid"));
			// parent keyids to do <Parent PK1> = "KEYID1"
			ii.KeyID1 = JDBCUtil.PKs(Child.U_Name, 1);
			ii.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			ii.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);
			// ii.isF2F = true;

			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {
				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				ii.CommunColumns = Commun;
				ii.isUseFieldName = true;

			}
			Child.L = ii;
			// Child.Folder = this.Folder + "." + ii.SdcID.toLowerCase();
			Child.Folder = this.Folder + "." + Child.U_Name.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + Child.QuerySubject;

			this.Children.add(Child);

		}

	}

	private void MakeRI() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> RIs = JDBCUtil.getLink(U_Name, this.Parent, "RI");

		for (ArrayList<String> ROW : RIs) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), this));
			Child.Parent = this;

			RI ri = new RI("RI");

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			ri.TableID = ROW.get(Header.indexOf("u_tableid"));
			ri.SdcID = ROW.get(Header.indexOf("u_sdcid"));
			// parent keyids to do <Parent PK1> = "KEYID1"
			ri.KeyID1 = JDBCUtil.PKs(this.U_Name, 1);
			ri.KeyID2 = JDBCUtil.PKs(this.U_Name, 2);
			ri.KeyID3 = JDBCUtil.PKs(this.U_Name, 3);
			// ri.isF2F = true;

			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {
				ri.isUseFieldName = true;
				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				ri.CommunColumns = Commun;
				ri.isUseFieldName = true;

			}
			Child.L = ri;
			// Child.Folder = "";
			// Child.QueryItemPrefix = "";

			Child.Folder = this.Folder + "." + Child.U_Name.toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			Child.QuerySubject = this.QuerySubject + "_" + Child.QuerySubject;
			// Child.QuerySubject = this.QuerySubject + "_" +
			// Child.QuerySubject;
			this.Children.add(Child);

		}

	}

	public void PrettyPrint() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));

		} catch (IOException ex) {
			lg(ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void MakeF_F() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getFinalLinks(U_Name, this.Parent, "F");

		for (ArrayList<String> ROW : FKs) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), null));
			// Child.Parent = this;

			F f = new F("F");
			f.isF2F = true;
			f.LinkedFinale = this;
			// use field names ou non
			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {

				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				f.CommunColumns = Commun;
				f.isUseFieldName = true;
			} else {
				f.SDCCOLUMNID = ROW.get(Header.indexOf("u_sdccolumnid"));
				f.SDCCOLUMNID2 = ROW.get(Header.indexOf("u_sdccolumnid2"));
				f.SDCCOLUMNID3 = ROW.get(Header.indexOf("u_sdccolumnid3"));

				f.KeyID = JDBCUtil.PKs(Child.U_Name, 1);
				f.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
				f.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);
			}
			Child.L = f;
			// Child.Folder = this.Folder + "." + f.SDCCOLUMNID.toLowerCase();
			// Child.QueryItemPrefix = Child.Folder + ".";
			// Child.QuerySubject = this.QuerySubject + "_" + f.SDCCOLUMNID +
			// "_" + Child.QuerySubject;

			// first child
			// if (this.Parent == null) {
			//
			// }
			this.LinkedFinales.add(Child);
		}

	}

	private void MakeF_FS() {
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getFinalLinks(U_Name, this.Parent, "FS");

		for (ArrayList<String> ROW : FKs) {
			// JOptionPane.showMessageDialog(null, "thank you for using java");

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), null));
			// Child.Parent = this;

			FS fs = new FS("FS");
			fs.LinkedFinale = this;
			fs.isF2F = true;
			fs.Prefix = ROW.get(Header.indexOf("u_foreignsdiusedkeys"));
			fs.Prefix = fs.Prefix.replace("sdcid", "");
			fs.Prefix = fs.Prefix.replace("keyid1", "");
			fs.Prefix = fs.Prefix.replace("keyid2", "");
			fs.Prefix = fs.Prefix.replace("keyid3", "");

			fs.SDCID = ROW.get(Header.indexOf("u_linksdcid"));
			fs.keyID1 = JDBCUtil.PKs(Child.U_Name, 1);
			fs.keyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			fs.keyID3 = JDBCUtil.PKs(Child.U_Name, 3);

			Child.L = fs;
			Child.Folder = this.Folder + "." + fs.Prefix.concat("sdcid").toLowerCase();
			Child.QueryItemPrefix = Child.Folder + ".";
			// Child.QuerySubject = this.QuerySubject + "_" +
			// fs.Prefix.concat("sdcid") + "_" + Child.QuerySubject;

			// first child
			// if (this.Parent == null) {
			//
			// }
			this.LinkedFinales.add(Child);

		}

	}

	private void MakeF_DE() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> DEs = JDBCUtil.getFinalLinks(U_Name, this.Parent, "DE");

		for (ArrayList<String> ROW : DEs) {

			// get real u_name
			// System.out.println();
			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), null));
			// Child.Parent = this;

			DE de = new DE("DE");

			// le table id du parrent!!! pour savoir si c une sdc-sdi ou sdi-sdi
			de.LinkedFinale = this;
			de.TableID = ROW.get(Header.indexOf("u_linktableid"));
			de.SdcID = ROW.get(Header.indexOf("u_linksdcid"));
			// parent keyids to do <Parent PK1> = "KEYID1"
			de.KeyID1 = JDBCUtil.PKs(Child.U_Name, 1);
			de.KeyID2 = JDBCUtil.PKs(Child.U_Name, 2);
			de.KeyID3 = JDBCUtil.PKs(Child.U_Name, 3);
			de.isF2F = true;

			if (ROW.get(Header.indexOf("u_usefieldsname")).equals("Y")) {
				de.isUseFieldName = true;
				ArrayList<String> ParentColumns = JDBCUtil.PKColumns(this.U_Name);
				ArrayList<String> ChildColumns = JDBCUtil.PKColumns(Child.U_Name);

				ArrayList<String> Commun = new ArrayList<String>(ParentColumns);
				Commun.retainAll(ChildColumns);
				de.CommunColumns = Commun;
				de.isUseFieldName = true;

			}
			Child.L = de;
			Child.Folder = "";
			Child.QueryItemPrefix = "";
			// Child.QuerySubject = this.QuerySubject + "_" +
			// Child.QuerySubject;

			this.LinkedFinales.add(Child);

		}

	}

	private void MakeF_C() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbisdclink");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getFinalLinks(U_Name, this.Parent, "C");

		for (ArrayList<String> ROW : FKs) {

			QuerySubject Child = new QuerySubject(JDBCUtil.getUName(ROW.get(Header.indexOf("u_linksdcid")),
					ROW.get(Header.indexOf("u_linktablename")), null));

			C cc = new C("C");

			cc.isF2F = true;
			cc.LinkedFinale = this;
			cc.Relation = ROW.get(Header.indexOf("u_customrelation"));

			Child.L = cc;

			this.LinkedFinales.add(Child);
		}

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
