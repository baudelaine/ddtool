package com.dma.action;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dma.properties.ConfigProperties;
import com.dma.svc.CognosSVC;
import com.dma.svc.FactorySVC;
import com.dma.svc.ProjectSVC;
import com.dma.svc.TaskerSVC;

public class Main6 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TaskerSVC.start();
		String[] locales = {"en"};
/*
//		ProjectSVC.addLocale("fr");
		FactorySVC.deleteNamespace("[PHYSICAL]");
		FactorySVC.deleteNamespace("[PHYSICALUSED]");
//		FactorySVC.deleteNamespace("[FINAL]");
//		FactorySVC.deleteNamespace("[REF]");
//		FactorySVC.deleteNamespace("[DATA]");
		FactorySVC.deleteNamespace("[AUTOGENERATION]");
		FactorySVC.deleteDataSource("[].[dataSources].[DEV]");
		ProjectSVC.removeLocale("en");

//		FactorySVC.createNamespace("TESTSYNCHRO", "DATA");
//		FactorySVC.deleteNamespace("[TESTSYNCHRO]");

*/
		ProjectSVC.changePropertyFixIDDefaultLocale();
//		FactorySVC.createPackage("DDToolTip", "DDToolTip description", "DDToolTip Screen Tip", locales);
//		FactorySVC.publishPackage("DDToolTip","/content");

		FactorySVC.createPackage(ConfigProperties.PackageName, ConfigProperties.PackageName, ConfigProperties.PackageName, locales);
		FactorySVC.publishPackage(ConfigProperties.PackageName,"/content");

		TaskerSVC.stop(); 
		
//		String objectType = "querySubject"; //queryItem , queryItemFolder
//		String objectPath = "[DATA].[S_SAMPLE]";
//		String objectToolTip = "the SAMPLE !";l
//
//		FactorySVC.createToolTip(objectType, objectPath, objectToolTip);
		
//		FactorySVC.changeQueryItemProperty("[DATA].[S_SAMPLE].[S_SAMPLEID1]", "usage", "identifier");
		
	}

}
