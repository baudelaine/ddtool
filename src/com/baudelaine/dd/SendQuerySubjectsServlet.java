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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import datadixit.limsbi.pojos.RelationShip;
import datadixit.limsbi.svc.FactorySVC;
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
        
        for(QuerySubject qs: qsList){
        	query_subjects.put(qs.get_id(), qs);
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
		
		try{
			
			TaskerSVC.start();
	
			TaskerSVC.init();
			TaskerSVC.Import();
			TaskerSVC.IICInitNameSpace();
	
			
			
			List<Object> result = new ArrayList<Object>();
		
			// start creation tronc
			
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
							
							//seq
							String gFieldName = rel.getSeqs().get(0).getColumn_name();
							String gDirName = "." + rel.getSeqs().get(0).getColumn_name();
							FactorySVC.createSubFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName);
							FactorySVC.ReorderSubFolderBefore("[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gDirName + "]", "[DATA].[" + query_subject.getValue().getTable_alias() + "].[" + gFieldName + "]");
							
							for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
								
								FactorySVC.createQueryItemInFolder("[DATA].[" + query_subject.getValue().getTable_alias() + "]", gDirName, gFieldName + "." + field.getField_name(), "[REF].["+ pkAlias + String.valueOf(i) +"].[" + field.getField_name() + "]");
								
							}
							
							f1(pkAlias, pkAlias + i, gDirName, "[DATA].[" + query_subject.getValue().getTable_alias() + "]" );
						}
					}
					
				}
			}

			// start creation troncs
			
//			for(Entry<String, QuerySubject> query_subject: query_subjects.entrySet()){
//	
//				// on reset tout les inc de gRefMap
//				for(Entry<String, RefMap> rm: gRefMap.entrySet()){
//					rm.getValue().resetInc();
//				}
//				
//				System.out.println("+++++++++++++++ alias=" + query_subject.getValue().getTable_alias());
//				System.out.println("+++++++++++++++ table=" + query_subject.getValue().getTable_name());
//				System.out.println("+++++++++++++++ type=" + query_subject.getValue().getType());
//				
//				if (query_subject.getValue().getType().equalsIgnoreCase("Final")){					
//								
//					for(Relation rel: query_subject.getValue().getRelations()){
//						if(rel.isRef()){
//							
//							String alias = rel.getPktable_name();
//							
//							RefMap refMap = gRefMap.get(alias);
//							String qs = "";
//							
//							if(refMap == null){
//								refMap = new RefMap();
//								refMap.add(alias, true);
//								refMap.incInc();
//								gRefMap.put(alias, refMap);
//								
//								qs = alias + refMap.getCount();
//								
//								FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), qs);
//								
//
//							}
//							else{
//								refMap.incInc();
//								if(refMap.getInc() > refMap.getCount()){
//									refMap.add(alias, true);
//									gRefMap.put(alias, refMap);
//									
//									qs = alias + refMap.getCount();
//									
//									FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), qs);
//								}
//								
//							}
//
//							qs = alias + refMap.getInc();
//
//							// on cree un arbre pour la seq[0] de chaque ref cochée du qs  final
//							
//							RelationShip RS = new RelationShip("[FINAL].[" + query_subject.getValue().getTable_alias() + "]" , "[REF].[" + qs + "]");
//							
//							String exp = rel.getRelationship();
//							String fixedExp = StringUtils.replace(exp, "[REF].[" + alias + "]", "[REF].[" + qs + "]");
//							RS.setExpression(fixedExp);
//							
//							RS.setCard_left_min("one");
//							RS.setCard_left_max("many");
//
//							RS.setCard_right_min("one");
//							RS.setCard_right_max("one");
//							RS.setParentNamespace("AUTOGENERATION");
//							rsList.add(RS);
//
//							String gTableAlias = query_subject.getValue().getTable_alias();
//							String gFieldName = rel.getSeqs().get(0).getColumn_name();
//							String gDirName = "." + rel.getSeqs().get(0).getColumn_name();
//							System.out.println("QS=" + qs);
//							refMap.addDir(qs, "[DATA].[" + gTableAlias + "];" + gDirName);
//							
//							FactorySVC.createSubFolder("[DATA].[" + gTableAlias + "]", gDirName);
//							
//							FactorySVC.ReorderSubFolderBefore("[DATA].[" + gTableAlias + "].[" + gDirName + "]", "[DATA].[" + gTableAlias + "].[" + gFieldName + "]");
//							
//							for(Field field: query_subjects.get(alias + "Ref").getFields()){
//								
//								FactorySVC.createQueryItemInFolder("[DATA].[" + gTableAlias + "]", gDirName, gFieldName + "." + field.getField_name(), "[REF].["+ qs +"].[" + field.getField_name() + "]");
//								
//							}
//								
//						}
//					}
//
//				}
//	
//		    }
			// end creation troncs
			

			
			

//			Map<String, RefMap> troncs = new HashMap<String, RefMap>();
//			
//			for(Entry<String, RefMap> tronc: gRefMap.entrySet()){
//				RefMap value = tronc.getValue();
//				RefMap copy = new RefMap(value.getCount(), value.getInc(), value.getTreeCount(), value.getTree());
//				troncs.put(tronc.getKey(), copy);
//				
//			}
//			
//					
//			System.out.println("+-+-+-+-+-+- TRONCS " + troncs);
//			
//			for(Entry<String, RefMap> tronc: troncs.entrySet()){
//				String aliasTronc = tronc.getKey();
//				System.out.println("+-+-+-+-+- aliasTronc " + aliasTronc);
//				Map<String, Boolean> qsTroncs = tronc.getValue().getTree();
//				for(Entry<String, Boolean> qsTronc: qsTroncs.entrySet()){
//					System.out.println("+-+-+-+-+- qsTronc " + qsTronc);
//					
//					f0(aliasTronc, qsTronc.getKey());
//					
//					}
//					
//				
//			}
			
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
	
	protected void f1(String qsAlias, String qsAliasInc, String gDirName, String qsFinal){
		
		QuerySubject query_subject = query_subjects.get(qsAlias + "Ref");
		
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
				String gFieldName = gDirName.substring(1) + "." + rel.getSeqs().get(0).getColumn_name();
				String rep = qsFinal + ".[" + gDirName + "]";
				String gDirNameCurrent = gDirName + "." + rel.getSeqs().get(0).getColumn_name();
				

				System.out.println("qsAlias=" + qsAlias);
				System.out.println("qsAliasInc=" + qsAliasInc);
				System.out.println("rep=" + rep);
				System.out.println("qsFinal=" + qsFinal);

				System.out.println("gFieldName=" + gFieldName);
				System.out.println("gDirName=" + gDirNameCurrent);
				
				System.out.println("FactorySVC.createSubFolderInSubFolderIIC(" + rep + "," + gDirNameCurrent +")");
				FactorySVC.createSubFolderInSubFolderIIC(rep, gDirNameCurrent);
				
				
				System.out.println("FactorySVC.ReorderSubFolderBefore");
				FactorySVC.ReorderSubFolderBefore(qsFinal + ".[" + gDirNameCurrent + "]", qsFinal + ".[" + gFieldName + "]");
				
				
				for(Field field: query_subjects.get(pkAlias + "Ref").getFields()){
					
					System.out.println("FactorySVC.createQueryItemInFolder");
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

				f1(pkAlias, pkAlias + String.valueOf(i), gDirNameCurrent, qsFinal);
			}
		}
		
		
		
	}

//	protected void f0(String aliasTronc, String qs){
//		
//		System.out.println("aliasTronc=" + aliasTronc);
//		System.out.println("qs=" + qs);
//	
//		QuerySubject query_subject = query_subjects.get(aliasTronc + "Ref");
//		
//		System.out.println("+-+-+-+-" + query_subject.get_id());
//		for(Relation rel: query_subject.getRelations()){
//			if(rel.isRef()){
//				
//				String alias = rel.getPktable_name();
//				
//				RefMap refMap = gRefMap.get(alias);
//				
//				if(refMap == null){
//					System.out.println("+-+-+-+- ON RENTRE DANS LE NULL");
//					refMap = new RefMap();
//					gRefMap.put(alias, refMap);
//				}
//				refMap.add(alias, false);
//
//				
//				FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), alias + refMap.getCount());
//				
//
//				RefMap aliasTroncRefMap = gRefMap.get(aliasTronc);
//				
//				
//				for(String dir: aliasTroncRefMap.getDirList().get(qs)){	
//					String qsFinal = dir.split(";")[0];
//					String rep = dir.split(";")[1];
//					System.out.println("dir=" + dir + ";qsFinal=" + qsFinal + ";rep=" + rep);
//					System.out.println("rel.getSeqs().get(0).getColumn_name()=" + rel.getSeqs().get(0).getColumn_name());
//					FactorySVC.createSubFolderInSubFolder(qsFinal, rep, rep + "." + rel.getSeqs().get(0).getColumn_name());
//					System.out.println("rep créé :" + rep + "." + rel.getSeqs().get(0).getColumn_name());
//					FactorySVC.ReorderSubFolderBefore(qsFinal + ".[" + rep + "." + rel.getSeqs().get(0).getColumn_name() + "]", 
//							qsFinal + ".[" + rep.substring(1) + "." + rel.getSeqs().get(0).getColumn_name() + "]");
//					refMap.addDir(alias + refMap.getCount(), qsFinal + ";" + rep + "." + rel.getSeqs().get(0).getColumn_name());
//					
//					for(Field field: query_subjects.get(alias + "Ref").getFields()){
//						
//						FactorySVC.createQueryItemInFolder(qsFinal, rep + "." + rel.getSeqs().get(0).getColumn_name(), rep.substring(1) + "." + rel.getSeqs().get(0).getColumn_name() + "." + field.getField_name(), "[REF].["+ alias + refMap.getCount() +"].[" + field.getField_name() + "]");
//					}
//
//					
//				}
//
//	        	System.out.print("------------- QS " + qs);
//	        	
//	        	RefMap rfg = gRefMap.get(aliasTronc);
//	        	
//				
//				RelationShip RS = new RelationShip("[REF].[" + qs + "]" , "[REF].[" + alias + refMap.getCount() + "]");
//				// changer en qs + refobj
//				String fixedExp = StringUtils.replace(rel.getRelationship(), "[REF].[" + aliasTronc + "]", "[REF].[" + qs + "]");
//				fixedExp = StringUtils.replace(fixedExp, "[REF].[" + alias + "]", "[REF].[" + alias + refMap.getCount() + "]");
//				RS.setExpression(fixedExp);
//				RS.setCard_left_min("one");
//				RS.setCard_left_max("many");
//
//				RS.setCard_right_min("one");
//				RS.setCard_right_max("one");
//				RS.setParentNamespace("REF");
//				rsList.add(RS);
//				
//				// Le rappel recursif
//				f0(alias, alias + refMap.getCount());
//				
//			}
//		}
//	}
	
}
