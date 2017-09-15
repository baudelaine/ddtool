package datadixit.limsbi.svc;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;

import datadixit.limsbi.cognos.CRNConnect;
import datadixit.limsbi.properties.ConfigProperties;
import sapphire.util.Logger;

public class ProjectSVC {

	private static CRNConnect crnConnect;

	static {

		crnConnect = CognosSVC.crnConnect;
	}

	public static void openModel() {

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/openModel.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			Node node = document.selectSingleNode("//@model");
			node.setText(ConfigProperties.model);
			XmlEncodedXML xex = new XmlEncodedXML(node.getParent().asXML());
			String res = crnConnect.getMetadataService().updateMetadata(xex).toString();
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		} catch (RemoteException ex) {
			lg(ex.getMessage());
		}

	}

	public static void saveModel() {

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/saveModel.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node node = document.selectSingleNode("//@model");
			node.setText(ConfigProperties.model);
			XmlEncodedXML xex = new XmlEncodedXML(node.getParent().asXML());
			String res = crnConnect.getMetadataService().updateMetadata(xex).toString();
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		} catch (RemoteException ex) {
			lg(ex.getMessage());
		}

	}

	public static void closeModel() {

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/closeModel.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node node = document.selectSingleNode("//@model");
			node.setText(ConfigProperties.model);
			XmlEncodedXML xex = new XmlEncodedXML(node.getParent().asXML());
			String res = crnConnect.getMetadataService().updateMetadata(xex).toString();
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		} catch (RemoteException ex) {
			lg(ex.getMessage());
		}

	}

	public static void createModel() {

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createModel.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node node = document.selectSingleNode("//@model");
			node.setText(ConfigProperties.model);
			XmlEncodedXML xex = new XmlEncodedXML(node.getParent().asXML());
			String res = crnConnect.getMetadataService().updateMetadata(xex).toString();
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		} catch (RemoteException ex) {
			lg(ex.getMessage());
		}

	}

	public static void addLocale(String locale) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/addLocale.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node newLocale = document.selectSingleNode("//param[@seq='1']/value");
			Node activeLocale = document.selectSingleNode("//param[@seq='2']/value");

			newLocale.setText("<stringCollection><item>" + locale + "</item></stringCollection>");
			activeLocale.setText("en-gb");

			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}

	public static void removeLocale(String locale) {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/removeLocale.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node nodeLocale = document.selectSingleNode("//param[@seq='1']/value");

			nodeLocale.setText(locale);

			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}

	public static void setLocale() {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/setLocale.xml");

			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Element root = document.getRootElement();

			Node n1 = document.selectSingleNode("//action[@type='SetActiveLocale']/inputparams/param/value");
			Node n2 = document.selectSingleNode("//action[@type='SetDefaultLocale']/inputparams/param/value");

			n1.setText(ConfigProperties.ActiveLocale);
			n2.setText(ConfigProperties.DefaultLocale);

			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}

	public static void publish() {

		lg(" publish START ");
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/publish-old.xml");

			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node n1 = document.selectSingleNode("//param[@seq='3']/value");
			Node n2 = document.selectSingleNode("//param[@seq='4']/value");
			Node n3 = document.selectSingleNode("//param[@seq='1']/value");
			Element root = document.getRootElement();
			Date date = new Date();

			// create a folder in the public folders
			String folder = ConfigProperties.PublishFolder + date.toString();
			FactorySVC.createPublicFolder(folder);

			n1.setText("/content/folder[@name='" + folder + "']");
			n2.setText("[].[packages].[" + ConfigProperties.PackageName + "]");
			n3.setText("[].[packages].[" + ConfigProperties.PackageName + "]");

			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}
		lg(" publish END ");
	}

	public static void publishpackage() {

		lg(" publishpackage START ");
		// try {
		// File xmlFile = new File(ConfigProperties.PathToXML +
		// "/publish.xml");
		//
		// SAXReader reader = new SAXReader();
		// Document document = reader.read(xmlFile);
		// Node n1 = document.selectSingleNode("//param[@seq='3']/value");
		// Node n2 = document.selectSingleNode("//param[@seq='4']/value");
		// Node n3 = document.selectSingleNode("//param[@seq='1']/value");
		// Element root = document.getRootElement();

		Date date = new Date();

		// create a folder in the public folders
		String folder = ConfigProperties.PublishFolder + date.toString();
		FactorySVC.createPublicFolder(folder);

		// n1.setText("/content/folder[@name='" + folder + "']");
		// n2.setText("[].[packages].[" + ConfigProperties.PackageName +
		// "]");
		// n3.setText("[].[packages].[" + ConfigProperties.PackageName +
		// "]");

		// CognosSVC.executeModel(document);
		// } catch (DocumentException ex) {
		// lg(ex.getMessage());
		// }
		lg(" publishpackage END ");
	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
