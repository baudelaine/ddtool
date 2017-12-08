package datadixit.limsbi.action;

import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.ProjectSVC;
import datadixit.limsbi.svc.TaskerSVC;

public class Main5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TaskerSVC.start();

		FactorySVC.deleteNamespace("[DIMENSIONS]");
		FactorySVC.createNamespace("DIMENSIONS", "DATA");

		FactorySVC.createFolder("[DIMENSIONS]", ".SDIDATA");
		
		FactorySVC.createMeasureDimension("[DIMENSIONS].[.SDIDATA]", "SDIDATA_MEASURES");
		
		FactorySVC.createMeasure("[DIMENSIONS].[SDIDATA_MEASURES]", "SDIDATA_COUNT", "[FINAL].[SDIDATA].[SDIDATA_COUNT]");
		
		
		//Paramlist dimension
		FactorySVC.createDimension("[DIMENSIONS].[.SDIDATA]", "PARAMLIST");
		FactorySVC.createDimensionHierarchy("[DIMENSIONS].[PARAMLIST]", "[FINAL].[INSTRUMENTTYPE]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[INSTRUMENTTYPE].[INSTRUMENTTYPEDESC]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[INSTRUMENTTYPE].[INSTRUMENTTYPEDESC]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[INSTRUMENTTYPE].[INSTRUMENTTYPEDESC]");

		FactorySVC.createScopeRelationship("[DIMENSIONS].[PARAMLIST]");
		
		FactorySVC.addDimensionHierarchy("[DIMENSIONS].[PARAMLIST]", "[FINAL].[PARAMLIST].[U_CATEGORY]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[U_CATEGORY].[U_CATEGORY]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[U_CATEGORY].[U_CATEGORY]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[U_CATEGORY].[U_CATEGORY]");

		FactorySVC.addHierarchyLevel("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE]", "[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[INSTRUMENTTYPE]", "[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[PARAMLIST]", "[FINAL].[PARAMLIST]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[PARAMLIST].[PARAMLISTID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[PARAMLIST].[PARAMLISTDESC]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[PARAMLIST].[INSTRUMENTTYPE].[PARAMLIST].[PARAMLISTDESC]");

		FactorySVC.addHierarchyLevel("[DIMENSIONS].[PARAMLIST].[U_CATEGORY]", "[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[U_CATEGORY]", "[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[PARAMLIST]", "[FINAL].[PARAMLIST]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[PARAMLIST].[PARAMLISTID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[PARAMLIST].[PARAMLISTDESC]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[PARAMLIST].[U_CATEGORY].[PARAMLIST].[PARAMLISTDESC]");

		//Sample measure
		FactorySVC.createFolderInFolder("[DIMENSIONS]", ".SDIDATA", ".S_SAMPLE");
		FactorySVC.createMeasureDimension("[DIMENSIONS].[.S_SAMPLE]", "S_SAMPLE_MEASURES");
		FactorySVC.createMeasure("[DIMENSIONS].[S_SAMPLE_MEASURES]", "S_SAMPLE_COUNT", "[FINAL].[S_SAMPLE].[S_SAMPLE_COUNT]");
		
//		FactorySVC.adjustScopeRelationship("[DIMENSIONS].[S_SAMPLE_MEASURES]", "[DIMENSIONS].[S_SAMPLE_MEASURES].[S_SAMPLE_COUNT]", "[DIMENSIONS].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]");

		//Product dimension
		FactorySVC.createDimension("[DIMENSIONS].[.S_SAMPLE]", "S_PRODUCT");
		FactorySVC.createDimensionHierarchy("[DIMENSIONS].[S_PRODUCT]", "[FINAL].[S_PRODUCT]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[S_PRODUCTID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[PRODUCTDESC]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT].[PRODUCTDESC]");
		FactorySVC.createScopeRelationship("[DIMENSIONS].[S_PRODUCT]");
		
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]", "[FINAL].[S_BATCH]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH].[S_BATCHID]");
	
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_BATCH]", "[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE]", "[FINAL].[S_SAMPLE]");
		FactorySVC.createDimensionRole_BK("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[S_PRODUCT].[S_PRODUCT].[S_SAMPLE].[S_SAMPLEID]");

		FactorySVC.createTimeDimension("[FINAL].[SDIDATA].[CREATEDT]", "SDIDATA.CREATEDT", "CREATEDT");
		FactorySVC.createTimeDimension("[FINAL].[S_SAMPLE].[CREATEDT]", "S_SAMPLE.CREATEDT", "CREATEDT");
		
		TaskerSVC.stop();
		TaskerSVC.start();
		TaskerSVC.stop();

	}

}
