package datadixit.limsbi.pojos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;

import datadixit.limsbi.db.JDBCUtil;
import sapphire.util.Logger;

@JsonIgnoreProperties({ "QSParent", "ROOTS" })
public class QSFolder {

	public String Name;
	public QSFolder QSParent;
	public ArrayList<QSFolder> QSChildren;

	public static ArrayList<QSFolder> ROOTS;

	static {
		ArrayList<String> L = JDBCUtil.FOLDERS();
		ROOTS = new ArrayList<QSFolder>();

		for (String s : L) {
			if (!s.equals("")) {
				QSFolder QSF = new QSFolder();
				QSF.Name = s;
				QSF.Make();
				ROOTS.add(QSF);
				QSF.PrettyPrint();
			}

		}

	}

	public QSFolder() {
		QSParent = null;
		QSChildren = new ArrayList<QSFolder>();

	}

	public void Make() {
		// TODO Auto-generated method stub
		ArrayList<String> Header = JDBCUtil.getTableHeader("u_limsbifolders");
		ArrayList<ArrayList<String>> FKs = JDBCUtil.getFolders(Name);

		for (ArrayList<String> ROW : FKs) {
			QSFolder C = new QSFolder();
			// U_LIMSBIFOLDERSID
			C.Name = ROW.get(Header.indexOf("u_limsbifoldersid"));
			C.QSParent = this;

			this.QSChildren.add(C);
		}
		for (QSFolder F : QSChildren) {
			F.Make();
		}
	}

	public void PrettyPrint() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			lg(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));

		} catch (IOException ex) {
			lg(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
