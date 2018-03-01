package com.dma.svc;

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
import com.dma.cognos.CRNConnect;
import com.dma.properties.ConfigProperties;

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
			System.out.println("openModel" + ConfigProperties.model + "successful");
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
			System.out.println("saveModel " + ConfigProperties.model);
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
			System.out.println("closeModel " + ConfigProperties.model);
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
			System.out.println("addLocale(" + locale + ")");
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
			
			System.out.println("setLocale " + "ActiveLocale=" + ConfigProperties.ActiveLocale + " DefaultLocale=" + ConfigProperties.DefaultLocale);
			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}
	
	public static void changePropertyFixIDDefaultLocale() {
		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/changePropertyFixIDDefaultLocale.xml");

			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			lg(ex.getMessage());
		}

	}

	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
