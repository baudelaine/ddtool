package datadixit.limsbi.svc;

import java.io.File;
import java.util.logging.Level;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cognos.developer.schemas.bibus._3.AddOptions;
import com.cognos.developer.schemas.bibus._3.BaseClass;
import com.cognos.developer.schemas.bibus._3.Folder;
import com.cognos.developer.schemas.bibus._3.PropEnum;
import com.cognos.developer.schemas.bibus._3.QueryOptions;
import com.cognos.developer.schemas.bibus._3.SearchPathMultipleObject;
import com.cognos.developer.schemas.bibus._3.SearchPathSingleObject;
import com.cognos.developer.schemas.bibus._3.Sort;
import com.cognos.developer.schemas.bibus._3.TokenProp;
import com.cognos.developer.schemas.bibus._3.UpdateActionEnum;

import datadixit.limsbi.cognos.CRNConnect;
import datadixit.limsbi.pojos.RelationShip;
import datadixit.limsbi.properties.ConfigProperties;
import sapphire.util.Logger;

public class FactorySVC {

	private static CRNConnect crnConnect;

	static {
		crnConnect = CognosSVC.crnConnect;
	}

	public static void ImportDB(String Namespace) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/DBImport.xml");

			SAXReader reader = new SAXReader();
			Document script = reader.read(xmlFile);

			Element root = script.getRootElement();
			Node namespace = script.selectSingleNode("//param[@seq='1']/value");
			Node cdata = script.selectSingleNode("//param[@seq='2']/value");

			namespace.setText("[" + Namespace + "]");
			CognosSVC.executeModel(script);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createFolder(String NamespaceRefObj, String Name) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node n1 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727115905548\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			n1.setText(NamespaceRefObj);

			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727115905548\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[3]/value");
			n2.setText(Name);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createFolderInFolder(String Namespace, String Fin, String Fout) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createFolderInFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node n1 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150916123602448\"]/transaction[@saved=\"false\"]/action[1]/inputparams/param[2]/value");
			n1.setText(Namespace + ".[" + Fin + "]");

			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150916123602448\"]/transaction[@saved=\"false\"]/action[1]/inputparams/param[3]/value");
			n2.setText(Fout);

			Node n3 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150916123602448\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[@seq=\"1\"]/value");
			n3.setText(Namespace + ".[" + Fout + "]");

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createNamespace(String NameSpace, String Parent) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createNamespace.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node parent = document.selectSingleNode("//param[@seq='2']/value");
			Node namespace = document.selectSingleNode("//param[@seq='3']/value");

			parent.setText("[" + Parent + "]");
			namespace.setText(NameSpace);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createQuerySubject(String Source, String Destination, String Name) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQuerySubject.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node src = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[1]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node dst = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node mov = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[2]/value");
			Node mod_path = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node mod_val = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[2]/value");

			src.setText("[" + Source + "].[" + Name + "]");
			dst.setText("[" + Destination + "]");
			mov.setText("[" + Source + "].[" + Name + "_" + Name + "]");
			mod_path.setText("/O/name[0]/O/[" + Destination + "].[" + Name + "_" + Name + "]");
			mod_val.setText(Name);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createQuerySubjectInFolder(String Source, String Destination, String Folder, String Name) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQuerySubject.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			///////////////////////////////////////////////////

			//////////////////////////////////////////////////
			Node src = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[1]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node dst = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node dstType = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[1]/mappingpath");
			Node mov = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[2]/value");
			Node mod_path = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node mod_val = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[2]/value");

			src.setText("[" + Source + "].[" + Name + "]");
			dst.setText("[" + Destination + "].[" + Folder + "]");
			dstType.setText("folder");
			mov.setText("[" + Source + "].[" + Name + "_" + Name + "]");
			mod_path.setText("/O/name[0]/O/[" + Destination + "].[" + Name + "_" + Name + "]");
			mod_val.setText(Name);

			System.out.println("+-+-+-+-+-+-+- CREATE QS " + Source + ", " + Destination + ", " + Folder + ", " + Name);
			
			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	// ceci pour les QS qui pointe directement sur la DB.
	// il ont besoin du vrai nom de la table et aussi du u_name
	public static void createQuerySubject(String Source, String Destination, String BasedOnQuerySubject, String NameQuerySubject) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQuerySubject.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node src = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[1]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node dst = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node mov = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[2]/action[@seq=\"1\"]/inputparams/param[2]/value");
			Node mod_path = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node mod_val = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150722173540031\"]/transaction[3]/action[@seq=\"1\"]/inputparams/param[2]/value");

			src.setText("[" + Source + "].[" + BasedOnQuerySubject + "]");
			dst.setText("[" + Destination + "]");

			mov.setText("[" + Source + "].[" + BasedOnQuerySubject + "_" + BasedOnQuerySubject + "]");
			mod_path.setText("/O/name[0]/O/[" + Destination + "].[" + BasedOnQuerySubject + "_" + BasedOnQuerySubject + "]");
			mod_val.setText(NameQuerySubject);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createQueryItem(String QuerySubject, String Name, String Exp) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQueryItem.xml");
			File cdataFile = new File(ConfigProperties.PathToXML + "/createQueryItemCDATA.xml");
			SAXReader reader = new SAXReader();

			Document document = reader.read(xmlFile);
			Document cdata_document = reader.read(cdataFile);

			// xml
			Element qs = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154520606\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Element cdata = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154520606\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			// cdata
			Element xp = (Element) cdata_document.selectSingleNode("/updateObjectRequest/tasks/task[@name=\"addObject\"]/parameters/param[4]/expression");
			Element nm = (Element) cdata_document.selectSingleNode("/updateObjectRequest/tasks/task[@name=\"addObject\"]/parameters//param[1]");
			Element root_cdata = cdata_document.getRootElement();

			qs.setText(QuerySubject);

			nm.addAttribute("value", Name);
			xp.setText(Exp);

			// attach cdata
			cdata.setText("");
			cdata.addCDATA(root_cdata.asXML());

			// System.out.println(document.asXML());
			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createQueryItemInFolder(String QS, String Folder, String Name, String Exp) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQueryItemInFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			// xml
			Element folder = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[1]/inputparams/param[2]/value");
			Element nm = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[2]/value");
			Element xp = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[3]/inputparams/param[2]/value");

			Element n1 = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[1]/value");
			Element n2 = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[3]/inputparams/param[1]/value");

			// cdata
			n1.setText("/O/name[0]/O/" + QS + ".[New Query Item]");
			n2.setText("/O/expression[0]/O/" + QS + ".[" + Name + "]");
			folder.setText(QS + ".[" + Folder + "]");
			nm.setText(Name);
			xp.setText(Exp);

			// System.out.println(document.asXML());
			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createRelationship(RelationShip rs) {

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createRelationship.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Element root = document.getRootElement();
			Element query_left = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[2]/value");
			Element query_right = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[5]/inputparams/param[2]/value");
			Element left_min = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[3]/inputparams/param[2]/value");
			Element left_max = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[4]/inputparams/param[2]/value");

			Element refobj_right_min = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[6]/inputparams/param[1]/value");
			Element refobj_right_max = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[7]/inputparams/param[1]/value");
			Element refobj_nm = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[8]/inputparams/param[1]/value");
			Element refobj_xp = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[9]/inputparams/param[1]/value");
			Element refobj_cr = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[10]/inputparams/param[1]/value");

			Element right_min = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[6]/inputparams/param[2]/value");
			Element right_max = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[7]/inputparams/param[2]/value");
			Element exp = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[9]/inputparams/param[2]/value");
			Element nm = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723131838883\"]/transaction[@saved=\"false\"]/action[8]/inputparams/param[2]/value");

			query_left.setText(rs.getQuerySubject_left());
			query_right.setText(rs.getQuerySubject_right());
			left_min.setText(rs.getCard_left_min());
			right_min.setText(rs.getCard_right_min());
			left_max.setText(rs.getCard_left_max());
			right_max.setText(rs.getCard_right_max());

			refobj_right_min.setText("/O/right[0]/mincard[0]/O/[" + rs.getParentNamespace() + "].[New Relationship]");
			refobj_right_max.setText("/O/right[0]/maxcard[0]/O/[" + rs.getParentNamespace() + "].[New Relationship]");
			refobj_nm.setText("/O/name[0]/O/[" + rs.getParentNamespace() + "].[New Relationship]");

			refobj_xp.setText("/O/expression[0]/O/[" + rs.getParentNamespace() + "].[" + rs.getName() + "]");
			refobj_cr.setText("[" + rs.getParentNamespace() + "].[" + rs.getName() + "]");
			nm.setText(rs.getName());
			exp.setText(rs.getExpression());
			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}

	public static void createPublicFolder(String newFolderName) {
		PropEnum[] properties = new PropEnum[] { PropEnum.searchPath, PropEnum.defaultName };

		AddOptions add = new AddOptions();
		add.setUpdateAction(UpdateActionEnum.replace);

		// create new folder object
		Folder aFolder = new Folder();

		TokenProp tp = new TokenProp();
		tp.setValue(newFolderName);

		aFolder.setDefaultName(tp);

		SearchPathMultipleObject searchPath = new SearchPathMultipleObject();
		searchPath.set_value("/content/folder[@name='" + newFolderName + "']");

		try {
			BaseClass[] folder = crnConnect.getCMService().query(searchPath, properties, new Sort[] {}, new QueryOptions());
			// Check if the folder is already exist if not create it.
			if (folder.length == 0) {
				SearchPathSingleObject searchPathSing = new SearchPathSingleObject();
				searchPathSing.set_value("/content");
				crnConnect.getCMService().add(searchPathSing, new BaseClass[] { aFolder }, add);

				System.out.println("New folder [" + newFolderName + "] has been created.");
			} else {
				System.out.println(newFolderName + " already exists.");
			}
		} catch (Exception e) {
			System.out.println("Exception ");
		}
	}

	public static void ReorderSubFolderBefore(String handle, String target) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/ReorderBefore.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node tp = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154806075\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/mappingpath");
			Node h = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154806075\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node t = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154806075\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");

			h.setText(handle);
			t.setText(target);
			tp.setText("queryItemFolder");

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void ReorderBefore(String handle, String target) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/ReorderBefore.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node h = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154806075\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Node t = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150723154806075\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");

			h.setText(handle);
			t.setText(target);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createSubFolder(String QSRejObj, String Name) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createSubFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			Node n1 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727152555245\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			n1.setText(QSRejObj);

			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727152555245\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[3]/value");
			n2.setText(Name);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createSubFolderInSubFolder(String QSRefObj, String Fext, String Fint) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createSubFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			Node n0 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727152555245\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			n0.setText(QSRefObj + ".[" + Fext + "]");

			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150727152555245\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[3]/value");
			n2.setText(Fint);

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void createFilter(String Name, String Expression, String Namespace) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createFilter.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node NamespaceNode = document.selectSingleNode("/bmtactionlog/transaction[1]/action[1]/inputparams[1]/param[2]/value[1]");
			Node NameNode = document.selectSingleNode("/bmtactionlog/transaction[1]/action[1]/inputparams[1]/param[3]/value[1]");
			Node ExpressionNode = document.selectSingleNode("/bmtactionlog/transaction[1]/action[3]/inputparams[1]/param[2]/value[1]");
			Node ExpressionSettingNode = document.selectSingleNode("/bmtactionlog/transaction[1]/action[3]/inputparams[1]/param[1]/value[1]");

			NamespaceNode.setText("[" + Namespace + "]");
			NameNode.setText(Name);
			ExpressionNode.setText(Expression);
			ExpressionSettingNode.setText("/O/expression[0]/O/[" + Namespace + "].[" + Name + "]");

			CognosSVC.executeModel(document);

		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void assignFilter(String Filter, String QuerySubject) {
		try {

			File xmlFile = new File(ConfigProperties.PathToXML + "/assignFilter.xml");
			File cdataFile = new File(ConfigProperties.PathToXML + "/assignFilterCDATA.xml");
			SAXReader reader = new SAXReader();

			Document document = reader.read(xmlFile);
			Document cdata_document = reader.read(cdataFile);

			// xml
			Element qs = (Element) document.selectSingleNode("/bmtactionlog/transaction[1]/action[1]/inputparams[1]/param[1]/value[1]");
			Element cdata = (Element) document.selectSingleNode("/bmtactionlog/transaction[1]/action[1]/inputparams[1]/param[2]/value[1]");

			// cdata
			Element eFilter = (Element) cdata_document.selectSingleNode("/updateObjectRequest/tasks[1]/task[1]/parameters[1]/param[2]/refobj[1]");

			Element root_cdata = cdata_document.getRootElement();

			qs.setText(QuerySubject);
			eFilter.setText(Filter);

			// attach cdata
			cdata.setText("");
			cdata.addCDATA(root_cdata.asXML());

			// System.out.println(document.asXML());
			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
