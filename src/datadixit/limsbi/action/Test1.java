package datadixit.limsbi.action;

import java.io.File;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import datadixit.limsbi.properties.ConfigProperties;
import datadixit.limsbi.svc.FactorySVC;


public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			String dimensionMeasurePath = "[DIMENSIONS].[S_SAMPLE_MEASURES]";
			String measurePath = "[DIMENSIONS].[S_SAMPLE_MEASURES].[S_SAMPLE_COUNT]";
			String dimensionPath = "[DIMENSIONS].[S_PRODUCT]";
			String levelPath = "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]";

			File xmlFile = new File("/root/ddtool/WebContent/res/templates/adjustScopeRelationship.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);

			Element handleDMP = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171116145857971\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[1]/value");
			Element handleMP = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171116145857971\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[3]/value");
			Element handleDP = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171116145857971\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			Element handleLP = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171116145857971\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[4]/value");
			
			handleDMP.setText(dimensionMeasurePath);
			handleMP.setText(measurePath);
			handleDP.setText(dimensionPath);
			handleLP.setText(levelPath);
			
			/*
			Element cdata = (Element) document.selectSingleNode("/bmtactionlog[@timestamp=\"20171127165154093\"]/transaction[@saved=\"false\"]/action[@seq=\"1\"]/inputparams/param[2]/value");
			String s = cdata.getText();
			handle.setText(levelPath);

			Document doc = reader.read(new StringReader(s));
			Element qin = (Element) doc.selectSingleNode("/updateObjectRequest/tasks/task[@name=\"addObject\"]/parameters//param[1]");
			Element xp = (Element) doc.selectSingleNode("/updateObjectRequest/tasks/task[@name=\"addObject\"]/parameters/param[4]/expression");		
			Element root_cdata = doc.getRootElement();
			qin.addAttribute("value", queryItemName);
			xp.setText(exp);
			// attach cdata
		    cdata.setText("");
		    cdata.addCDATA(root_cdata.asXML());
			*/
			System.out.println(document.asXML());
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
        
		
	}

}
