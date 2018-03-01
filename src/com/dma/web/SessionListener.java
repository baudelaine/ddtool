package com.dma.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCDataSource;

/**
 * Application Lifecycle Listener implementation class SessionListener
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener {

	// Value as to match the value in server.xml
	String jndiName = "";
	String schema = "";
	String query = "";
	boolean withRecCount = false;
	Map<String, QuerySubject> query_subjects = new HashMap<String, QuerySubject>();
	
    /**
     * Default constructor. 
     */
    public SessionListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
		System.out.println("Session " + arg0.getSession().getId() + " has been created...");
    	HttpSession s = arg0.getSession();
    	InitialContext ic = (InitialContext) s.getServletContext().getAttribute("ic");
    	
    	try {
    		
    		String cognosModelsPath = (String) ic.lookup("CognosModelsPath"); 
    		
    		String dbEngine = (String) ic.lookup("DBEngine");
    		if(dbEngine.equalsIgnoreCase("DB2400")){
				jndiName = "jdbc/ds2";
				schema = (String) ic.lookup("DB2400Schema");
				query = (String) ic.lookup("TestDB2400DBConnection");
			}
    		if(dbEngine.equalsIgnoreCase("DB2")){
				jndiName = "jdbc/ds1";
				schema = (String) ic.lookup("DB2Schema");
				query = (String) ic.lookup("TestDB2DBConnection");
			}

			if(dbEngine.equalsIgnoreCase("ORA")){
				jndiName = "jdbc/ds0";
				schema = (String) ic.lookup("ORASchema");
				query = (String) ic.lookup("TestORADBConnection");
			}    		
			
			Connection con = null;
//			if(dbEngine.equalsIgnoreCase("DB2400")){
//				Class.forName("com.ibm.as400.access.AS400JDBCDriver");
//				System.out.println("com.ibm.as400.access.AS400JDBCDriver loaded successfully !!!");
//				AS400JDBCConnection con400 = (AS400JDBCConnection) DriverManager.getConnection("jdbc:as400:");
				
//				AS400JDBCDataSource datasource = new AS400JDBCDataSource("172.16.2.70");
//				  datasource.setUser("IBMIIC");
//				  datasource.setPassword("spcspc");
//				  datasource.setDatabaseName("S6514BFA");
//				  con = datasource.getConnection();
//			}
//			else{
				DataSource ds = (DataSource) ic.lookup(jndiName);
				con = ds.getConnection();
//			}
			s.setAttribute("con", con);
			s.setAttribute("dbEngine", dbEngine);
			s.setAttribute("jndiName", jndiName);
			s.setAttribute("schema", schema);
			s.setAttribute("query", query);
			s.setAttribute("query_subjects", query_subjects);
			s.setAttribute("cognosModelsPath", cognosModelsPath);
			System.out.println("SessionId " + s.getId() + " is now connected to " + jndiName + " using shema " + schema);
			
			withRecCount = (Boolean) ic.lookup("WithRecCount");
			s.setAttribute("withRecCount", withRecCount);
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0)  { 
         // TODO Auto-generated method stub
    	HttpSession s = arg0.getSession();
    	InitialContext ic = (InitialContext) s.getServletContext().getAttribute("ic");
    	try {
    		query_subjects.clear();
			DataSource ds = (DataSource) ic.lookup(jndiName);
			Connection con = ds.getConnection();
			con.close();
			System.out.println("Connection to " + jndiName + " has been closed...");
			
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
		System.out.println("Session " + arg0.getSession().getId() + " has been destroyed...");
    }
	
}
