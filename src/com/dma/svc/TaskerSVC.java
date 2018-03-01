package com.dma.svc;

import java.util.List;

import com.dma.web.RelationShip;

import sapphire.util.Logger;

public class TaskerSVC {

	static {
	}

	public static void start() {
		CognosSVC.logon();
		ProjectSVC.openModel();
		ProjectSVC.setLocale();
	}

	public static void stop() {

		ProjectSVC.saveModel();
		ProjectSVC.closeModel();
		CognosSVC.logoff();
		System.out.println("END COGNOS API");
	}
	

	public static void init() {

	}

	public static void Import() {
		FactorySVC.DBImport("PHYSICAL", "DEV", "DANONE");
	}

	public static void IICInitNameSpace() {
		
		
		try {
			FactorySVC.createNamespace("PHYSICAL", "Model");
			FactorySVC.createNamespace("PHYSICALUSED", "Model");
			FactorySVC.createNamespace("AUTOGENERATION", "Model");
			FactorySVC.createNamespace("FINAL", "AUTOGENERATION");
			FactorySVC.createNamespace("REF", "AUTOGENERATION");
			FactorySVC.createNamespace("DATA", "AUTOGENERATION");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			FactorySVC.deleteNamespace("[PHYSICAL]");
			FactorySVC.deleteNamespace("[PHYSICALUSED]");
			FactorySVC.deleteNamespace("[AUTOGENERATION]");
			FactorySVC.deleteDataSource("[].[dataSources].[DEV]");   // Ajout Nico à déplacer
			ProjectSVC.removeLocale("en");   // Ajout Nico à déplacer
			FactorySVC.createNamespace("PHYSICAL", "Model");
			FactorySVC.createNamespace("PHYSICALUSED", "Model");
			FactorySVC.createNamespace("AUTOGENERATION", "Model");
			FactorySVC.createNamespace("FINAL", "AUTOGENERATION");
			FactorySVC.createNamespace("REF", "AUTOGENERATION");
			FactorySVC.createNamespace("DATA", "AUTOGENERATION");
		}
	}
	
	public static void IICCreateRelation(List<RelationShip> list){
		for(RelationShip rs: list){
			FactorySVC.createRelationship(rs);
		}
	}
	
	public static void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}

}
