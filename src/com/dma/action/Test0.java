package com.dma.action;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.dma.properties.ConfigProperties;


public class Test0 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			String modelName = "bidule";
			
			File xmlFile = new File("/opt/wks/ddtool/WebContent/res/templates/createModel.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			
			Node node = document.selectSingleNode("//@project");
			node.setText("C:\\LimsBI\\models\\" + modelName + "\\" + modelName + ".cpf");
			
			Node n1 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20171003184009897\"]/transaction[@uniqueId=\"78F4A23E78F4A23E\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			n1.setText(modelName);

//			Node n2 = document.selectSingleNode("/bmtactionlog[@timestamp=\"20150410184900010\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
//			n2.setText(sourceQS);
			
			System.out.println(document.asXML());
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
