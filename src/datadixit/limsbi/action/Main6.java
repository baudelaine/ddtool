package datadixit.limsbi.action;

import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.ProjectSVC;
import datadixit.limsbi.svc.TaskerSVC;

public class Main6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TaskerSVC.start();

		ProjectSVC.addLocale("fr");
		
		TaskerSVC.stop();
		TaskerSVC.start();
		TaskerSVC.stop();

	}

}
