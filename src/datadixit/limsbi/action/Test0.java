package datadixit.limsbi.action;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import datadixit.limsbi.properties.ConfigProperties;
import datadixit.limsbi.svc.CognosSVC;


public class Test0 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			File xmlFile = new File(ConfigProperties.PathToXML + "/renameQuerySubject.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			Node n1 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150323183114850\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");
			n1.setText("[PHYSICALUSED].[SYSUSER]");

			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150323183114850\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			n2.setText("SYSUSER1");

			System.out.println(document.asXML());
			

		} catch (DocumentException ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
