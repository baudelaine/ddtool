package com.baudelaine.dd;

import java.sql.Connection;
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
    		
    		String dbEngine = (String) ic.lookup("DBEngine");
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
			
			DataSource ds = (DataSource) ic.lookup(jndiName);
			Connection con = ds.getConnection();
			s.setAttribute("con", con);
			s.setAttribute("jndiName", jndiName);
			s.setAttribute("schema", schema);
			s.setAttribute("query", query);
			s.setAttribute("query_subjects", query_subjects);
			System.out.println("SessionId " + s.getId() + " is now connected to " + jndiName + " using shema " + schema);
			
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
