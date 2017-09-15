package datadixit.limsbi.action;

import datadixit.limsbi.svc.TaskerSVC;
import sapphire.SapphireException;
import sapphire.accessor.ActionProcessor;
import sapphire.accessor.QueryProcessor;
import sapphire.action.BaseAction;
import sapphire.util.Logger;
import sapphire.xml.PropertyList;

public class BuildModel extends BaseAction {

	private QueryProcessor qp;
	private ActionProcessor ap;
	PropertyList pl;

	@Override
	public void processAction(PropertyList props) throws SapphireException {
		qp = this.getQueryProcessor();
		ap = this.getActionProcessor();
		pl = props;

		lg(" BuildModel START ");

		long start = System.currentTimeMillis();

		TaskerSVC.start();
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

		// TaskerSVC.publish();

		TaskerSVC.stop();

		// ! \\ THE SEQUENCE PROVIDED BY COLUMN_SEQUENCE IS NOT RELIABLE TO
		// ORDER FOLDERS
		// TaskerSVC.MakeTrees();

		long end = System.currentTimeMillis();

		lg(" program took " + (end - start) / (1000 * 60) + " min");

		lg(" BuildModel END ");
	}

	public void lg(String msg) {
		Logger.logInfo(" BuildModel.java ", msg);
	}
}
