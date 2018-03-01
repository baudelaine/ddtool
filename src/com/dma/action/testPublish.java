package com.dma.action;

import com.dma.properties.ConfigProperties;
import com.dma.svc.FactorySVC;
import com.dma.svc.ProjectSVC;
import com.dma.svc.TaskerSVC;

public class testPublish {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TaskerSVC.start();
		String[] locales = {"en"};
		ProjectSVC.changePropertyFixIDDefaultLocale();
		FactorySVC.createPackage(ConfigProperties.PackageName, ConfigProperties.PackageName, ConfigProperties.PackageName, locales);
		FactorySVC.publishPackage(ConfigProperties.PackageName,"/content");
		TaskerSVC.stop();

	}

}
