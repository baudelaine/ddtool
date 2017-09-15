package datadixit.limsbi.action;

import datadixit.limsbi.svc.TaskerSVC;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		TaskerSVC.start();

		try{

			
			
		TaskerSVC.init();
		TaskerSVC.Import();

		TaskerSVC.Security();

		TaskerSVC.Final();
		TaskerSVC.RefSDC();
		TaskerSVC.Translanguage();
		TaskerSVC.LinkTransLanguage();
		TaskerSVC.Final_T();
		TaskerSVC.RefSDC_T();
		TaskerSVC.M2M_T();

		TaskerSVC.Fields_T();
		TaskerSVC.MakeTrees();
		TaskerSVC.MakeMainFolders();
 
		TaskerSVC.MakeAllSubFoldersXX();
		TaskerSVC.MakeFinaleLinks();
		
		TaskerSVC.stop();
		
		
		
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		
	}

}
