package datadixit.limsbi.action;

import datadixit.limsbi.svc.FactorySVC;
import datadixit.limsbi.svc.TaskerSVC;

public class Main3 {

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

// time dimension
		String timeDimensionField = "[FINAL].[SDIDATA].[CREATEDT]";
		String dateQueryItemName = "CREATEDT";
		String dimensionName = "SDIDATA.CREATEDT";	
		
		FactorySVC.createDimension("[DIMENSIONS]", "SDIDATA.CREATEDT");

// hierarchy (By month)
		FactorySVC.createDimensionHierarchy("[DIMENSIONS].[SDIDATA.CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.createScopeRelationship("[DIMENSIONS].[SDIDATA.CREATEDT]");
	
// level year
		FactorySVC.modifyHierarchyName("[DIMENSIONS].[SDIDATA.CREATEDT]", "CREATEDT","CREATEDT (By month)");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "YEAR");
		String exp = "_year([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR]", "CREATEDT", "YEAR_KEY", exp);
		exp = "_year ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR]", "YEAR", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR].[YEAR]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR].[YEAR]");

//level quarter	
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "QUARTER");
		exp = "if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (1,2,3)) then (1)  else (\n" + 
		"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (4,5,6)) then (2)  else (\n" + 
		"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (7,8,9)) then (3)  else (\n" + 
		"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (10,11,12)) then (4)  else (0)\n" + 
		")))";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[QUARTER]", "CREATEDT", "QUARTER_KEY", exp);
		exp = "_year ([FINAL].[SDIDATA].[CREATEDT]) + '/' +\n" + 
				"cast (if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (1,2,3)) then (1)  else (\n" + 
				"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (4,5,6)) then (2)  else (\n" + 
				"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (7,8,9)) then (3)  else (\n" + 
				"if (_month (  [FINAL].[SDIDATA].[CREATEDT] ) in (10,11,12)) then (4)  else (0)\n" + 
				"))),varchar)";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[QUARTER]", "QUARTER", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[QUARTER].[QUARTER]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[QUARTER].[QUARTER]");

// level month
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[QUARTER]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "MONTH");
		exp = "_month ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[MONTH]", "CREATEDT", "MONTH_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(7))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[MONTH]", "MONTH", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[MONTH].[MONTH]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[MONTH].[MONTH]");

// level day
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[MONTH]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "DAY");
		exp =  "_day ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DAY]", "CREATEDT", "DAY_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(10))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DAY]", "DAY", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DAY].[DAY]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DAY].[DAY]");
						
// level AM/PM
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DAY]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "AM/PM");
		exp = "if (_hour (  [FINAL].[SDIDATA].[CREATEDT] ) in_range {0:11}) then (1)  else (2)";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[AM/PM]", "CREATEDT", "AM/PM_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(10)) + ' ' +\n" + 
				"if (_hour (  [FINAL].[SDIDATA].[CREATEDT] ) in_range {0:11}) then ('AM')  else ('PM')";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[AM/PM]", "AM/PM", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[AM/PM].[AM/PM]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[AM/PM].[AM/PM]");

// level hour
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[AM/PM]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "HOUR");
		exp = "_hour (  [FINAL].[SDIDATA].[CREATEDT] )";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[HOUR]", "CREATEDT", "HOUR_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(13))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[HOUR]", "HOUR", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[HOUR].[HOUR]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[HOUR].[HOUR]");

// level date
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[HOUR]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)]", "CREATEDT", "DATE");
		exp = "[FINAL].[SDIDATA].[CREATEDT]";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DATE]", "CREATEDT", "DATE_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(19))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DATE]", "DATE", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DATE].[DATE]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[DATE].[DATE]");

		
// hierarchy by week
		FactorySVC.addDimensionHierarchy("[DIMENSIONS].[SDIDATA.CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyHierarchyName("[DIMENSIONS].[SDIDATA.CREATEDT]", "CREATEDT","CREATEDT (By week)");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "YEAR");
		exp = "_year([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[YEAR]", "CREATEDT", "YEAR_KEY", exp);
		exp = "_year ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[YEAR]", "YEAR", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR].[YEAR]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By month)].[YEAR].[YEAR]");

//level week	
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[YEAR]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "WEEK");
		exp = "_week_of_year ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[WEEK]", "CREATEDT", "WEEK_KEY", exp);
		exp = "cast(_year ([FINAL].[SDIDATA].[CREATEDT]) ,varchar(4))+ '/' +\n" + 
				"if (_week_of_year ([FINAL].[SDIDATA].[CREATEDT]) < 10 ) then ('0') else ('') + \n" + 
				"cast(_week_of_year ([FINAL].[SDIDATA].[CREATEDT]),varchar(2))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[WEEK]", "WEEK", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[WEEK].[WEEK]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[WEEK].[WEEK]");

//level day_of_week	
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[WEEK]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "DAY_OF_WEEK");
		exp = "_week_of_year ([FINAL].[SDIDATA].[CREATEDT])";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DAY_OF_WEEK]", "CREATEDT", "DAY_OF_WEEK_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(10)) + ' ' +\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 1)\n" + 
				"then ('Mon') else (\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 2)\n" + 
				"then ('Tue') else (\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 3)\n" + 
				"then ('Wed') else (\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 4)\n" + 
				"then ('Thu') else (\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 5)\n" + 
				"then ('Fri') else (\n" + 
				"if (_day_of_week ([FINAL].[SDIDATA].[CREATEDT],1) = 6)\n" + 
				"then ('Sat') else ('Sun')\n" + 
				")))))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DAY_OF_WEEK]", "DAY_OF_WEEK", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DAY_OF_WEEK].[DAY_OF_WEEK]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DAY_OF_WEEK].[DAY_OF_WEEK]");

// level AM/PM
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DAY_OF_WEEK]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "AM/PM");
		exp = "if (_hour (  [FINAL].[SDIDATA].[CREATEDT] ) in_range {0:11}) then (1)  else (2)";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[AM/PM]", "CREATEDT", "AM/PM_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(10)) + ' ' +\n" + 
				"if (_hour (  [FINAL].[SDIDATA].[CREATEDT] ) in_range {0:11}) then ('AM')  else ('PM')";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[AM/PM]", "AM/PM", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[AM/PM].[AM/PM]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[AM/PM].[AM/PM]");

// level hour
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[AM/PM]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "HOUR");
		exp = "_hour (  [FINAL].[SDIDATA].[CREATEDT] )";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[HOUR]", "CREATEDT", "HOUR_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(13))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[HOUR]", "HOUR", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[HOUR].[HOUR]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[HOUR].[HOUR]");

// level date
		FactorySVC.addHierarchyLevel("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[HOUR]", "[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[CREATEDT]", "[FINAL].[SDIDATA].[CREATEDT]");
		FactorySVC.modifyLevelName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)]", "CREATEDT", "DATE");
		exp = "[FINAL].[SDIDATA].[CREATEDT]";
		FactorySVC.modifyLevelQueryItemName("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DATE]", "CREATEDT", "DATE_KEY", exp);
		exp = "cast ([FINAL].[SDIDATA].[CREATEDT],varchar(19))";
		FactorySVC.createHierarchyLevelQueryItem("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DATE]", "DATE", exp);
		FactorySVC.createDimensionRole_MC("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DATE].[DATE]");
		FactorySVC.createDimensionRole_MD("[DIMENSIONS].[SDIDATA.CREATEDT].[CREATEDT (By week)].[DATE].[DATE]");
		
		TaskerSVC.stop();
		TaskerSVC.start();
		TaskerSVC.stop();

	}

}
