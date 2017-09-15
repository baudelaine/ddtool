package com.baudelaine.dd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

	Map<String, RefMap> gRefMap;
	List<RelationShip> rsList;
	Map<String, QuerySubject> query_subjects;
//	Map<String, DefaultMutableTreeNode> arbres = new HashMap<String, DefaultMutableTreeNode>();
	
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
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        query_subjects = new HashMap<String, QuerySubject>();
        
        if(br != null){
            while((json = br.readLine()) != null){
	            System.out.println("json="+json);
	            QuerySubject query_subject = mapper.readValue(json, QuerySubject.class);
	            query_subjects.put(query_subject.get_id(), query_subject);
            }
        }
        
        
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
			
			gRefMap = new HashMap<String, RefMap>();
			
			rsList = new ArrayList<RelationShip>();
			
			// create qs in data namespace
			
			for(Entry<String, QuerySubject> query_subject: query_subjects.entrySet()){
				if (query_subject.getValue().getType().equalsIgnoreCase("Final")){
					
					FactorySVC.createQuerySubject("PHYSICAL", "FINAL", query_subject.getValue().getTable_name(), query_subject.getValue().getTable_alias());
					System.out.println("FactorySVC.createQuerySubject PHYSICAL, FINAL, " + query_subject.getValue().getTable_name() + ", " + query_subject.getValue().getTable_alias());

					FactorySVC.createQuerySubject("FINAL", "DATA", query_subject.getValue().getTable_alias() , query_subject.getValue().getTable_alias());
					
					for(Relation rel: query_subject.getValue().getRelations()){
						if(rel.isFin()){
					
							RelationShip RS = new RelationShip("[DATA].[" + query_subject.getValue().getTable_alias() + "]" , "[DATA].[" + rel.getPktable_alias() + "]");
							// changer en qs + refobj
							RS.setExpression(rel.getRelationship());
							RS.setCard_left_min("one");
							RS.setCard_left_max("many");
		
							RS.setCard_right_min("one");
							RS.setCard_right_max("one");
							RS.setParentNamespace("DATA");
							rsList.add(RS);					
					
						}	
					}
					
				}
			}

			// start creation troncs
			
			for(Entry<String, QuerySubject> query_subject: query_subjects.entrySet()){
	
				// on reset tout les inc de gRefMap
				for(Entry<String, RefMap> rm: gRefMap.entrySet()){
					rm.getValue().resetInc();
				}
				
				System.out.println("+++++++++++++++ alias=" + query_subject.getValue().getTable_alias());
				System.out.println("+++++++++++++++ table=" + query_subject.getValue().getTable_name());
				System.out.println("+++++++++++++++ type=" + query_subject.getValue().getType());
				
				if (query_subject.getValue().getType().equalsIgnoreCase("Final")){					
								
					for(Relation rel: query_subject.getValue().getRelations()){
						if(rel.isRef()){
							
							String alias = rel.getPktable_name();
							
							RefMap refMap = gRefMap.get(alias);
							String qs = "";
							
							if(refMap == null){
								refMap = new RefMap();
								refMap.add(alias, true);
								refMap.incInc();
								gRefMap.put(alias, refMap);
								
								qs = alias + refMap.getCount();
								
								FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), qs);
								

							}
							else{
								refMap.incInc();
								if(refMap.getInc() > refMap.getCount()){
									refMap.add(alias, true);
									gRefMap.put(alias, refMap);
									
									qs = alias + refMap.getCount();
									
									FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), qs);
								}
								
							}

							qs = alias + refMap.getInc();

							// on cree un arbre pour la seq[0] de chaque ref cochée du qs  final
//							String champ = rel.getSeqs().get(0).getColumn_name();
//							Dossier dossier = new Dossier(champ.toLowerCase(), qs, champ.toUpperCase());
//							String clef = query_subject.getValue().getTable_alias() + "." +  champ + "." + qs;
//							String FolderName = champ.toLowerCase();
//							String QSRef = qs;
//							String QS = alias;
//							String reoderBefore = champ.toUpperCase();
//							String valeurSep = ";";
//							String valeur = FolderName + valeurSep + QSRef + valeurSep + reoderBefore + valeurSep + QS;
//							arbres.put(clef, new DefaultMutableTreeNode(valeur));
//							refMap.addNoeud(qs, new DefaultMutableTreeNode(valeur));
							
							RelationShip RS = new RelationShip("[FINAL].[" + query_subject.getValue().getTable_alias() + "]" , "[REF].[" + qs + "]");
							
							String exp = rel.getRelationship();
//							System.out.println("+++++++++++ EXP AVANT= " + exp);
//							exp = exp.replaceAll("\\[" + query_subject.getValue().getTable_name() + "\\]", "[FINAL].[" + query_subject.getValue().getTable_alias() + "]");
//							System.out.println("+++++++++++ EXP ENCORE APRES= " + exp);
//							exp = exp.replaceAll("[" + rel.getValue().getPktable_name() + "]", "[REF].[" + qs + "]");
//							System.out.println("+++++++++++ EXP ENCORE APRES= " + exp);
//							exp = exp.replaceAll(" = ", " </refobj> = <refobj> ");
//							exp = exp.replaceAll(" AND ", " </refobj> AND <refobj> ");
//							exp = "<refobj> " + exp + " </refobj>";
							
							// [SYSUSER].[DEFAULTDEPARTMENT] = [DEPARTMENT].[DEPARTMENTID]
							// to be changed in
							// "and <refobj>" + [SYSUSERX].[DEFAULTDEPARTMENT] + "</refobj> = <refobj>" + [DEPARTMENTX].[DEPARTMENTID] + "</refobj>"
							
//							System.out.println("******** REGEX EXP =" +exp.replaceAll("\\[REF\\]\\.\\[[^\\]]+\\]", "[REF].["+qs+"]"));
							String fixedExp = StringUtils.replace(exp, "[REF].[" + alias + "]", "[REF].[" + qs + "]");
							RS.setExpression(fixedExp);
							
							RS.setCard_left_min("one");
							RS.setCard_left_max("many");

							RS.setCard_right_min("one");
							RS.setCard_right_max("one");
							RS.setParentNamespace("AUTOGENERATION");
							rsList.add(RS);

							String gTableAlias = query_subject.getValue().getTable_alias();
							String gFieldName = rel.getSeqs().get(0).getColumn_name();
							String gDirName = "." + rel.getSeqs().get(0).getColumn_name();
							System.out.println("QS=" + qs);
							refMap.addDir(qs, "[DATA].[" + gTableAlias + "];" + gDirName);
							
							FactorySVC.createSubFolder("[DATA].[" + gTableAlias + "]", gDirName);
							
							FactorySVC.ReorderSubFolderBefore("[DATA].[" + gTableAlias + "].[" + gDirName + "]", "[DATA].[" + gTableAlias + "].[" + gFieldName + "]");
							
							for(Field field: query_subjects.get(alias + "Ref").getFields()){
								
								FactorySVC.createQueryItemInFolder("[DATA].[" + gTableAlias + "]", gDirName, gFieldName + "." + field.getField_name(), "[REF].["+ qs +"].[" + field.getField_name() + "]");
//							
//							FactorySVC.createSubFolderInSubFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.baseDepartment");
//							
//							FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy.baseDepartment", "createBy.baseDepartment.DEPARTMENTID", "[REF].[DEPARTMENT4].[DEPARTMENTID]");
								
							}
								
						}
					}

				}
	
		    }
			// end creation troncs
			

			
			

			Map<String, RefMap> troncs = new HashMap<String, RefMap>();
			
			for(Entry<String, RefMap> tronc: gRefMap.entrySet()){
				RefMap value = tronc.getValue();
				RefMap copy = new RefMap(value.getCount(), value.getInc(), value.getTreeCount(), value.getTree());
				troncs.put(tronc.getKey(), copy);
				
			}
			
					
			System.out.println("+-+-+-+-+-+- TRONCS " + troncs);
			
			for(Entry<String, RefMap> tronc: troncs.entrySet()){
				String aliasTronc = tronc.getKey();
				System.out.println("+-+-+-+-+- aliasTronc " + aliasTronc);
				Map<String, Boolean> qsTroncs = tronc.getValue().getTree();
				for(Entry<String, Boolean> qsTronc: qsTroncs.entrySet()){
					System.out.println("+-+-+-+-+- qsTronc " + qsTronc);
					
					f0(aliasTronc, qsTronc.getKey());
					
					}
					
				
			}
			

			
			// create folders in qs
			
			
//			FactorySVC.ReorderSubFolderBefore("[DATA].[S_SAMPLE].[createBy]", "[DATA].[S_SAMPLE].[CREATEBY]");
//			
//			FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.SYSUSERID", "[REF].[SYSUSER1].[SYSUSERID]");
//			
//			FactorySVC.createSubFolderInSubFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.baseDepartment");
//			
//			FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy.baseDepartment", "cr  eateBy.baseDepartment.DEPARTMENTID", "[REF].[DEPARTMENT4].[DEPARTMENTID]");

				
//				for(Entry<String, DefaultMutableTreeNode> arbre: arbres.entrySet()){
//
//					String alias = arbre.getKey().split("\\.")[0];
//					
//					TreeNode root = arbre.getValue().getRoot();
//					
//					String dossier = root.toString();
//					
//					String FolderName = dossier.split(";")[0];
//					String QSRef = dossier.split(";")[1]; 
//					String reoderBefore = dossier.split(";")[2];
//					String QS = dossier.split(";")[3];
//					
//					FactorySVC.createSubFolder("[DATA].[" + alias + "]", FolderName.toLowerCase());
//					FactorySVC.ReorderSubFolderBefore("[DATA].["+ alias + "].[" + FolderName.toLowerCase() + "]", "[DATA].["+ alias + "].[" + reoderBefore + "]");
//				
//				}
			
			
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

	protected void f0(String aliasTronc, String qs){
		
		System.out.println("aliasTronc=" + aliasTronc);
		System.out.println("qs=" + qs);
	
		QuerySubject query_subject = query_subjects.get(aliasTronc + "Ref");
		
		System.out.println("+-+-+-+-" + query_subject.get_id());
		for(Relation rel: query_subject.getRelations()){
			if(rel.isRef()){
				
				String alias = rel.getPktable_name();
				
				RefMap refMap = gRefMap.get(alias);
				
				if(refMap == null){
					System.out.println("+-+-+-+- ON RENTRE DANS LE NULL");
					refMap = new RefMap();
					gRefMap.put(alias, refMap);
				}
				refMap.add(alias, false);

				
				FactorySVC.createQuerySubject("PHYSICAL", "REF", rel.getPktable_name(), alias + refMap.getCount());
				

				RefMap aliasTroncRefMap = gRefMap.get(aliasTronc);
				
				
				for(String dir: aliasTroncRefMap.getDirList().get(qs)){	
					String qsFinal = dir.split(";")[0];
					String rep = dir.split(";")[1];
					System.out.println("dir=" + dir + ";qsFinal=" + qsFinal + ";rep=" + rep);
					System.out.println("rel.getSeqs().get(0).getColumn_name()=" + rel.getSeqs().get(0).getColumn_name());
//					FactorySVC.createSubFolderInSubFolder("[DATA].[S_SAMPLE]", ".SUBMITTERID", ".SUBMITTERID.DEFAULTDEPARTMENT_Blabla");
					FactorySVC.createSubFolderInSubFolder(qsFinal, rep, rep + "." + rel.getSeqs().get(0).getColumn_name());
					System.out.println("rep créé :" + rep + "." + rel.getSeqs().get(0).getColumn_name());
					FactorySVC.ReorderSubFolderBefore(qsFinal + ".[" + rep + "." + rel.getSeqs().get(0).getColumn_name() + "]", 
							qsFinal + ".[" + rep.substring(1) + "." + rel.getSeqs().get(0).getColumn_name() + "]");
					refMap.addDir(alias + refMap.getCount(), qsFinal + ";" + rep + "." + rel.getSeqs().get(0).getColumn_name());
					
					for(Field field: query_subjects.get(alias + "Ref").getFields()){
						
						FactorySVC.createQueryItemInFolder(qsFinal, rep + "." + rel.getSeqs().get(0).getColumn_name(), rep.substring(1) + "." + rel.getSeqs().get(0).getColumn_name() + "." + field.getField_name(), "[REF].["+ alias + refMap.getCount() +"].[" + field.getField_name() + "]");
//					
//					FactorySVC.createSubFolderInSubFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.baseDepartment");
//					
//					FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy.baseDepartment", "createBy.baseDepartment.DEPARTMENTID", "[REF].[DEPARTMENT4].[DEPARTMENTID]");
						
					}

					
				}

				

//				for(Entry<String, DefaultMutableTreeNode> arbre: arbres.entrySet()){

//					FactorySVC.createSubFolder("[DATA].[S_SAMPLE], "createBy");

//					FactorySVC.ReorderSubFolderBefore("[DATA].[S_SAMPLE].[createBy]", "[DATA].[S_SAMPLE].[CREATEBY]");
//					
//					FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.SYSUSERID", "[REF].[SYSUSER1].[SYSUSERID]");
//					
//					FactorySVC.createSubFolderInSubFolder("[DATA].[S_SAMPLE]", "createBy", "createBy.baseDepartment");
//					
//					FactorySVC.createQueryItemInFolder("[DATA].[S_SAMPLE]", "createBy.baseDepartment", "createBy.baseDepartment.DEPARTMENTID", "[REF].[DEPARTMENT4].[DEPARTMENTID]");
					
					
//					DefaultMutableTreeNode pere = arbre.getValue();
//					String FolderNameDuPere = pere.toString().split(";")[0];
//					String QSref = arbre.getKey().split("\\.")[2];
//					if(qs.equalsIgnoreCase(QSref)){
//						String champ = rel.getValue().getSeqs().get(0).getColumn_name();
//						String FolderNameDuFils = FolderNameDuPere + "." + champ.toLowerCase();
//						String fils = FolderNameDuFils + ";" + qs + ";" + champ + ";" + alias;
//						DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(fils);
//						pere.add(newChild);
//						
//					}
					
					
//				}
				
				
	        	System.out.print("------------- QS " + qs);
	        	
	        	RefMap rfg = gRefMap.get(aliasTronc);
	        	
				
//				for(DefaultMutableTreeNode noeud: rfg.getNoeuds(qs)){
	/*        	
		        	System.out.print("------------- ON PASS DANS LA BOUCLE ");
		        	DefaultMutableTreeNode noeud = rfg.getNoeuds(qs).get(0);
					String FolderNameDuPere = noeud.toString().split(";")[0];
					String champ = rel.getSeqs().get(0).getColumn_name();
					String FolderNameDuFils = FolderNameDuPere + "." + champ.toLowerCase();
					String fils = FolderNameDuFils + ";" + qs + ";" + champ + ";" + aliasTronc;
					DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(fils);
					noeud.add(newChild);
					rfg.addNoeud(qs, newChild);
					System.out.print("-------------" + qs + "-------------");
					
					System.out.print("-------------" + newChild.toString() + "-------------");
	*/
//				}
				
				
				
				RelationShip RS = new RelationShip("[REF].[" + qs + "]" , "[REF].[" + alias + refMap.getCount() + "]");
				// changer en qs + refobj
				String fixedExp = StringUtils.replace(rel.getRelationship(), "[REF].[" + aliasTronc + "]", "[REF].[" + qs + "]");
				fixedExp = StringUtils.replace(fixedExp, "[REF].[" + alias + "]", "[REF].[" + alias + refMap.getCount() + "]");
				RS.setExpression(fixedExp);
				RS.setCard_left_min("one");
				RS.setCard_left_max("many");

				RS.setCard_right_min("one");
				RS.setCard_right_max("one");
				RS.setParentNamespace("REF");
				rsList.add(RS);
				
				// Le rappel recursif
				f0(alias, alias + refMap.getCount());
				
			}
		}
	}
	
}
