package datadixit.limsbi.action;

import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.TaskerSVC;


public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		TaskerSVC.start();

		try{

			
			
		TaskerSVC.init();
		TaskerSVC.Import();
		
		TaskerSVC.IICInitNameSpace();
		
		FactorySVC.copyQuerySubject("[PHYSICALUSED]", "[PHYSICAL].[S_SAMPLE]");
		
		FactorySVC.renameQuerySubject("[PHYSICALUSED].[S_SAMPLE]", "FINAL_S_SAMPLE");
		
		
		TaskerSVC.stop();
		
		
		
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
		
		
	}

}
