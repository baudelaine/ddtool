package datadixit.limsbi.action;

import datadixit.limsbi.svc.TaskerSVC;


public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		TaskerSVC.start();

		try{

			
			
		TaskerSVC.init();
		TaskerSVC.Import();
		
		TaskerSVC.IICInitNameSpace();
		
		TaskerSVC.IICCreateQuerySubject();
		
		TaskerSVC.IICCreateRelation();
		TaskerSVC.IICCreateRelation();
		TaskerSVC.IICCreateRelation();
		TaskerSVC.IICCreateRelation();
		TaskerSVC.IICCreateRelation();
		TaskerSVC.IICCreateRelation();
		
//		RS = new RelationShip("[DATA].[S_SAMPLE_ALIAS]", "[DATA].[S_BATCH_ALIAS]");
//		RS.setExpression("[DATA].[S_SAMPLE_ALIAS].[BATCHID] = [DATA].[S_BATCH_ALIAS].[S_BATCHID]");
//		RS.setCard_left_min("one");
//		RS.setCard_left_max("many");
//
//		RS.setCard_right_min("one");
//		RS.setCard_right_max("one");
//		RS.setParentNamespace("DATA");
//		
//		rsList.add(RS);
//
//		RS = new RelationShip("[DATA].[S_SAMPLE_ALIAS]", "[DATA].[S_BATCH_ALIAS]");
//		RS.setExpression("[DATA].[S_SAMPLE_ALIAS].[BATCHID] = [DATA].[S_BATCH_ALIAS].[S_BATCHID]");
//		RS.setCard_left_min("one");
//		RS.setCard_left_max("many");
//
//		RS.setCard_right_min("one");
//		RS.setCard_right_max("one");
//		RS.setParentNamespace("DATA");
//
//		rsList.add(RS);

//		for(RelationShip rs: rsList){
//			System.out.println("+++++++++++++" + System.currentTimeMillis());
//			Thread.sleep(10000);
//			System.out.println("+++++++++++++ Wait for 10 secs.....");
//			System.out.println("+++++++++++++" + System.currentTimeMillis());
//			System.out.println("++++++++++++ EXPRESSION=" + rsList.get(0).getExpression());
//			FactorySVC.createRelationship(rsList.get(0));
//		}

//		TaskerSVC.Security();

//		TaskerSVC.Final();
//		TaskerSVC.RefSDC();
//		TaskerSVC.Translanguage();
//		TaskerSVC.LinkTransLanguage();
//		TaskerSVC.Final_T();
//		TaskerSVC.RefSDC_T();
//		TaskerSVC.M2M_T();
//
//		TaskerSVC.Fields_T();
//		TaskerSVC.MakeTrees();
//		TaskerSVC.MakeMainFolders();
// 
//		TaskerSVC.MakeAllSubFoldersXX();
//		TaskerSVC.MakeFinaleLinks();
		
		TaskerSVC.stop();
		
		
		
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		
	}

}
