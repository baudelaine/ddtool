package datadixit.limsbi.action;

import java.io.File;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import datadixit.limsbi.properties.ConfigProperties;
import datadixit.limsbi.svc.FactorySVC;


public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			String namespace = "[DIMENSIONS]";
						
			File xmlFile = new File("/root/ddtool/WebContent/res/templates/deleteNamespace.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			Element handle = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171117155621486\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");

			handle.setText(namespace);
	
			System.out.println(document.asXML());
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
