package com.dma.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import com.dma.svc.FactorySVC;
import com.dma.svc.ProjectSVC;
import com.dma.svc.TaskerSVC;
import com.dma.web.Field;
import com.dma.web.QuerySubject;
import com.dma.web.Relation;
import com.dma.web.RelationShip;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTravail {

	static Map<String, Integer> gRefMap;
	static List<RelationShip> rsList;
	static Map<String, QuerySubject> query_subjects;
	static Map<String, String> labelMap;
	static Map<String, String> toolTipMap;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String json = null;
		String model = "/root/ddtool/WebContent/models/model-label-test.json";
		Path path = Paths.get(model);
		Charset charset = StandardCharsets.UTF_8;
		List<QuerySubject> qsList = null;
		Map<String, Integer> recurseCount = null;

		if(Files.exists(path)){
			try {
				json = new String(Files.readAllBytes(path), charset);
				System.out.println("json =" + json);

		        ObjectMapper mapper = new ObjectMapper();
		        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		        
//		        QuerySubject[] qsArray = mapper.readValue(br, QuerySubject[].class);
		        qsList = Arrays.asList(mapper.readValue(json, QuerySubject[].class));
		        
		        query_subjects = new HashMap<String, QuerySubject>();
		        recurseCount = new HashMap<String, Integer>();
		        
		        for(QuerySubject qs: qsList){
		        	query_subjects.put(qs.get_id(), qs);
		        	recurseCount.put(qs.getTable_alias(), 0);
		        }
		       
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// On est bien Tintin...
		
        TaskerSVC.start();
		TaskerSVC.IICInitNameSpace();
		TaskerSVC.Import();
		
		gRefMap = new HashMap<String, Integer>();
		
		rsList = new ArrayList<RelationShip>();

		labelMap = new HashMap<String, String>();
		toolTipMap = new HashMap<String, String>();
		
		for(Entry<String, QuerySubject> query_subject: query_subjects.entrySet()){
			
			if (query_subject.getValue().getType().equalsIgnoreCase("Final")){
				
				FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[" + query_subject.getValue().getTable_name() + "]");
				FactorySVC.renameQuerySubject("[PHYSICALUSED].[" + query_subject.getValue().getTable_name() + "]", "FINAL_" + query_subject.getValue().getTable_alias());
				
				FactorySVC.createQuerySubject("PHYSICALUSED", "FINAL", "FINAL_" + query_subject.getValue().getTable_alias(), query_subject.getValue().getTable_alias());
				
				FactorySVC.createQuerySubject("FINAL", "DATA", query_subject.getValue().getTable_alias() , query_subject.getValue().getTable_alias());
				//tooltip
				String desc = "";
				if(query_subject.getValue().getDescription() != null) {desc = ": " + query_subject.getValue().getDescription();}
				FactorySVC.createScreenTip("querySubject", "[DATA].[" + query_subject.getValue().getTable_alias() + "]" , query_subject.getValue().getTable_name() + desc );
				//end tooltip
				
				for(Relation rel: query_subject.getValue().getRelations()){
					if(rel.isFin()){
				
						RelationShip RS = new RelationShip("[FINAL].[" + query_subject.getValue().getTable_alias() + "]" , "[FINAL].[" + rel.getPktable_alias() + "]");
						// changer en qs + refobj
						RS.setExpression(rel.getRelationship());
						RS.setCard_left_min("one");
						RS.setCard_left_max("many");
	
						RS.setCard_right_min("one");
						RS.setCard_right_max("one");
						RS.setParentNamespace("FINAL");
						rsList.add(RS);					
				
					}
					if(rel.isRef()){
						
						String pkAlias = rel.getPktable_alias();
						Integer i = gRefMap.get(pkAlias);
						
						if(i == null){
							gRefMap.put(pkAlias, new Integer(0));
							i = gRefMap.get(pkAlias);
						}
						gRefMap.put(pkAlias, i + 1);
			
						FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[" + rel.getPktable_name() + "]");	
						FactorySVC.renameQuerySubject("[PHYSICALUSED].[" + rel.getPktable_name() + "]","REF_" + pkAlias + String.valueOf(i));
						FactorySVC.createQuerySubject("PHYSICALUSED", "REF","REF_" + pkAlias + String.valueOf(i), pkAlias + String.valueOf(i));
	
						RelationShip RS = new RelationShip("[FINAL].[" + query_subject.getValue().getTable_alias() + "]" , "[REF].[" + rel.getPktable_alias()  + String.valueOf(i) + "]");
						// changer en qs + refobj
						String exp = rel.getRelationship();
						String fixedExp = StringUtils.replace(exp, "[REF].[" + rel.getPktable_alias() + "]", "[REF].[" + rel.getPktable_alias()  + String.valueOf(i) + "]");
						RS.setExpression(fixedExp);
						RS.setCard_left_min("one");
						RS.setCard_left_max("many");
	
						RS.setCard_right_min("one");
						RS.setCard_right_max("one");
						RS.setParentNamespace("AUTOGENERATION");
						rsList.add(RS);
						
						String gFieldName = "";
						String gDirName = "";
						String label = "";
						
						//seq
						if(rel.getKey_type().equalsIgnoreCase("P") || rel.isNommageRep()){
//							gFieldName = rel.getPktable_alias();
//							gDirName = "." + rel.getPktable_alias();
							gFieldName = pkAlias;
							gDirName = "." + pkAlias;
							// add labels to map
							if(query_subjects.get(pkAlias + "Ref").getLabel() == null || query_subjects.get(pkAlias + "Ref").getLabel().equals(""))
							{label = pkAlias;} else {label = query_subjects.get(pkAlias + "Ref").getLabel();
							}
							labelMap.put(query_subject.getValue().getTable_alias() + gDirName , label);
							//end labels
						}
						else{
							gFieldName = rel.getSeqs().get(0).getColumn_name();
							gDirName = "." + rel.getSeqs().get(0).getColumn_name();
						}
						String gFieldNameReorder = rel.getSeqs().get(0).getColumn_name();
						FactorySVC.createSubFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName);
						//tooltip
						desc = "";
						if(query_subjects.get(pkAlias + "Ref").getDescription() != null) {desc = ": " + query_subjects.get(pkAlias + "Ref").getDescription();}
						FactorySVC.createScreenTip("queryItemFolder", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gDirName + "]", query_subjects.get(pkAlias + "Ref").getTable_name() + desc);

						if(rel.getKey_type().equalsIgnoreCase("F")){
							FactorySVC.ReorderSubFolderBefore("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gDirName + "]", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldNameReorder + "]");
						}
						
						for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
							
							FactorySVC.createQueryItemInFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName, gFieldName + "." + field.getField_name(), "[REF].["+ pkAlias + String.valueOf(i) +"].[" + field.getField_name() + "]");
							//add label
							if(field.getLabel() == null || field.getLabel().equals(""))
							{label = field.getField_name();} else {label = field.getLabel();
							}
							labelMap.put(query_subject.getValue().getTable_alias() + "." + gFieldName + "." + field.getField_name(), label);
							// end label
							//add tooltip
							desc = "";
							if(field.getDescription() != null) {desc = ": " + field.getDescription();}
							FactorySVC.createScreenTip("queryItem", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldName + "." + field.getField_name() + "]", query_subjects.get(pkAlias + "Ref").getTable_name() + "." + field.getField_name() + desc);
							//end tooltip
							//change property query item
							FactorySVC.changeQueryItemProperty("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldName + "." + field.getField_name() + "]", "usage", field.getIcon().toLowerCase());
							if (!field.getDisplayType().toLowerCase().equals("value"))
							{
								FactorySVC.changeQueryItemProperty("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldName + "." + field.getField_name() + "]", "displayType", field.getDisplayType().toLowerCase());
								
							}
							//end change
						}
						
						
						for(QuerySubject qs: qsList){
				        	recurseCount.put(qs.getTable_alias(), 0);
				        }
						
						f1(pkAlias, pkAlias + i, gDirName, "[DATA].[" + query_subject.getValue().getTable_alias() + "]", query_subject.getValue().getTable_alias(), recurseCount);
					}

				}
				//add label map qs
				labelMap.put(query_subject.getValue().getTable_alias(), query_subject.getValue().getLabel());
				
				//add label map fields
				for(Field field: query_subject.getValue().getFields()) {
					labelMap.put(query_subject.getValue().getTable_alias() + "." + field.getField_name(), field.getLabel());
				//add tooltip
				desc = "";
				if(field.getDescription() != null) {desc = ": " + field.getDescription();}	
				FactorySVC.createScreenTip("queryItem", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + field.getField_name() + "]", query_subject.getValue().getTable_name() + "." + field.getField_name() + desc);
				//end tooltip
				//change property query item
				FactorySVC.changeQueryItemProperty("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + field.getField_name() + "]", "usage", field.getIcon().toLowerCase());
				if (!field.getDisplayType().toLowerCase().equals("value"))
				{
					FactorySVC.changeQueryItemProperty("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + field.getField_name() + "]", "displayType", field.getDisplayType().toLowerCase());
				}
				//end change
				}
				// end label		
			}
			
		}
		TaskerSVC.IICCreateRelation(rsList);
		ProjectSVC.addLocale("en");

/*
		for(Entry<String, String> map: labelMap.entrySet()){
			System.out.println(map.getKey() + " * * * * * " + map.getValue());
		}
*/		
		
		
		// tests
		
		
		// fin tests
	
		TaskerSVC.stop();
//		TaskerSVC.start();
//		TaskerSVC.stop();
		
		// code parser xml for labels
		
		System.out.println("START XML MODIFICATION");
		try {
			File xmlFile = new File("/root/ddtool/WebContent/res/templates/model-label-test.xml");
			SAXReader reader = new SAXReader();
			Document document = reader.read(xmlFile);
			
			String namespaceName = "AUTOGENERATION";
			String spath = "/project/namespace/namespace";
			int k=1;
			
			Element namespace = (Element) document.selectSingleNode(spath + "[" + k + "]/name");			
			while(!namespace.getStringValue().equals(namespaceName) && namespace != null)
			{
			k++;
			namespace = (Element) document.selectSingleNode(spath + "[" + k + "]/name");
			}
			namespaceName = "DATA";
			spath = spath + "[" + k + "]";
						
			k=1;
			namespace = (Element) document.selectSingleNode(spath + "/namespace[" + k + "]/name");
			while(!namespace.getStringValue().equals(namespaceName) && namespace!=null)
			{
			k++;
			namespace = (Element) document.selectSingleNode(spath + "/namespace[" + k + "]/name");
			}
			spath = spath + "/namespace[" + k + "]";
			FactorySVC.recursiveParserQS(document, spath, "en", labelMap);

			// test writer

			OutputFormat format = OutputFormat.createPrettyPrint();
		    format.setEncoding(document.getXMLEncoding());
		    PrintStream ps = null;
		    try {
				ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("/root/ddtool/WebContent/res/templates/model.xml")), true);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			XMLWriter writer = null;
			try {
				writer = new XMLWriter( ps, format );
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch blockco
				e.printStackTrace();
			}
			try {
				writer.write( document );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// fin test writer
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// fin code xml for labels
		System.out.println("START XML MODIFICATION");
		System.out.println("FIN DU MAIN");
	}
	
	protected static void f1(String qsAlias, String qsAliasInc, String gDirName, String qsFinal, String qsFinalName, Map<String, Integer> recurseCount){
		
		Map<String, Integer> copyRecurseCount = new HashMap<String, Integer>();
		copyRecurseCount.putAll(recurseCount);
		
		QuerySubject query_subject = query_subjects.get(qsAlias + "Ref");

		int j = copyRecurseCount.get(qsAlias);
		if(j == query_subject.getRecurseCount()){
			return;
		}
		copyRecurseCount.put(qsAlias, j + 1);

		for(Relation rel: query_subject.getRelations()){
			if(rel.isRef()){
				
				String pkAlias = rel.getPktable_alias();
				Integer i = gRefMap.get(pkAlias);
				
				if(i == null){
					gRefMap.put(pkAlias, new Integer(0));
					i = gRefMap.get(pkAlias);
				}
				gRefMap.put(pkAlias, i + 1);

				FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[" + rel.getPktable_name() + "]");	
				FactorySVC.renameQuerySubject("[PHYSICALUSED].[" + rel.getPktable_name() + "]","REF_" + pkAlias + String.valueOf(i));
				FactorySVC.createQuerySubject("PHYSICALUSED", "REF","REF_" + pkAlias + String.valueOf(i), pkAlias + String.valueOf(i));
				
				//seq
				String gFieldName = "";
				String gDirNameCurrent = "";
				String label = "";
				if(rel.getKey_type().equalsIgnoreCase("P") || rel.isNommageRep()){
					gFieldName = gDirName.substring(1) + "." + pkAlias;
					gDirNameCurrent = gDirName + "." + pkAlias;
					if(query_subjects.get(pkAlias + "Ref").getLabel() == null || query_subjects.get(pkAlias + "Ref").getLabel().equals(""))
					{label = pkAlias;} else {label = query_subjects.get(pkAlias + "Ref").getLabel();
					}
					labelMap.put(qsFinalName + gDirNameCurrent, label);
				}
				else{
					gFieldName = gDirName.substring(1) + "." + rel.getSeqs().get(0).getColumn_name();
					gDirNameCurrent = gDirName + "." + rel.getSeqs().get(0).getColumn_name();
				}
				String gFieldNameReorder = gDirName.substring(1) + "." + rel.getSeqs().get(0).getColumn_name();
				String rep = qsFinal + ".[" + gDirName + "]";
				
				FactorySVC.createSubFolderInSubFolderIIC(rep, gDirNameCurrent);
				
				//add tooltip
				String desc = "";
				if(query_subjects.get(pkAlias + "Ref").getDescription() != null) {desc = ": " + query_subjects.get(pkAlias + "Ref").getDescription();}
				FactorySVC.createScreenTip("queryItemFolder", qsFinal + ".[" + gDirNameCurrent + "]", query_subjects.get(pkAlias + "Ref").getTable_name() + desc);
				//end tooltip
				
				if(rel.getKey_type().equalsIgnoreCase("F")){
					FactorySVC.ReorderSubFolderBefore(qsFinal + ".[" + gDirNameCurrent + "]", qsFinal + ".[" + gFieldNameReorder + "]");
				}
				
				for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
					
					FactorySVC.createQueryItemInFolder(qsFinal, gDirNameCurrent, gFieldName + "." + field.getField_name(), "[REF].["+ pkAlias + String.valueOf(i) +"].[" + field.getField_name() + "]");
					
					//add label
					if(field.getLabel() == null || field.getLabel().equals(""))
					{label = field.getField_name();} else {label = field.getLabel();
					}
					labelMap.put(qsFinalName + "." + gFieldName + "." + field.getField_name(), label);
					// end label
					// add tooltip
					desc = "";
					if(field.getDescription() != null) {desc = ": " + field.getDescription();}
					FactorySVC.createScreenTip("queryItem", qsFinal + ".[" + gFieldName + "." + field.getField_name() + "]", query_subjects.get(pkAlias + "Ref").getTable_name() + "." + field.getField_name() + desc);
					// end tooltip
					//change property query item
					FactorySVC.changeQueryItemProperty(qsFinal + ".[" + gFieldName + "." + field.getField_name() + "]", "usage", field.getIcon().toLowerCase());
					if (!field.getDisplayType().toLowerCase().equals("value"))
					{
						FactorySVC.changeQueryItemProperty(qsFinal + ".[" + gFieldName + "." + field.getField_name() + "]", "displayType", field.getDisplayType().toLowerCase());
						
					}
					//end change
				}
				
				RelationShip RS = new RelationShip("[REF].[" + qsAliasInc + "]" , "[REF].[" + pkAlias + String.valueOf(i) + "]");
				// changer en qs + refobj
				String fixedExp = StringUtils.replace(rel.getRelationship(), "[REF].[" + qsAlias + "]", "[REF].[" + qsAliasInc + "]");
				fixedExp = StringUtils.replace(fixedExp, "[REF].[" + pkAlias + "]", "[REF].[" + pkAlias + String.valueOf(i) + "]");
				RS.setExpression(fixedExp);
				RS.setCard_left_min("one");
				RS.setCard_left_max("many");

				RS.setCard_right_min("one");
				RS.setCard_right_max("one");
				RS.setParentNamespace("REF");
				rsList.add(RS);
				

				f1(pkAlias, pkAlias + String.valueOf(i), gDirNameCurrent, qsFinal, qsFinalName, copyRecurseCount);
				
			}
		}
		
		
		
	}


}
