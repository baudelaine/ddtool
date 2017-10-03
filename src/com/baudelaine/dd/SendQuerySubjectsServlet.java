package com.baudelaine.dd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import datadixit.limsbi.pojos.RelationShip;
import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.ProjectSVC;
import datadixit.limsbi.svc.TaskerSVC;

/**
 * Servlet implementation class GetSelectionsServlet
 */
@WebServlet(name = "SendQuerySubjects", urlPatterns = { "/SendQuerySubjects" })
public class SendQuerySubjectsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	Map<String, Integer> gRefMap;
	List<RelationShip> rsList;
	Map<String, QuerySubject> query_subjects;
	

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendQuerySubjectsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        

//        QuerySubject[] qsArray = mapper.readValue(br, QuerySubject[].class);
        List<QuerySubject> qsList = Arrays.asList(mapper.readValue(br, QuerySubject[].class));
        
        query_subjects = new HashMap<String, QuerySubject>();
        Map<String, Integer> recurseCount = new HashMap<String, Integer>();
        
        for(QuerySubject qs: qsList){
        	query_subjects.put(qs.get_id(), qs);
        	recurseCount.put(qs.getTable_alias(), 0);
        }
        
//        if(br != null){
//            while((json = br.readLine()) != null){
//	            System.out.println("json="+json);
//	            QuerySubject query_subject = mapper.readValue(json, QuerySubject.class);
//	            
//            }
//        }
        
        request.getSession().setAttribute("query_subjects", query_subjects);;
		
		query_subjects = (Map<String, QuerySubject>) request.getSession().getAttribute("query_subjects");
		
		System.out.println("+++++++++++ query_subjects.size=" + query_subjects.size());
		
		List<Object> result = new ArrayList<Object>();

		try{
			
			
			TaskerSVC.start();
	
			TaskerSVC.init();
			TaskerSVC.Import();
			TaskerSVC.IICInitNameSpace();
			
		
			gRefMap = new HashMap<String, Integer>();
			
			rsList = new ArrayList<RelationShip>();
			
			// create qs in data namespace
			
			for(Entry<String, QuerySubject> query_subject: query_subjects.entrySet()){
				if (query_subject.getValue().getType().equalsIgnoreCase("Final")){
					
					FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[" + query_subject.getValue().getTable_name() + "]");
					FactorySVC.renameQuerySubject("[PHYSICALUSED].[" + query_subject.getValue().getTable_name() + "]", "FINAL_" + query_subject.getValue().getTable_alias());
					
					FactorySVC.createQuerySubject("PHYSICALUSED", "FINAL", "FINAL_" + query_subject.getValue().getTable_alias(), query_subject.getValue().getTable_alias());
					System.out.println("FactorySVC.createQuerySubject PHYSICALUSED, FINAL, " + query_subject.getValue().getTable_alias() + ", " + query_subject.getValue().getTable_alias());

					FactorySVC.createQuerySubject("FINAL", "DATA", query_subject.getValue().getTable_alias() , query_subject.getValue().getTable_alias());
					
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
							
							System.out.println("pkAlias=" + pkAlias);
							
							Integer i = gRefMap.get(pkAlias);
							System.out.println("i avant=" + i);
							
							if(i == null){
								gRefMap.put(pkAlias, new Integer(0));
								i = gRefMap.get(pkAlias);
								
							}
							gRefMap.put(pkAlias, i + 1);
							
							System.out.println("i apres=" + i);
							
							
							System.out.println("pkAlias + i =" + pkAlias + String.valueOf(i));
							
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
							//seq
							if(rel.getKey_type().equalsIgnoreCase("P") || rel.isNommageRep()){
//								gFieldName = rel.getPktable_alias();
//								gDirName = "." + rel.getPktable_alias();
								gFieldName = pkAlias;
								gDirName = "." + pkAlias;
							}
							else{
								gFieldName = rel.getSeqs().get(0).getColumn_name();
								gDirName = "." + rel.getSeqs().get(0).getColumn_name();
							}
							String gFieldNameReorder = rel.getSeqs().get(0).getColumn_name();
							FactorySVC.createSubFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName);

							if(rel.getKey_type().equalsIgnoreCase("F")){
								FactorySVC.ReorderSubFolderBefore("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gDirName + "]", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldNameReorder + "]");
							}
							
							for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
								
								FactorySVC.createQueryItemInFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName, gFieldName + "." + field.getField_name(), "[REF].["+ pkAlias + String.valueOf(i) +"].[" + field.getField_name() + "]");
								
							}
							
							for(QuerySubject qs: qsList){
					        	recurseCount.put(qs.getTable_alias(), 0);
					        }
							
							f1(pkAlias, pkAlias + i, gDirName, "[DATA].[" + query_subject.getValue().getTable_alias() + "]", recurseCount);
						}
					}
					
				}
			}

			
			TaskerSVC.IICCreateRelation(rsList);
			
			TaskerSVC.stop();

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(result));
			
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void f1(String qsAlias, String qsAliasInc, String gDirName, String qsFinal, Map<String, Integer> recurseCount){
		
		Map<String, Integer> copyRecurseCount = new HashMap<String, Integer>();
		copyRecurseCount.putAll(recurseCount);
		
		QuerySubject query_subject = query_subjects.get(qsAlias + "Ref");

		int j = copyRecurseCount.get(qsAlias);
		System.out.println("*l*l*l*l*l* copyRecurseCount.get(" + qsAlias + ") = " + j);
		System.out.println("*l*l*l*l*l* query_subject.getRecurseCount()=" + query_subject.getRecurseCount());
		if(j == query_subject.getRecurseCount()){
//			recurseCount.put(qsAlias, 0);
			System.out.println("*l*l*l*l*l* return");
			return;
		}
		
		System.out.println("*l*l*l*l*l* copyRecurseCount.put(" + qsAlias + "," + j + "+" + 1 + ")");
		copyRecurseCount.put(qsAlias, j + 1);

		for(Relation rel: query_subject.getRelations()){
			if(rel.isRef()){
				
				String pkAlias = rel.getPktable_alias();
				
				System.out.println("pkAlias=" + pkAlias);
				
				Integer i = gRefMap.get(pkAlias);
				
				if(i == null){
					gRefMap.put(pkAlias, new Integer(0));
					i = gRefMap.get(pkAlias);
					
				}
				gRefMap.put(pkAlias, i + 1);
				
				System.out.println("pkAlias + i =" + pkAlias + String.valueOf(i));
				
				FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[" + rel.getPktable_name() + "]");	
				FactorySVC.renameQuerySubject("[PHYSICALUSED].[" + rel.getPktable_name() + "]","REF_" + pkAlias + String.valueOf(i));
				FactorySVC.createQuerySubject("PHYSICALUSED", "REF","REF_" + pkAlias + String.valueOf(i), pkAlias + String.valueOf(i));
				
				
				//seq
				String gFieldName = "";
				String gDirNameCurrent = "";
				if(rel.getKey_type().equalsIgnoreCase("P") || rel.isNommageRep()){
					gFieldName = gDirName.substring(1) + "." + pkAlias;
					gDirNameCurrent = gDirName + "." + pkAlias;
				}
				else{
					gFieldName = gDirName.substring(1) + "." + rel.getSeqs().get(0).getColumn_name();
					gDirNameCurrent = gDirName + "." + rel.getSeqs().get(0).getColumn_name();
				}
				String gFieldNameReorder = gDirName.substring(1) + "." + rel.getSeqs().get(0).getColumn_name();
				String rep = qsFinal + ".[" + gDirName + "]";
				

				System.out.println("qsAlias=" + qsAlias);
				System.out.println("qsAliasInc=" + qsAliasInc);
				System.out.println("rep=" + rep);
				System.out.println("qsFinal=" + qsFinal);

				System.out.println("gFieldName=" + gFieldName);
				System.out.println("gDirName=" + gDirNameCurrent);
				
				System.out.println("FactorySVC.createSubFolderInSubFolderIIC(" + rep + "," + gDirNameCurrent +")");
				FactorySVC.createSubFolderInSubFolderIIC(rep, gDirNameCurrent);
				
				if(rel.getKey_type().equalsIgnoreCase("F")){
					System.out.println("FactorySVC.ReorderSubFolderBefore");
					FactorySVC.ReorderSubFolderBefore(qsFinal + ".[" + gDirNameCurrent + "]", qsFinal + ".[" + gFieldNameReorder + "]");
				}
				
				
				for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
					
					FactorySVC.createQueryItemInFolder(qsFinal, gDirNameCurrent, gFieldName + "." + field.getField_name(), "[REF].["+ pkAlias + String.valueOf(i) +"].[" + field.getField_name() + "]");
					
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
				

				f1(pkAlias, pkAlias + String.valueOf(i), gDirNameCurrent, qsFinal, copyRecurseCount);
				
			}
		}
		
		
		
	}

}
