package datadixit.limsbi.action;

import datadixit.limsbi.svc.CognosSVC;
import datadixit.limsbi.svc.ProjectSVC;


public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		try{

			CognosSVC.logon();
			ProjectSVC.createModelIIC();
		
		
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		
	}

}
