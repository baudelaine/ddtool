package datadixit.limsbi.action;

import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.TaskerSVC;

public class Main4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TaskerSVC.start();

		FactorySVC.deleteNamespace("[DIMENSIONS]");
		FactorySVC.createNamespace("DIMENSIONS", "DATA");

		FactorySVC.createMeasureDimension("[DIMENSIONS]", "SDIDATA_MEASURES");
		
		FactorySVC.createMeasure("[DIMENSIONS].[SDIDATA_MEASURES]", "SDIDATA_COUNT", "[FINAL].[SDIDATA].[SDIDATA_COUNT]");
		
		FactorySVC.createDimension("[DIMENSIONS]", "S_PRODUCT");
		FactorySVC.createDimensionHierarchy("[DIMENSIONS].[S_PRODUCT]", "[FINAL].[S_PRODUCT]");
		
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]", "[FINAL].[S_BATCH]");
		FactorySVC.addDimensionHierarchy("[DIMENSIONS].[S_PRODUCT]", "[FINAL].[S_REQUEST]");
	
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE]", "[FINAL].[S_SAMPLE]");
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[S_PRODUCT].[S_REQUEST]", "[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_REQUEST]", "[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_SAMPLE]", "[FINAL].[S_SAMPLE]");

		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[S_PRODUCTID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[S_PRODUCTID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[PRODUCTDESC]");

		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");

		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");
		
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_REQUEST].[S_REQUESTID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_REQUEST].[S_REQUESTID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_REQUEST].[S_REQUESTID]");

		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_REQUEST].[S_SAMPLE].[S_SAMPLEID]");
		
		FactorySVC.createScopeRelationship("[DIMENSIONS].[S_PRODUCT]");

		FactorySVC.createTimeDimension("[FINAL].[SDIDATA].[CREATEDT]", "SDIDATA.CREATEDT", "CREATEDT");
		
		TaskerSVC.stop();
		TaskerSVC.start();
		TaskerSVC.stop();

	}

}
