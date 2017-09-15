/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datadixit.limsbi.svc;

import java.util.ArrayList;
import java.util.logging.Level;

import datadixit.limsbi.db.JDBCUtil;
import datadixit.limsbi.pojos.C;
import datadixit.limsbi.pojos.D;
import datadixit.limsbi.pojos.DE;
import datadixit.limsbi.pojos.F;
import datadixit.limsbi.pojos.FS;
import datadixit.limsbi.pojos.I;
import datadixit.limsbi.pojos.M;
import datadixit.limsbi.pojos.QSFolder;
import datadixit.limsbi.pojos.QuerySubject;
import datadixit.limsbi.pojos.RF;
import datadixit.limsbi.pojos.RI;
import datadixit.limsbi.pojos.RS;
import datadixit.limsbi.pojos.RelationShip;
import datadixit.limsbi.pojos.S;
import datadixit.limsbi.pojos.V;
import sapphire.util.Logger;

/**
 *
 * @author splims
 */
public class NodeSVC {

	public static ArrayList<QuerySubject> lookup;

	static {
		lookup = new ArrayList<QuerySubject>(50);
	}

	public static void WriteTreeToModel(QuerySubject N) {
		if (N.Parent == null) {
			FactorySVC.createQuerySubject("TABLES_T", "TABLES_UTIL", N.U_Name.toUpperCase(), N.QuerySubject);
		} else {
			FactorySVC.createQuerySubject("TABLES_RefSDC_T", "RefSDC", N.U_Name.toUpperCase(), N.QuerySubject);

		}
		for (QuerySubject C : N.Children) {
			WriteTreeToModel(C);
		}
	}

	private static void WriteF(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("F")) {

			F FKs = (F) C.L;
			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = FKs.SDCCOLUMNID.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID.toUpperCase() + "]";
			String sdc2 = FKs.SDCCOLUMNID2.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID2.toUpperCase() + "]";
			;
			String sdc3 = FKs.SDCCOLUMNID3.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID3.toUpperCase() + "]";
			;

			String keyid1 = FKs.KeyID.equals("") ? "" : to + ".[" + FKs.KeyID.toUpperCase() + "]";
			String keyid2 = FKs.KeyID2.equals("") ? "" : to + ".[" + FKs.KeyID2.toUpperCase() + "]";
			String keyid3 = FKs.KeyID3.equals("") ? "" : to + ".[" + FKs.KeyID3.toUpperCase() + "]";
			String xp = "";

			if (sdc.equals("")) {
				try {
					throw new Exception("FK but even SDCCOLUMNID1 is empty/null !! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
			}
			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("many");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);
		}

	}

	private static void WriteFS(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("FS")) {

			FS FKs = (FS) C.L;

			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("sdcid").toUpperCase() + "]";
			String sdc1 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid1").toUpperCase() + "]";
			String sdc2 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid2").toUpperCase() + "]";
			String sdc3 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid3").toUpperCase() + "]";

			String sdcid = FKs.SDCID.equals("") ? "" : to + ".[" + FKs.SDCID.toUpperCase() + "]";
			String keyid1 = FKs.keyID1.equals("") ? "" : to + ".[" + FKs.keyID1.toUpperCase() + "]";
			String keyid2 = FKs.keyID2.equals("") ? "" : to + ".[" + FKs.keyID2.toUpperCase() + "]";
			String keyid3 = FKs.keyID3.equals("") ? "" : to + ".[" + FKs.keyID3.toUpperCase() + "]";

			String xp = "";

			if (sdcid.equals("")) {
				try {
					throw new Exception("!!!!!!!!! LIEN FS SANS SDC !!!!!! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = '" + FKs.SDCID + "'";
			}

			if (!sdc1.equals("")) {
				xp = xp + "and <refobj>" + sdc1 + "</refobj> = <refobj>" + keyid1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
			}
			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("many");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);
		}

	}

	private static void WriteRS(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("RS")) {

			RS FKs = (RS) C.L;

			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = FKs.Prefix.equals("") ? "" : to + ".[" + FKs.Prefix.concat("sdcid").toUpperCase() + "]";
			String sdc1 = FKs.Prefix.equals("") ? "" : to + ".[" + FKs.Prefix.concat("keyid1").toUpperCase() + "]";
			String sdc2 = FKs.Prefix.equals("") ? "" : to + ".[" + FKs.Prefix.concat("keyid2").toUpperCase() + "]";
			String sdc3 = FKs.Prefix.equals("") ? "" : to + ".[" + FKs.Prefix.concat("keyid3").toUpperCase() + "]";

			String keyid1 = FKs.keyID1.equals("") ? "" : from + ".[" + FKs.keyID1.toUpperCase() + "]";
			String keyid2 = FKs.keyID2.equals("") ? "" : from + ".[" + FKs.keyID2.toUpperCase() + "]";
			String keyid3 = FKs.keyID3.equals("") ? "" : from + ".[" + FKs.keyID3.toUpperCase() + "]";

			String xp = "";

			if (sdc.equals("")) {
				try {
					throw new Exception("!!!!!!!!! LIEN RS SANS SDC !!!!!! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = '" + FKs.SDCID + "'";
			}

			if (!sdc1.equals("")) {
				xp = xp + "and <refobj>" + keyid1 + "</refobj> = <refobj>" + sdc1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + keyid2 + "</refobj> = <refobj>" + sdc2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + keyid3 + "</refobj> = <refobj>" + sdc3 + "</refobj>";
			}
			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("one");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("many");
			FactorySVC.createRelationship(RS);
		}

	}

	private static void WriteRF(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("RF")) {

			RF FKs = (RF) C.L;
			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = FKs.SDCCOLUMNID.equals("") ? "" : to + ".[" + FKs.SDCCOLUMNID.toUpperCase() + "]";
			String sdc2 = FKs.SDCCOLUMNID2.equals("") ? "" : to + ".[" + FKs.SDCCOLUMNID2.toUpperCase() + "]";
			;
			String sdc3 = FKs.SDCCOLUMNID3.equals("") ? "" : to + ".[" + FKs.SDCCOLUMNID3.toUpperCase() + "]";
			;

			String keyid1 = FKs.KeyID.equals("") ? "" : from + ".[" + FKs.KeyID.toUpperCase() + "]";
			String keyid2 = FKs.KeyID2.equals("") ? "" : from + ".[" + FKs.KeyID2.toUpperCase() + "]";
			String keyid3 = FKs.KeyID3.equals("") ? "" : from + ".[" + FKs.KeyID3.toUpperCase() + "]";
			String xp = "";

			if (FKs.isUseFieldName) {

				for (String CommunColumn : FKs.CommunColumns) {
					String de = from + ".[" + CommunColumn.toUpperCase() + "]";
					String a = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + de + "</refobj> = <refobj>" + a + "</refobj>";
					}
					if (FKs.CommunColumns.indexOf(CommunColumn) < FKs.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}

			} else {

				if (sdc.equals("")) {
					try {
						throw new Exception("FK but even SDCCOLUMNID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + keyid1 + "</refobj> = <refobj>" + sdc + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + keyid2 + "</refobj> = <refobj>" + sdc2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + keyid3 + "</refobj> = <refobj>" + sdc3 + "</refobj>";
				}

			}

			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("one");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("many");
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteV(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("V")) {
			V v = (V) C.L;
			String xp = "";
			// col1
			String sdc = v.SDCCOLUMNID.equals("") ? "" : from + ".[" + v.SDCCOLUMNID.toUpperCase() + "]";
			// col2 (refvalue.refvalueid)
			String refvalueid = to + ".[REFVALUEID]";
			String reftypeid = to + ".[REFTYPEID]";
			xp = "<refobj>" + sdc + "</refobj> = <refobj>" + refvalueid + "</refobj>";
			xp = xp + "and <refobj>" + reftypeid + "</refobj> = '" + v.RefType + "'";
			RS.setExpression(xp);
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteS(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("S")) {
			S s = (S) C.L;
			String ParentPrefix = s.TableID.substring(0, 3).toLowerCase();
			lg("ParentPrefix = " + ParentPrefix);
			Boolean isSDI = ParentPrefix.equals("sdi");

			String xp = "";
			if (isSDI) {
				// throw new Exception("les relations sdi - sdi non encore prise
				// en charge");
				for (String CommunColumn : s.CommunColumns) {
					String de = from + ".[" + CommunColumn.toUpperCase() + "]";
					String a = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + de + "</refobj> = <refobj>" + a + "</refobj>";
					}
					if (s.CommunColumns.indexOf(CommunColumn) < s.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}
			} else {
				String sdc = s.KeyID1.equals("") ? "" : from + ".[" + s.KeyID1.toUpperCase() + "]";
				String sdc2 = s.KeyID2.equals("") ? "" : from + ".[" + s.KeyID2.toUpperCase() + "]";
				;
				String sdc3 = s.KeyID3.equals("") ? "" : from + ".[" + s.KeyID3.toUpperCase() + "]";
				;

				String keyid1 = s.KeyID1.equals("") ? "" : to + ".[KEYID1]";
				String keyid2 = s.KeyID2.equals("") ? "" : to + ".[KEYID2]";
				String keyid3 = s.KeyID3.equals("") ? "" : to + ".[KEYID3]";
				String SDCID = s.SdcID.equals("") ? "" : to + ".[SDCID]";

				if (sdc.equals("")) {
					try {
						throw new Exception("M but  even KEY ID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
				}
				// sdcid
				// lg("XXXXX SDCID = " + SDCID);
				xp = xp + "and <refobj>" + SDCID + "</refobj> = '" + s.SdcID + "'";

			}

			RS.setExpression(xp);
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteI(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("I")) {
			I de = (I) C.L;
			String xp = "";

			if (C.L.isUseFieldName) {
				// throw new Exception("les relations sdi - sdi non encore prise
				// en charge");
				for (String CommunColumn : de.CommunColumns) {
					String dee = from + ".[" + CommunColumn.toUpperCase() + "]";
					String aa = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + dee + "</refobj> = <refobj>" + aa + "</refobj>";
					}
					if (de.CommunColumns.indexOf(CommunColumn) < de.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}
			} else {
				String keyid1 = de.KeyID1.equals("") ? "" : to + ".[" + de.KeyID1.toUpperCase() + "]";
				String keyid2 = de.KeyID2.equals("") ? "" : to + ".[" + de.KeyID2.toUpperCase() + "]";
				;
				String keyid3 = de.KeyID3.equals("") ? "" : to + ".[" + de.KeyID3.toUpperCase() + "]";
				;

				String sdc = de.KeyID1.equals("") ? "" : from + ".[KEYID1]";
				String sdc2 = de.KeyID2.equals("") ? "" : from + ".[KEYID2]";
				String sdc3 = de.KeyID3.equals("") ? "" : from + ".[KEYID3]";
				String SDCID = de.SdcID.equals("") ? "" : from + ".[SDCID]";

				if (sdc.equals("")) {
					try {
						throw new Exception("M but  even KEY ID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
				}
				// sdcid
				// lg("XXXXX SDCID = " + SDCID);
				xp = xp + "and <refobj>" + SDCID + "</refobj> = '" + de.SdcID + "'";

			}

			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("many");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteRI(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("RI")) {
			RI ri = (RI) C.L;
			String xp = "";
			if (ri.isUseFieldName) {
				// throw new Exception("les relations sdi - sdi non encore prise
				// en charge");
				for (String CommunColumn : ri.CommunColumns) {
					String de = from + ".[" + CommunColumn.toUpperCase() + "]";
					String a = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + de + "</refobj> = <refobj>" + a + "</refobj>";
					}
					if (ri.CommunColumns.indexOf(CommunColumn) < ri.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}
			} else {
				String sdc = ri.KeyID1.equals("") ? "" : from + ".[" + ri.KeyID1.toUpperCase() + "]";
				String sdc2 = ri.KeyID2.equals("") ? "" : from + ".[" + ri.KeyID2.toUpperCase() + "]";
				;
				String sdc3 = ri.KeyID3.equals("") ? "" : from + ".[" + ri.KeyID3.toUpperCase() + "]";
				;

				String keyid1 = ri.KeyID1.equals("") ? "" : to + ".[KEYID1]";
				String keyid2 = ri.KeyID2.equals("") ? "" : to + ".[KEYID2]";
				String keyid3 = ri.KeyID3.equals("") ? "" : to + ".[KEYID3]";
				String SDCID = ri.SdcID.equals("") ? "" : to + ".[SDCID]";

				if (sdc.equals("")) {
					try {
						throw new Exception("M but  even KEY ID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
				}
				// sdcid
				// lg("XXXXX SDCID = " + SDCID);
				xp = xp + "and <refobj>" + SDCID + "</refobj> = '" + ri.SdcID + "'";

			}

			RS.setExpression(xp);
			RS.setCard_left_min("zero");
			RS.setCard_left_max("one");

			RS.setCard_right_min("zero");
			RS.setCard_right_max("many");
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteM(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);

		if (C.L.LinkType.equals("M1") || C.L.LinkType.equals("M2")) {
			M m = (M) C.L;

			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = m.KeyID1.equals("") ? "" : from + ".[" + m.KeyID1.toUpperCase() + "]";
			String sdc2 = m.KeyID2.equals("") ? "" : from + ".[" + m.KeyID2.toUpperCase() + "]";
			;
			String sdc3 = m.KeyID3.equals("") ? "" : from + ".[" + m.KeyID3.toUpperCase() + "]";
			;

			String keyid1 = m.KeyID1.equals("") ? "" : to + ".[" + m.KeyID1.toUpperCase() + "]";
			String keyid2 = m.KeyID2.equals("") ? "" : to + ".[" + m.KeyID2.toUpperCase() + "]";
			String keyid3 = m.KeyID3.equals("") ? "" : to + ".[" + m.KeyID3.toUpperCase() + "]";
			String xp = "";

			if (sdc.equals("")) {
				try {
					throw new Exception("M but  even KEY ID1 is empty/null !! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
			}
			RS.setExpression(xp);

			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteD(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.Parent.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);

		if (C.L.LinkType.equals("D")) {

			D Ds = (D) C.L;
			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = Ds.KeyID1.equals("") ? "" : from + ".[" + Ds.KeyID1.toUpperCase() + "]";
			String sdc2 = Ds.KeyID2.equals("") ? "" : from + ".[" + Ds.KeyID2.toUpperCase() + "]";
			;
			String sdc3 = Ds.KeyID3.equals("") ? "" : from + ".[" + Ds.KeyID3.toUpperCase() + "]";
			;

			String keyid1 = Ds.KeyID1.equals("") ? "" : to + ".[" + Ds.KeyID1.toUpperCase() + "]";
			String keyid2 = Ds.KeyID2.equals("") ? "" : to + ".[" + Ds.KeyID2.toUpperCase() + "]";
			String keyid3 = Ds.KeyID3.equals("") ? "" : to + ".[" + Ds.KeyID3.toUpperCase() + "]";
			String xp = "";

			if (sdc.equals("")) {
				try {
					throw new Exception("FK but even SDCCOLUMNID1 is empty/null !! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
			}
			RS.setExpression(xp);

			FactorySVC.createRelationship(RS);
		}

	}

	public static void WriteTreeLinksToModel(QuerySubject N) {
		String srcNameSpace = N.Parent == null ? "TABLES_UTIL" : "RefSDC";
		String dstNameSpace = "RefSDC";
		String prntNameSpace = srcNameSpace.equals("RefSDC") ? "RefSDC" : "AUTOGENERATION";

		for (QuerySubject C : N.Children) {
			if (C.L.isF2F == false) {
				WriteF(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteRF(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteFS(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteRS(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteI(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteRI(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteV(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteM(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteD(C, srcNameSpace, dstNameSpace, prntNameSpace);
				WriteS(C, srcNameSpace, dstNameSpace, prntNameSpace);
			}

		}

		for (QuerySubject C : N.Children) {
			WriteTreeLinksToModel(C);
		}
	}

	public static void WriteFinalSelectedLinksToModel(QuerySubject N) {
		String srcNameSpace = "DYNAMIC DATA";
		String dstNameSpace = "DYNAMIC DATA";
		String prntNameSpace = "DYNAMIC DATA";

		for (QuerySubject C : N.LinkedFinales) {
			if (C.L.LinkType.equals("F") && C.L.isF2F == true) {
				WriteF_F(C, srcNameSpace, dstNameSpace, prntNameSpace);
			}
			if (C.L.LinkType.equals("DE") && C.L.isF2F == true) {
				WriteF_DE(C, srcNameSpace, dstNameSpace, prntNameSpace);
			}
			// if (C.L.LinkType.equals("RF") && C.L.isF2F == true) {
			// WriteF_RF(C, srcNameSpace, dstNameSpace, prntNameSpace);
			// }
			if (C.L.LinkType.equals("FS") && C.L.isF2F == true) {
				WriteF_FS(C, srcNameSpace, dstNameSpace, prntNameSpace);
			}
			if (C.L.LinkType.equals("FS") && C.L.isF2F == true) {
				WriteF_C(C, srcNameSpace, dstNameSpace, prntNameSpace);
			}

			// WriteV(C, srcNameSpace, dstNameSpace, prntNameSpace);
			// WriteM(C, srcNameSpace, dstNameSpace, prntNameSpace);
			// WriteD(C, srcNameSpace, dstNameSpace, prntNameSpace);
			// WriteS(C, srcNameSpace, dstNameSpace, prntNameSpace);
		}

	}

	// va créer les sous dossiers de la QuerySubject (handle)
	public static void MakeSubFolders(QuerySubject N) {
		N.PrettyPrint();
		if (N.Parent == null) {
			// FactorySVC.createFolder("[DYNAMIC DATA]", N.QuerySubject +
			// ".DD");
			// FactorySVC.createQuerySubjectInFolder("TABLES_T", "DYNAMIC DATA",
			// N.QuerySubject + ".DD", N.QuerySubject);
			// FactorySVC.createQuerySubject("TABLES_RefSDC", "TABLES_RefSDC_T",
			// s.toUpperCase());
			FactorySVC.createQuerySubject("TABLES_T", "REFERENCE DATA", N.QuerySubject);
		}

		try {
			// ! \\ICI que des F2R
			MakeSubFoldersF(N);
			MakeSubFoldersFS(N);
			MakeSubFoldersRS(N);
			MakeSubFoldersRF(N);
			MakeSubFoldersRI(N);
			MakeSubFoldersI(N);
			MakeSubFoldersV(N);
			MakeSubFoldersM2M(N);
			MakeSubFoldersS(N);
			MakeSubFoldersD(N);
		} catch (Exception e) {
			lg(e.getMessage());
		}

		for (QuerySubject C : N.Children) {
			MakeSubFolders(C);
		}
		// ...
		// créer les dossiers

	}

	public static void MakeSubFoldersXX(QuerySubject N) {
		N.PrettyPrint();
		if (N.Parent == null) {
			// FactorySVC.createFolder("[DYNAMIC DATA]", N.QuerySubject +
			// ".DD");
			// FactorySVC.createQuerySubjectInFolder("TABLES_T", "DYNAMIC DATA",
			// N.QuerySubject + ".DD", N.QuerySubject);
			// FactorySVC.createQuerySubject("TABLES_RefSDC", "TABLES_RefSDC_T",
			// s.toUpperCase());
			// FactorySVC.createQuerySubject("TABLES_T", "DYNAMIC DATA",
			// N.QuerySubject);

			if (N.FinalFolder.equals("")) {
				FactorySVC.createQuerySubject("TABLES_T", "DYNAMIC DATA", N.QuerySubject);
			} else {
				FactorySVC.createQuerySubjectInFolder("TABLES_T", "DYNAMIC DATA", N.FinalFolder, N.QuerySubject);
			}
		}

		try {
			// ! \\ICI que des F2R
			MakeSubFoldersFXX(N);
			MakeSubFoldersFSXX(N);
			MakeSubFoldersRSXX(N);
			MakeSubFoldersRFXX(N);
			MakeSubFoldersRIXX(N);
			MakeSubFoldersIXX(N);
			MakeSubFoldersVXX(N);
			MakeSubFoldersM2MXX(N);
			MakeSubFoldersSXX(N);
			MakeSubFoldersDXX(N);
		} catch (Exception e) {
			lg(e.getMessage());
		}

		for (QuerySubject C : N.Children) {
			MakeSubFoldersXX(C);
		}
		// ...
		// créer les dossiers

	}

	// public static void WriteTreeToDynamicData(myNode N) {
	//
	// }
	public static void PrettyPrint(QuerySubject N) {
		N.PrettyPrint();
	}
	// une fonction qui créera les dossiers selon le type F, M2M, D, V etc

	private static void MakeSubFoldersF(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof F) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				F f = (F) C.L;
				String ColumnReorg = f.SDCCOLUMNID;
				if (!f.SDCCOLUMNID3.equals("")) {
					ColumnReorg = f.SDCCOLUMNID3;
				} else if (!f.SDCCOLUMNID2.equals("")) {
					ColumnReorg = f.SDCCOLUMNID2;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	private static void MakeSubFoldersRF(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RF) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				RF rf = (RF) C.L;
				String ColumnReorg = rf.KeyID;
				if (!rf.KeyID2.equals("")) {
					ColumnReorg = rf.KeyID2;
				} else if (!rf.KeyID3.equals("")) {
					ColumnReorg = rf.KeyID3;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	private static void MakeSubFoldersV(QuerySubject N) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		for (QuerySubject C : N.Children) {

			if (C.L instanceof V) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// si notre dossier est dans la 1ere couche ou non, cad
				// directement dans le QS ou bien imbriqué
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// création des QI de la refvalue
				// commençons par l'exemple d'une refvalue desc*
				// j'ai laissé toUpperCase() pour utiliser des variable si on
				// veut rapatrier plusieurs chalps
				// ^ keep in mind ^\\
				String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + "REFVALUEDESC".toUpperCase() + "]";
				String Name = C.QueryItemPrefix + "REFVALUEDESC".toUpperCase();
				FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);

				// ! \\ reorganiser les dossiers
				V v = (V) C.L;
				String ColumnReorg = v.SDCCOLUMNID;
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}
		}
	}

	private static void MakeSubFoldersM2M(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof M) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// si le lien est de type M1, on crée le dossier
				if (C.L.LinkType.equals("M1")) {
					// si notre dossier est dans la 1ere couche ou non, cad
					// directement dans le QS ou bien imbriqué
					if (N.Parent == null) {
						FactorySVC.createSubFolder(QSrefobj, C.Folder);
					} else {
						FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
					}

					String Name = C.QueryItemPrefix + "usersequence".toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + "usersequence".toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);

					// reorg après la dernier PK
					M m = (M) C.L;
					String ColumnReorg = m.KeyID1;
					if (!m.KeyID3.equals("")) {
						// reorg before key 3
						ColumnReorg = m.KeyID3;
					} else if (!m.KeyID2.equals("")) {
						// reorg before key id 2
						ColumnReorg = m.KeyID3;
					}

					// ! \\ reorganiser les dossiers
					String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
					// String h, t;
					String hf, tf;
					if (ReorderBefore.equals("")) {
						tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
						hf = QSrefobj + ".[" + C.Folder + "]";
						// lg("tf = " + tf);
						// lg("hf = " + hf);
						FactorySVC.ReorderSubFolderBefore(hf, tf);
					}

					// ! \\ FIN reorganiser les dossiers
				}

				// si le lien est de type M2, on crée les QIs de M2
				if (C.L.LinkType.equals("M2")) {
					// ajouter les elements de requêtes

					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);

					ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
					ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
					for (String c : Columns) {
						// String FolderHandle = QSrefobj + ".[" + C.Folder +
						// "]";

						String Name = C.QueryItemPrefix + c.toUpperCase();
						String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
						FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
					}

					// ajouter les champs traduits _T
					for (String c : TranslatedColumns) {
						// String FolderHandle = QSrefobj + ".[" + C.Folder +
						// "]";
						String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
						String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
						FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
					}
					// organiser les colonnes _T au dessous de leurs champs
					for (String c : TranslatedColumns) {
						// order _T
						String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
						if (!ReorgBefore.equals("")) {
							String hx, tx;
							String CurrentFolderx = QSrefobj;
							hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
							tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
							FactorySVC.ReorderBefore(hx, tx);
						}
					}

				}

			}
		}
	}

	private static void MakeSubFoldersS(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof S) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				S s = (S) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = s.KeyID1;
				// void si le parent est une sdi
				String ParentPrefix = s.TableID.substring(0, 3).toLowerCase();
				Boolean isSDI = ParentPrefix.equals("sdi");

				if (isSDI) {
					ColumnReorg = "keyid3";
				} else {
					if (!s.KeyID3.equals("")) {
						// reorg before key 3
						ColumnReorg = s.KeyID3;
					} else if (!s.KeyID2.equals("")) {
						// reorg before key id 2
						ColumnReorg = s.KeyID3;
					}
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersRI(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RI) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				RI ri = (RI) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = ri.KeyID1;
				// void si le parent est une sdi
				// String ParentPrefix = s.TableID.substring(0,
				// 3).toLowerCase();
				// Boolean isSDI = ParentPrefix.equals("sdi");

				if (!ri.KeyID3.equals("")) {
					// reorg before key 3
					ColumnReorg = ri.KeyID3;
				} else if (!ri.KeyID2.equals("")) {
					// reorg before key id 2
					ColumnReorg = ri.KeyID3;
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";

					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersI(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof I) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				I ii = (I) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg;

				ColumnReorg = "keyid3";
				// if (isSDI) {
				// ColumnReorg = "keyid3";
				// } else {
				// if (!s.KeyID3.equals("")) {
				// //reorg before key 3
				// ColumnReorg = s.KeyID3;
				// } else if (!s.KeyID2.equals("")) {
				// //reorg before key id 2
				// ColumnReorg = s.KeyID3;
				// }
				// }

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersFS(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof FS) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				FS fs = (FS) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg;

				ColumnReorg = fs.Prefix.concat("sdcid");
				// if (isSDI) {
				// ColumnReorg = "keyid3";
				// } else {
				// if (!s.KeyID3.equals("")) {
				// //reorg before key 3
				// ColumnReorg = s.KeyID3;
				// } else if (!s.KeyID2.equals("")) {
				// //reorg before key id 2
				// ColumnReorg = s.KeyID3;
				// }
				// }

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersRS(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RS) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				RS rs = (RS) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = rs.keyID1;

				if (!rs.keyID3.equals("")) {
					// reorg before key 3
					ColumnReorg = rs.keyID3;
				} else if (!rs.keyID2.equals("")) {
					// reorg before key id 2
					ColumnReorg = rs.keyID3;
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersD(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof D) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[REFERENCE DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				D d = (D) C.L;
				String ColumnReorg = d.KeyID1;
				if (!d.KeyID3.equals("")) {
					ColumnReorg = d.KeyID3;
				} else if (!d.KeyID2.equals("")) {
					ColumnReorg = d.KeyID2;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	//
	public static QuerySubject Memoize(String s) {
		// boolean isMemoized = false;

		for (QuerySubject N : lookup) {
			if (N != null && N.U_Name.equals(s)) {
				return N;
			}
		}
		QuerySubject N = new QuerySubject(s);
		N.Make();
		lookup.add(N);
		return N;

	}

	private static void WriteF_F(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {

		if (C.L.LinkType.equals("F") && C.L.isF2F == true) {
			String from, to;
			from = "[" + srcNameSpace + "].[" + C.L.LinkedFinale.QuerySubject + "]";
			to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
			RelationShip RS = new RelationShip(from, to);
			RS.setParentNamespace(prntNameSpace);

			F FKs = (F) C.L;
			String xp = "";
			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			if (FKs.isUseFieldName == true) {
				for (String CommunColumn : FKs.CommunColumns) {
					String de = from + ".[" + CommunColumn.toUpperCase() + "]";
					String a = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + de + "</refobj> = <refobj>" + a + "</refobj>";
					}
					if (FKs.CommunColumns.indexOf(CommunColumn) < FKs.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}
			} else {
				String sdc = FKs.SDCCOLUMNID.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID.toUpperCase() + "]";
				String sdc2 = FKs.SDCCOLUMNID2.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID2.toUpperCase() + "]";
				;
				String sdc3 = FKs.SDCCOLUMNID3.equals("") ? "" : from + ".[" + FKs.SDCCOLUMNID3.toUpperCase() + "]";
				;

				String keyid1 = FKs.KeyID.equals("") ? "" : to + ".[" + FKs.KeyID.toUpperCase() + "]";
				String keyid2 = FKs.KeyID2.equals("") ? "" : to + ".[" + FKs.KeyID2.toUpperCase() + "]";
				String keyid3 = FKs.KeyID3.equals("") ? "" : to + ".[" + FKs.KeyID3.toUpperCase() + "]";

				if (sdc.equals("")) {
					try {
						throw new Exception("FK but even SDCCOLUMNID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
				}
			}

			RS.setExpression(xp);
			RS.setCard_left_min("one");
			RS.setCard_left_max("many");

			RS.setCard_right_min("one");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);
		}

	}

	private static void WriteF_FS(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {
		String from, to;
		from = "[" + srcNameSpace + "].[" + C.L.LinkedFinale.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		lg("from = " + from);
		lg("to = " + to);
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("FS")) {

			FS FKs = (FS) C.L;

			// ArrayList<String> SDCCOLUMNIDs = new ArrayList<>();
			String sdc = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("sdcid").toUpperCase() + "]";
			String sdc1 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid1").toUpperCase() + "]";
			String sdc2 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid2").toUpperCase() + "]";
			String sdc3 = FKs.Prefix.equals("") ? "" : from + ".[" + FKs.Prefix.concat("keyid3").toUpperCase() + "]";

			String sdcid = FKs.SDCID.equals("") ? "" : to + ".[" + FKs.SDCID.toUpperCase() + "]";
			String keyid1 = FKs.keyID1.equals("") ? "" : to + ".[" + FKs.keyID1.toUpperCase() + "]";
			String keyid2 = FKs.keyID2.equals("") ? "" : to + ".[" + FKs.keyID2.toUpperCase() + "]";
			String keyid3 = FKs.keyID3.equals("") ? "" : to + ".[" + FKs.keyID3.toUpperCase() + "]";

			String xp = "";

			if (sdcid.equals("")) {
				try {
					throw new Exception("!!!!!!!!! LIEN FS SANS SDC !!!!!! ");
				} catch (Exception ex) {
					lg(ex.getMessage());
				}
			} else {
				xp = "<refobj>" + sdc + "</refobj> = '" + FKs.SDCID + "'";
			}

			if (!sdc1.equals("")) {
				xp = xp + "and <refobj>" + sdc1 + "</refobj> = <refobj>" + keyid1 + "</refobj>";
			}

			if (!sdc2.equals("")) {
				xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
			}
			if (!sdc3.equals("")) {
				xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
			}
			RS.setExpression(xp);
			RS.setCard_left_min("one");
			RS.setCard_left_max("many");

			RS.setCard_right_min("one");
			RS.setCard_right_max("one");

			FactorySVC.createRelationship(RS);
		}

	}

	private static void WriteF_DE(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {

		String from, to;
		from = "[" + srcNameSpace + "].[" + C.L.LinkedFinale.QuerySubject + "]";
		to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
		RelationShip RS = new RelationShip(from, to);
		RS.setParentNamespace(prntNameSpace);
		if (C.L.LinkType.equals("DE")) {
			DE de = (DE) C.L;
			String xp = "";

			if (C.L.isUseFieldName) {
				// throw new Exception("les relations sdi - sdi non encore prise
				// en charge");
				for (String CommunColumn : de.CommunColumns) {
					String dee = from + ".[" + CommunColumn.toUpperCase() + "]";
					String aa = to + ".[" + CommunColumn.toUpperCase() + "]";

					if (!CommunColumn.equals("")) {
						xp = xp + "<refobj>" + dee + "</refobj> = <refobj>" + aa + "</refobj>";
					}
					if (de.CommunColumns.indexOf(CommunColumn) < de.CommunColumns.size() - 1) {
						xp = xp + " and ";
					}

				}
			} else {
				String keyid1 = de.KeyID1.equals("") ? "" : to + ".[" + de.KeyID1.toUpperCase() + "]";
				String keyid2 = de.KeyID2.equals("") ? "" : to + ".[" + de.KeyID2.toUpperCase() + "]";
				;
				String keyid3 = de.KeyID3.equals("") ? "" : to + ".[" + de.KeyID3.toUpperCase() + "]";
				;

				String sdc = de.KeyID1.equals("") ? "" : from + ".[KEYID1]";
				String sdc2 = de.KeyID2.equals("") ? "" : from + ".[KEYID2]";
				String sdc3 = de.KeyID3.equals("") ? "" : from + ".[KEYID3]";
				String SDCID = de.SdcID.equals("") ? "" : from + ".[SDCID]";

				if (sdc.equals("")) {
					try {
						throw new Exception("M but  even KEY ID1 is empty/null !! ");
					} catch (Exception ex) {
						lg(ex.getMessage());
					}
				} else {
					xp = "<refobj>" + sdc + "</refobj> = <refobj>" + keyid1 + "</refobj>";
				}

				if (!sdc2.equals("")) {
					xp = xp + "and <refobj>" + sdc2 + "</refobj> = <refobj>" + keyid2 + "</refobj>";
				}
				if (!sdc3.equals("")) {
					xp = xp + "and <refobj>" + sdc3 + "</refobj> = <refobj>" + keyid3 + "</refobj>";
				}
				// sdcid
				// lg("XXXXX SDCID = " + SDCID);
				xp = xp + "and <refobj>" + SDCID + "</refobj> = '" + de.SdcID + "'";

			}

			RS.setExpression(xp);
			RS.setCard_left_min("one");
			RS.setCard_left_max("many");

			RS.setCard_right_min("one");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);

		}

	}

	private static void WriteF_C(QuerySubject C, String srcNameSpace, String dstNameSpace, String prntNameSpace) {

		if (C.L.LinkType.equals("C") && C.L.isF2F == true) {
			String from, to;
			from = "[" + srcNameSpace + "].[" + C.L.LinkedFinale.QuerySubject + "]";
			to = "[" + dstNameSpace + "].[" + C.QuerySubject + "]";
			RelationShip RS = new RelationShip(from, to);
			RS.setParentNamespace(prntNameSpace);

			C cc = (C) C.L;
			String xp = "";

			// xp = xp + "and <refobj>" + A + "</refobj> = <refobj>" + B +
			// "</refobj>";
			xp = cc.Relation;
			RS.setExpression(xp);
			RS.setCard_left_min("one");
			RS.setCard_left_max("many");

			RS.setCard_right_min("one");
			RS.setCard_right_max("one");
			FactorySVC.createRelationship(RS);
		}

	}

	public static void MakeMainFolder(QSFolder F) {
		if (F.QSParent == null) {
			FactorySVC.createFolder("[DYNAMIC DATA]", F.Name);
		} else {
			// FactorySVC.createFolderInFolder(String NameSpace, String
			// Fin,String Fout);
			FactorySVC.createFolderInFolder("[DYNAMIC DATA]", F.QSParent.Name, F.Name);

		}

		for (QSFolder C : F.QSChildren) {
			MakeMainFolder(C);
		}

	}

	private static void MakeSubFoldersFXX(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof F) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				F f = (F) C.L;
				String ColumnReorg = f.SDCCOLUMNID;
				if (!f.SDCCOLUMNID3.equals("")) {
					ColumnReorg = f.SDCCOLUMNID3;
				} else if (!f.SDCCOLUMNID2.equals("")) {
					ColumnReorg = f.SDCCOLUMNID2;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	private static void MakeSubFoldersRFXX(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RF) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				RF rf = (RF) C.L;
				String ColumnReorg = rf.KeyID;
				if (!rf.KeyID2.equals("")) {
					ColumnReorg = rf.KeyID2;
				} else if (!rf.KeyID3.equals("")) {
					ColumnReorg = rf.KeyID3;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	private static void MakeSubFoldersVXX(QuerySubject N) {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		for (QuerySubject C : N.Children) {

			if (C.L instanceof V) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// si notre dossier est dans la 1ere couche ou non, cad
				// directement dans le QS ou bien imbriqué
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// création des QI de la refvalue
				// commençons par l'exemple d'une refvalue desc*
				// j'ai laissé toUpperCase() pour utiliser des variable si on
				// veut rapatrier plusieurs chalps
				// ^ keep in mind ^\\
				String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + "REFVALUEDESC".toUpperCase() + "]";
				String Name = C.QueryItemPrefix + "REFVALUEDESC".toUpperCase();
				FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);

				// ! \\ reorganiser les dossiers
				V v = (V) C.L;
				String ColumnReorg = v.SDCCOLUMNID;
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}
		}
	}

	private static void MakeSubFoldersM2MXX(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof M) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// si le lien est de type M1, on crée le dossier
				if (C.L.LinkType.equals("M1")) {
					// si notre dossier est dans la 1ere couche ou non, cad
					// directement dans le QS ou bien imbriqué
					if (N.Parent == null) {
						FactorySVC.createSubFolder(QSrefobj, C.Folder);
					} else {
						FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
					}

					String Name = C.QueryItemPrefix + "usersequence".toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + "usersequence".toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);

					// reorg après la dernier PK
					M m = (M) C.L;
					String ColumnReorg = m.KeyID1;
					if (!m.KeyID3.equals("")) {
						// reorg before key 3
						ColumnReorg = m.KeyID3;
					} else if (!m.KeyID2.equals("")) {
						// reorg before key id 2
						ColumnReorg = m.KeyID3;
					}

					// ! \\ reorganiser les dossiers
					String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
					// String h, t;
					String hf, tf;
					if (ReorderBefore.equals("")) {
						tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
						hf = QSrefobj + ".[" + C.Folder + "]";
						// lg("tf = " + tf);
						// lg("hf = " + hf);
						FactorySVC.ReorderSubFolderBefore(hf, tf);
					}

					// ! \\ FIN reorganiser les dossiers
				}

				// si le lien est de type M2, on crée les QIs de M2
				if (C.L.LinkType.equals("M2")) {
					// ajouter les elements de requêtes

					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);

					ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
					ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
					for (String c : Columns) {
						// String FolderHandle = QSrefobj + ".[" + C.Folder +
						// "]";

						String Name = C.QueryItemPrefix + c.toUpperCase();
						String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
						FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
					}

					// ajouter les champs traduits _T
					for (String c : TranslatedColumns) {
						// String FolderHandle = QSrefobj + ".[" + C.Folder +
						// "]";
						String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
						String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
						FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
					}
					// organiser les colonnes _T au dessous de leurs champs
					for (String c : TranslatedColumns) {
						// order _T
						String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
						if (!ReorgBefore.equals("")) {
							String hx, tx;
							String CurrentFolderx = QSrefobj;
							hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
							tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
							FactorySVC.ReorderBefore(hx, tx);
						}
					}

				}

			}
		}
	}

	private static void MakeSubFoldersSXX(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof S) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				S s = (S) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = s.KeyID1;
				// void si le parent est une sdi
				String ParentPrefix = s.TableID.substring(0, 3).toLowerCase();
				Boolean isSDI = ParentPrefix.equals("sdi");

				if (isSDI) {
					ColumnReorg = "keyid3";
				} else {
					if (!s.KeyID3.equals("")) {
						// reorg before key 3
						ColumnReorg = s.KeyID3;
					} else if (!s.KeyID2.equals("")) {
						// reorg before key id 2
						ColumnReorg = s.KeyID3;
					}
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersRIXX(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RI) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				RI ri = (RI) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = ri.KeyID1;
				// void si le parent est une sdi
				// String ParentPrefix = s.TableID.substring(0,
				// 3).toLowerCase();
				// Boolean isSDI = ParentPrefix.equals("sdi");

				if (!ri.KeyID3.equals("")) {
					// reorg before key 3
					ColumnReorg = ri.KeyID3;
				} else if (!ri.KeyID2.equals("")) {
					// reorg before key id 2
					ColumnReorg = ri.KeyID3;
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";

					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersIXX(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof I) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				I ii = (I) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg;

				ColumnReorg = "keyid3";
				// if (isSDI) {
				// ColumnReorg = "keyid3";
				// } else {
				// if (!s.KeyID3.equals("")) {
				// //reorg before key 3
				// ColumnReorg = s.KeyID3;
				// } else if (!s.KeyID2.equals("")) {
				// //reorg before key id 2
				// ColumnReorg = s.KeyID3;
				// }
				// }

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersFSXX(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof FS) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				FS fs = (FS) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg;

				ColumnReorg = fs.Prefix.concat("sdcid");
				// if (isSDI) {
				// ColumnReorg = "keyid3";
				// } else {
				// if (!s.KeyID3.equals("")) {
				// //reorg before key 3
				// ColumnReorg = s.KeyID3;
				// } else if (!s.KeyID2.equals("")) {
				// //reorg before key id 2
				// ColumnReorg = s.KeyID3;
				// }
				// }

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersRSXX(QuerySubject N) throws Exception {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof RS) {
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";
				Boolean FirstChild = true;

				if (N.Parent != null) {
					FirstChild = false;
				}
				RS rs = (RS) C.L;

				if (FirstChild) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);

				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}

				// ! \\ reorganiser les dossiers
				String ColumnReorg = rs.keyID1;

				if (!rs.keyID3.equals("")) {
					// reorg before key 3
					ColumnReorg = rs.keyID3;
				} else if (!rs.keyID2.equals("")) {
					// reorg before key id 2
					ColumnReorg = rs.keyID3;
				}

				// String ColumnReorg = s.;
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}
	}

	private static void MakeSubFoldersDXX(QuerySubject N) {
		for (QuerySubject C : N.Children) {

			if (C.L instanceof D) {
				// chercher la racine
				QuerySubject RACINE = C;
				while (RACINE.Parent != null) {
					RACINE = RACINE.Parent;
				}
				// le sujet de requête dans lequel nous allons ajouter des
				// dossiers
				String QSrefobj = "[DYNAMIC DATA]" + ".[" + RACINE.QuerySubject + "]";
				// Colonnes pour pouvoir créer les elements de requêtes
				// lg(C.U_Name);
				ArrayList<String> Columns = JDBCUtil.Columns(C.U_Name);
				ArrayList<String> TranslatedColumns = JDBCUtil.getTranslantedColumns(C.U_Name, "RefSDC");
				// le namespace duquel nous allons chercher l'element de
				// requêtes
				String Namespace = C.Parent == null ? "TABLES_UTIL" : "RefSDC";

				// créer le premier dossier dans un sujet de requête est
				// different
				// en syntaxe
				if (N.Parent == null) {
					FactorySVC.createSubFolder(QSrefobj, C.Folder);
				} else {
					FactorySVC.createSubFolderInSubFolder(QSrefobj, N.Folder, C.Folder);
				}

				// ajouter les elements de requêtes
				for (String c : Columns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";

					String Name = C.QueryItemPrefix + c.toUpperCase();
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// ajouter les champs traduits _T
				for (String c : TranslatedColumns) {
					// String FolderHandle = QSrefobj + ".[" + C.Folder + "]";
					String Name = C.QueryItemPrefix + c.toUpperCase() + "_T";
					String Exp = "[" + Namespace + "].[" + C.QuerySubject + "].[" + c.toUpperCase() + "_T" + "]";
					FactorySVC.createQueryItemInFolder(QSrefobj, C.Folder, Name, Exp);
				}
				// organiser les colonnes _T au dessous de leurs champs
				for (String c : TranslatedColumns) {
					// order _T
					String ReorgBefore = JDBCUtil.ReorderBefore(C.U_Name, c);
					if (!ReorgBefore.equals("")) {
						String hx, tx;
						String CurrentFolderx = QSrefobj;
						hx = CurrentFolderx + ".[" + C.QueryItemPrefix + c.toUpperCase() + "_T" + "]";
						tx = CurrentFolderx + ".[" + C.QueryItemPrefix + ReorgBefore.toUpperCase() + "]";
						FactorySVC.ReorderBefore(hx, tx);
					}
				}

				// ! \\ reorganiser les dossiers
				D d = (D) C.L;
				String ColumnReorg = d.KeyID1;
				if (!d.KeyID3.equals("")) {
					ColumnReorg = d.KeyID3;
				} else if (!d.KeyID2.equals("")) {
					ColumnReorg = d.KeyID2;
				}
				// lg("+++++++++++++++++++ " + ColumnReorg);
				String ReorderBefore = JDBCUtil.ReorderBefore(N.U_Name, ColumnReorg);
				// String h, t;
				String hf, tf;
				// only reorder if you are not the last item
				if (!ReorderBefore.equals("")) {
					tf = QSrefobj + ".[" + N.QueryItemPrefix + ReorderBefore.toUpperCase() + "]";
					hf = QSrefobj + ".[" + C.Folder + "]";
					// lg("tf = " + tf);
					// lg("hf = " + hf);
					FactorySVC.ReorderSubFolderBefore(hf, tf);
				}

				// ! \\ FIN reorganiser les dossiers
			}

		}

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
