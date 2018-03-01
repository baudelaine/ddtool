package com.dma.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cognos.developer.schemas.bibus._3.XmlEncodedXML;
import com.dma.cognos.CRNConnect;
import com.dma.properties.ConfigProperties;
import com.dma.svc.CognosSVC;
import com.dma.svc.FactorySVC;


public class Test2 {

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		


		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/createQueryItemInFolder.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Element root = document.getRootElement();

			String QS = "QS"; 
			String Folder = "Folder";
			String Name = "Name";
			String Exp = "Exp";
			// xml
			Element transaction = (Element) document.selectSingleNode("/bmtactionlog/transaction");
			
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

//			System.out.println(transaction.asXML());
			System.out.println("createQueryItemInFolder(" + QS + ", " + Folder + ", " + Exp + ")");
			
			Document document1 = reader.read(xmlFile);
			root.addElement("transaction");
			
			Element transaction2 = (Element) document.selectSingleNode("/bmtactionlog/transaction[2]");
						
			String QS1 = "QS1"; 
			String Folder1 = "Folder1";
			String Name1 = "Name1";
			String Exp1 = "Exp1";
			// xml
			Element folder1 = (Element) document1.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[1]/inputparams/param[2]/value");
			Element nm1 = (Element) document1.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[2]/value");
			Element xp1 = (Element) document1.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[3]/inputparams/param[2]/value");

			Element n11 = (Element) document1.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[2]/inputparams/param[1]/value");
			Element n21 = (Element) document1.selectSingleNode("/bmtactionlog[@timestamp=\"20150729120206717\"]/transaction[@saved=\"false\"]/action[3]/inputparams/param[1]/value");

			// cdata
			n11.setText("/O/name[0]/O/" + QS1 + ".[New Query Item]");
			n21.setText("/O/expression[0]/O/" + QS1 + ".[" + Name1 + "]");
			folder1.setText(QS1 + ".[" + Folder1 + "]");
			nm1.setText(Name1);
			xp1.setText(Exp1);

			transaction2.appendContent(transaction);
//			System.out.println(transaction2.asXML());
			
			System.out.println(document.asXML());
			
			

			//CognosSVC.executeModel(document);
		} catch (DocumentException ex) {
			System.out.println(ex.getMessage());
		}

}
}

