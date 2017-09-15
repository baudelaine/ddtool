package datadixit.limsbi.svc;

import java.util.ArrayList;
import java.util.List;

import datadixit.limsbi.db.JDBCUtil;
import datadixit.limsbi.pojos.QSFolder;
import datadixit.limsbi.pojos.QuerySubject;
import datadixit.limsbi.pojos.RelationShip;
import sapphire.util.Logger;

public class TaskerSVC {

	static {
	}

	public static void start() {
		CognosSVC.logon();
		System.out.println("******** logon ***********");
		ProjectSVC.openModel();
		System.out.println("******** openmodel ***********");
		ProjectSVC.setLocale();
		System.out.println("******** setLocale	 ***********");

	}

	public static void stop() {
		// ProjectSVC.closeModel();
		ProjectSVC.saveModel();
		// ProjectSVC.closeModel();
		CognosSVC.logoff();
		System.out.println("PROGRAM END");

	}

	public static void init() {

	}

	public static void Import() {
		System.out.println("******** IMPORT	 ***********");
		FactorySVC.createNamespace("PHYSICAL", "STANDARD");
		FactorySVC.ImportDB("PHYSICAL");
	}

	/**
	 * will create security namespace, filters, and assign them to QSs
	 */

	public static void Security() {
		FactorySVC.createNamespace("SECURITY", "STANDARD");

		FactorySVC.createNamespace("L1", "SECURITY");
		FactorySVC.createNamespace("L2", "SECURITY");

		// sec tables
		// FactorySVC.createQuerySubject("PHYSICAL", "L1", "DEPARTMENTSYSUSER",
		// "_SEC_DEPARTMENTSYSUSER");
		// FactorySVC.createQuerySubject("PHYSICAL", "L1", "SYSUSER",
		// "_SEC_SYSUSER");
		// FactorySVC.createQuerySubject("PHYSICAL", "L1", "JOBTYPEDEPARTMENT",
		// "_SEC_JOBTYPEDEPARTMENT");
		// FactorySVC.createQuerySubject("PHYSICAL", "L1", "SYSUSERJOBTYPE",
		// "_SEC_SYSUSERJOBTYPE");

		// Setup security for SDCs

		// Setup security for non-SDCs, mostly SDI*

		ArrayList<String> alSecInfo = JDBCUtil.SECURITY();
		for (String s : alSecInfo) {

			if (!s.equals("")) {

				String tableid = s;
				String querySubject = s.toUpperCase();

				FactorySVC.createQuerySubject("PHYSICAL", "L1", querySubject, querySubject);
				FactorySVC.createQuerySubject("L1", "L2", querySubject, querySubject);

				// only make a filter if security is enabled

				if (JDBCUtil.isSecurityON(s)) {
					// 1) make 3 security tables for each SDC
					// 2) make 3 filters for each sec tab
					// 3) assign those 3 filters

					FactorySVC.createQuerySubject("PHYSICAL", "L1", "DEPARTMENTSYSUSER", "_SEC_DEPARTMENTSYSUSER_" + querySubject);
					FactorySVC.createQuerySubject("PHYSICAL", "L1", "SYSUSER", "_SEC_SYSUSER_" + querySubject);
					FactorySVC.createQuerySubject("PHYSICAL", "L1", "JOBTYPEDEPARTMENT", "_SEC_JOBTYPEDEPARTMENT_" + querySubject);
					FactorySVC.createQuerySubject("PHYSICAL", "L1", "SYSUSERJOBTYPE", "_SEC_SYSUSERJOBTYPE_" + querySubject);

					String fn1 = "sysuserid_" + querySubject;
					String fe1 = " <refobj>[L1].[_SEC_SYSUSER_" + querySubject + "].[SYSUSERID]</refobj> = #sq($account.parameters.defaultName)#";

					String fn2 = "departmentsysuserid_" + querySubject;
					String fe2 = " <refobj>[L1].[_SEC_DEPARTMENTSYSUSER_" + querySubject + "].[SYSUSERID]</refobj> = #sq($account.parameters.defaultName)#";

					String fn3 = "jobtypeid_" + querySubject;
					String fe3 = "<refobj>[L1].[_SEC_JOBTYPEDEPARTMENT_" + querySubject + "].[JOBTYPEID]</refobj> = #sq($account.parameters.jobtypeid_" + tableid + ")#";

					String fn4 = "sysuserjobtypeid_" + querySubject;
					String fe4 = "<refobj>[L1].[_SEC_SYSUSERJOBTYPE_" + querySubject + "].[SYSUSERID]</refobj> = #sq($account.parameters.defaultName)#";

					FactorySVC.createFilter(fn1, fe1, "L1");
					FactorySVC.assignFilter(fn1, "[L1].[_SEC_SYSUSER_" + querySubject + "]");

					FactorySVC.createFilter(fn2, fe2, "L1");
					FactorySVC.assignFilter(fn2, "[L1].[_SEC_DEPARTMENTSYSUSER_" + querySubject + "]");

					FactorySVC.createFilter(fn3, fe3, "L1");
					FactorySVC.assignFilter(fn3, "[L1].[_SEC_JOBTYPEDEPARTMENT_" + querySubject + "]");

					FactorySVC.createFilter(fn4, fe4, "L1");
					FactorySVC.assignFilter(fn4, "[L1].[_SEC_SYSUSERJOBTYPE_" + querySubject + "]");

					// FactorySVC.createFilter(FilterName, FilterExp,
					// "SECURITY");
					// FactorySVC.assignFilter("[SECURITY].[" + FilterName +
					// "]", "[SECURITY].[" + querySubject + "]");

					// link every sdc to its security tables
					RelationShip r1, r2, r3, r4;

					r1 = new RelationShip("[L1].[" + querySubject + "]", "[L1].[_SEC_SYSUSER_" + querySubject + "]");
					r2 = new RelationShip("[L1].[" + querySubject + "]", "[L1].[_SEC_DEPARTMENTSYSUSER_" + querySubject + "]");
					r3 = new RelationShip("[L1].[" + querySubject + "]", "[L1].[_SEC_JOBTYPEDEPARTMENT_" + querySubject + "]");
					r4 = new RelationShip("[L1].[" + querySubject + "]", "[L1].[_SEC_SYSUSERJOBTYPE_" + querySubject + "]");

					r1.setExpression(" <refobj>[L1].[" + querySubject + "].[SECURITYUSER]</refobj> = <refobj>[L1].[_SEC_SYSUSER_" + querySubject + "].[SYSUSERID]</refobj> ");
					r2.setExpression(" <refobj>[L1].[" + querySubject + "].[SECURITYDEPARTMENT]</refobj> = <refobj>[L1].[_SEC_DEPARTMENTSYSUSER_" + querySubject + "].[DEPARTMENTID]</refobj> ");
					r3.setExpression(" <refobj>[L1].[" + querySubject + "].[SECURITYDEPARTMENT]</refobj> = <refobj>[L1].[_SEC_JOBTYPEDEPARTMENT_" + querySubject + "].[DEPARTMENTID]</refobj>");
					r4.setExpression(" <refjob>[L1].[" + querySubject + "].[SECURITYUSER]</refjob> = <refjob>[L1].[_SEC_SYSUSERJOBTYPE_" + querySubject + "].[SYSUSERID]</refjob> ");

					r1.setParentNamespace("L1");
					r2.setParentNamespace("L1");
					r3.setParentNamespace("L1");
					r4.setParentNamespace("L1");

					FactorySVC.createRelationship(r1);
					FactorySVC.createRelationship(r2);
					FactorySVC.createRelationship(r3);
					FactorySVC.createRelationship(r4);

				}

			}

		}

		// Link Tables to the QSs
		// for (String s : alSecInfo) {
		// if (!s.equals("")) {
		// s.toUpperCase();
		// String querySubject = s.toUpperCase();
		//
		// if (JDBCUtil.isSecurityON(s)) {
		//
		// RelationShip RS1 = new RelationShip("[L1].[" + querySubject + "]",
		// "[L1].[_SEC_DEPARTMENTSYSUSER]");
		//
		// RelationShip RS2 = new RelationShip("[L1].[" + querySubject + "]",
		// "[L1].[_SEC_SYSUSER]");
		//
		// RelationShip RS3 = new RelationShip("[L1].[" + querySubject + "]",
		// "[L1].[_SEC_SYSUSERJOBTYPE]");
		//
		// RelationShip RS4 = new RelationShip("[L1].[" + querySubject + "]",
		// "[L1].[_SEC_JOBTYPEDEPARTMENT]");
		//
		// RS1.setExpression(" <refobj>[L1].[" + querySubject +
		// "].[SECURITYDEPARTMENT] </refobj> =
		// <refobj>[L1].[_SEC_DEPARTMENTSYSUSER].[DEPARTMENTID]</refobj> ");
		//
		// RS2.setExpression(" <refobj>[L1].[" + querySubject +
		// "].[SECURITYUSER]</refobj> =
		// <refobj>[L1].[_SEC_SYSUSER].[SYSUSERID]</refobj> ");
		//
		// RS3.setExpression(" <refjob>[L1].[" + querySubject +
		// "].[SECURITYUSER]</refjob> =
		// <refjob>[L1].[_SEC_SYSUSERJOBTYPE].[JOBTYPEID]</refjob> ");
		//
		// RS4.setExpression(" <refobj>[L1].[" + querySubject +
		// "].[SECURITYDEPARTMENT]</refobj> =
		// <refobj>[L1].[_SEC_JOBTYPEDEPARTMENT].[DEPARTMENTID]</refobj> ");
		//
		// RS1.setParentNamespace("L1");
		// RS2.setParentNamespace("L1");
		// RS3.setParentNamespace("L1");
		// RS4.setParentNamespace("L1");
		//
		// FactorySVC.createRelationship(RS1);
		// FactorySVC.createRelationship(RS2);
		// FactorySVC.createRelationship(RS3);
		// FactorySVC.createRelationship(RS4);
		//
		// }
		//
		// }
		// }

	}
	
	public static void IICInitNameSpace() {
		FactorySVC.createNamespace("AUTOGENERATION", "STANDARD");
		FactorySVC.createNamespace("FINAL", "AUTOGENERATION");
		FactorySVC.createNamespace("REF", "AUTOGENERATION");
		FactorySVC.createNamespace("DATA", "AUTOGENERATION");

		System.out.println("******** DATA	 ***********");
		
		
		
	}
	
	public static void IICCreateRelation(List<RelationShip> list){
		for(RelationShip rs: list){
			System.out.println(rs);
			System.out.println("******** CREATE RS ***********");
			FactorySVC.createRelationship(rs);
		}
	}
	
	public static void IICCreateRelation(){
		RelationShip RS = new RelationShip("[DATA].[S_SAMPLE_ALIAS]", "[DATA].[S_BATCH_ALIAS]");
		RS.setExpression("<refobj> [DATA].[S_SAMPLE_ALIAS].[BATCHID] </refobj> = <refobj> [DATA].[S_BATCH_ALIAS].[S_BATCHID] </refobj>");
//		RS.setExpression("[FINAL].[S_SAMPLE_ALIAS].[BATCHID] = [REF].[S_BATCH_ALIAS].[S_BATCHID]");
//		RS.setExpression("[S_SAMPLE_ALIAS].[BATCHID] = [S_BATCH_ALIAS].[S_BATCHID]");
		RS.setCard_left_min("one");
		RS.setCard_left_max("many");

		RS.setCard_right_min("one");
		RS.setCard_right_max("one");
		RS.setParentNamespace("DATA");
		
		System.out.println("******** CREATE RS ***********");
		
		FactorySVC.createRelationship(RS);
	}
	
	
	public static void IICCreateQuerySubject(){

		FactorySVC.createQuerySubject("PHYSICAL", "DATA", "S_SAMPLE", "S_SAMPLE_ALIAS");
		FactorySVC.createQuerySubject("PHYSICAL", "DATA", "S_BATCH", "S_BATCH_ALIAS");
		System.out.println("******** DATA	 ***********");
		
	}

	public static void Final() {
		FactorySVC.createNamespace("AUTOGENERATION", "STANDARD");
		FactorySVC.createNamespace("TABLES", "AUTOGENERATION");

		ArrayList<String> L = JDBCUtil.FINALES();

		for (String s : L) {
			if (!s.equals("")) {
				String tableid = JDBCUtil.getName(s).toUpperCase();
				String querySubject = s.toUpperCase();
				FactorySVC.createQuerySubject("L1", "TABLES", tableid, querySubject);
			}

		}

	}

	public static void RefSDC() {
		FactorySVC.createNamespace("TABLES_RefSDC", "AUTOGENERATION");
		ArrayList<String> L = JDBCUtil.REFSDC();

		for (String s : L) {
			if (!s.equals("")) {
				String tableid = JDBCUtil.getName(s).toUpperCase();
				String querySubject = s.toUpperCase();
				FactorySVC.createQuerySubject("L1", "TABLES_RefSDC", tableid, querySubject);
			}

		}

	}

	public static void Translanguage() {
		FactorySVC.createNamespace("TRADUCTION", "AUTOGENERATION");
		Integer Max = JDBCUtil.MAX();
		for (int i = 0; i < Max; i++) {
			FactorySVC.createQuerySubject("PHYSICAL", "TRADUCTION", "TRANSLANGUAGE", "TRANSLANGUAGE" + i);
		}

	}

	public static void LinkTransLanguage() {

		ArrayList<String> Finales = JDBCUtil.FINALES();
		ArrayList<String> RefSDCs = JDBCUtil.REFSDC();

		for (String s : Finales) {
			if (!s.equals("")) {
				String h1, h2;
				h1 = "[TABLES].[" + s.toUpperCase() + "]";
				h2 = "[TRADUCTION].[TRANSLANGUAGE";
				ArrayList<String> TranslantedColumns = JDBCUtil.getTranslantedColumns(s, "Final");
				int ct = 0;
				for (String c : TranslantedColumns) {
					String h11 = h1;
					String h22 = h2 + ct + "]";
					RelationShip RS = new RelationShip(h11, h22);

					RS.setParentNamespace("AUTOGENERATION");
					RS.setExpression(" <refobj>" + h1 + ".[" + c.toUpperCase() + "]</refobj>=<refobj>" + h22 + ".[TEXTID]</refobj> AND <refobj>" + h22 + ".[LANGUAGEID]</refobj> = #sq($runLocale)# ");

					FactorySVC.createRelationship(RS);
					ct++;

				}
			}
		}
		for (String s : RefSDCs) {
			if (!s.equals("")) {
				String h1, h2;
				h1 = "[TABLES_RefSDC].[" + s.toUpperCase() + "]";
				h2 = "[TRADUCTION].[TRANSLANGUAGE";
				ArrayList<String> TranslantedColumns = JDBCUtil.getTranslantedColumns(s, "RefSDC");
				int ct = 0;
				for (String c : TranslantedColumns) {
					String h11 = h1;
					String h22 = h2 + ct + "]";
					RelationShip RS = new RelationShip(h11, h22);

					RS.setParentNamespace("AUTOGENERATION");
					RS.setExpression(" <refobj>" + h1 + ".[" + c.toUpperCase() + "]</refobj>=<refobj>" + h22 + ".[TEXTID]</refobj> AND <refobj>" + h22 + ".[LANGUAGEID]</refobj> = #sq($runLocale)# ");

					FactorySVC.createRelationship(RS);
					ct++;

				}
			}

		}

	}

	public static void Final_T() {
		FactorySVC.createNamespace("TABLES_T", "AUTOGENERATION");
		ArrayList<String> Finales = JDBCUtil.FINALES();
		for (String s : Finales) {
			if (!s.equals("")) {
				FactorySVC.createQuerySubject("TABLES", "TABLES_T", s.toUpperCase());

			}

		}

	}

	// ! \\ ce namespace contiendra aussi les M2M!!
	public static void RefSDC_T() {
		FactorySVC.createNamespace("TABLES_RefSDC_T", "AUTOGENERATION");
		ArrayList<String> REFSDCs = JDBCUtil.REFSDC();
		for (String s : REFSDCs) {
			if (!s.equals("")) {
				FactorySVC.createQuerySubject("TABLES_RefSDC", "TABLES_RefSDC_T", s.toUpperCase());
			}

		}
		// FactorySVC.createQuerySubject("PHYSICAL", "TABLES_RefSDC_T",
		// "REFVALUE");

	}
	/* va créer les tables de jointures necessaires qui sont coché */

	public static void M2M_T() {
		// FactorySVC.createNamespace("TABLES_RefSDC_T", "AUTOGENERATION");
		ArrayList<String> M2Ms = JDBCUtil.M2M();
		for (String s : M2Ms) {
			if (!s.equals("")) {
				FactorySVC.createQuerySubject("PHYSICAL", "TABLES_RefSDC_T", s.toUpperCase());
			}

		}

	}

	public static void Fields_T() {
		ArrayList<String> Finales = JDBCUtil.FINALES();
		ArrayList<String> REFSDCs = JDBCUtil.REFSDC();
		for (String s : Finales) {
			ArrayList<String> TranslantedColumns = JDBCUtil.getTranslantedColumns(s, "Final");
			String h1, h2;
			h1 = "[TABLES_T].[" + s.toUpperCase() + "]";
			h2 = "[TRADUCTION].[TRANSLANGUAGE";
			int ct = 0;
			for (String c : TranslantedColumns) {
				String h11 = h1;
				String h22 = h2 + ct + "]";

				String xp = "if  (" + h22 + ".[TRANSTEXT]  is null  ) then  (" + h11 + ".[" + c.toUpperCase() + "]) else (" + h22 + ".[TRANSTEXT] )";

				FactorySVC.createQueryItem(h11, c.toUpperCase() + "_T", xp);
				// order _T
				String ReorderBefore = JDBCUtil.ReorderBefore(s, c);
				if (!ReorderBefore.equals("")) {
					String h, t;
					h = h11 + ".[" + c.toUpperCase() + "_T" + "]";
					t = h11 + ".[" + ReorderBefore.toUpperCase() + "]";

					FactorySVC.ReorderBefore(h, t);
				}
				ct++;
			}
		}

		for (String s : REFSDCs) {
			ArrayList<String> TranslantedColumns = JDBCUtil.getTranslantedColumns(s, "RefSDC");
			String h1, h2;
			h1 = "[TABLES_RefSDC_T].[" + s.toUpperCase() + "]";
			h2 = "[TRADUCTION].[TRANSLANGUAGE";
			int ct = 0;
			for (String c : TranslantedColumns) {
				String h11 = h1;
				String h22 = h2 + ct + "]";

				String xp = "if  (" + h22 + ".[TRANSTEXT]  is null  ) then  (" + h11 + ".[" + c.toUpperCase() + "]) else (" + h22 + ".[TRANSTEXT] )";

				FactorySVC.createQueryItem(h11, c.toUpperCase() + "_T", xp);
				// order _T
				String ReorderBefore = JDBCUtil.ReorderBefore(s, c);
				if (!ReorderBefore.equals("")) {
					String h, t;
					h = h11 + ".[" + c.toUpperCase() + "_T" + "]";
					t = h11 + ".[" + ReorderBefore.toUpperCase() + "]";

					FactorySVC.ReorderBefore(h, t);
				}
				ct++;
			}

		}

	}

	// utilisé localement pour créer un seul Noeud Final, par ex une Sample
	private static void MakeTree(QuerySubject N) {
		try {
			NodeSVC.WriteTreeToModel(N);
			NodeSVC.PrettyPrint(N);
			NodeSVC.WriteTreeLinksToModel(N);
		} catch (Exception ex) {
			lg(ex.getMessage());
		}
	}

	// va créer et lier toutes les arbres en commançant par les Finals
	// Namespaces concernés RefDC/TABLES_UTIL
	public static void MakeTrees() {
		FactorySVC.createNamespace("TABLES_UTIL", "AUTOGENERATION");
		FactorySVC.createNamespace("RefSDC", "AUTOGENERATION");
		ArrayList<String> Finales = JDBCUtil.FINALES();
		for (String s : Finales) {
			// it will make the entire tree
			if (!s.equals("")) {
				// myNode N = new myNode(s);
				// N.Make();

				// myNode N = MemoizeTree.MakeNode(s);
				QuerySubject N = NodeSVC.Memoize(s);
				MakeTree(N);
			}

		}

	}

	// ca créer un noeud final dans un dossier au choix et y créer
	// les dossiers internes
	// ! \\ va devenir privé une fois le test finie
	private static void MakeSubFolders(QuerySubject N) {
		NodeSVC.MakeSubFolders(N);
	}

	private static void MakeSubFoldersXX(QuerySubject N) {
		NodeSVC.MakeSubFoldersXX(N);
	}

	public static void MakeMainFolders() {

		FactorySVC.createNamespace("DYNAMIC DATA", "AUTOGENERATION");

		for (QSFolder F : QSFolder.ROOTS) {

			NodeSVC.MakeMainFolder(F);

		}

	}

	public static void MakeAllSubFolders() {
		FactorySVC.createNamespace("REFERENCE DATA", "AUTOGENERATION");

		// myNode N = new myNode("s_sample");
		// N.Make();
		// MakeSubFolders(N);
		ArrayList<String> Finales = JDBCUtil.FINALES();
		for (String s : Finales) {
			// it will make the entire tree
			if (!s.equals("")) {
				// myNode N = new myNode(s);
				// N.Make();
				// myNode N = MemoizeTree.MakeNode(s);
				QuerySubject N = NodeSVC.Memoize(s);
				MakeSubFolders(N);
			}

		}
	}

	public static void MakeAllSubFoldersXX() {
		// FactorySVC.createNamespace("TABLES_UTIL", "AUTOGENERATION");
		// FactorySVC.createNamespace("RefSDC", "AUTOGENERATION");
		// ArrayList<String> Finales = JDBCUtil.FINALES();
		// for (String s : Finales) {
		// //it will make the entire tree
		// myNode N = new myNode(s);
		// make(N);
		// }
		// FactorySVC.createNamespace("DYNAMIC DATA", "AUTOGENERATION");

		// myNode N = new myNode("s_sample");
		// N.Make();
		// MakeSubFolders(N);
		ArrayList<String> Finales = JDBCUtil.FINALES();
		for (String s : Finales) {
			// it will make the entire tree
			if (!s.equals("")) {
				// myNode N = new myNode(s);
				// N.Make();
				// myNode N = MemoizeTree.MakeNode(s);
				QuerySubject N = NodeSVC.Memoize(s);
				MakeSubFoldersXX(N);
			}

		}
	}

	public static void publish() {

		// ProjectSVC.publishpackage();
		ProjectSVC.publish();

	}

	public static void MakeFinaleLinks() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		ArrayList<String> Finales = JDBCUtil.FINALES();
		for (String s : Finales) {
			// it will make the entire tree
			if (!s.equals("")) {
				// myNode N = new myNode(s);
				// N.Make();
				// myNode N = MemoizeTree.MakeNode(s);
				QuerySubject N = NodeSVC.Memoize(s);
				NodeSVC.WriteFinalSelectedLinksToModel(N);
				// MakeSubFolders(N);
			}

		}

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}

}
