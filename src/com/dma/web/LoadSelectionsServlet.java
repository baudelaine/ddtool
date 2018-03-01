package com.dma.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class GetSelectionsServlet
 */
@WebServlet(name = "LoadSelections", urlPatterns = { "/LoadSelections" })
public class LoadSelectionsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	List<Relation> selections; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadSelectionsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		selections = (List<Relation>) request.getSession().getAttribute("selections");

		selections.clear();
		
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        
        if(br != null){
            while((json = br.readLine()) != null){
	            System.out.println("json="+json);
	
		        Relation fk = mapper.readValue(json, Relation.class);
		        
		        
		        selections.add(fk);
            }
        }
        
        System.out.println("selections=" + selections);
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
