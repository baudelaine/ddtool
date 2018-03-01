package com.dma.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetImportedKeysServlet
 */
@WebServlet("/GetModelList")
public class GetModelListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetModelListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		List<Object> result = new ArrayList<Object>();

		String realPath = getServletContext().getRealPath("/");
		System.out.println("realPath=" + realPath);
		File dir = new File(realPath + "/models/.");
		
		if(dir.exists()){
			File[] fs = dir.listFiles();
			
			Arrays.sort(fs, new Comparator<File>() {
			    public int compare(File f1, File f2) {
			        return Long.compare(f2.lastModified(), f1.lastModified());
			    }
			});			
			
			int i = 0;
			for(File f: fs){
				Map<String, String> m = new HashMap<String, String>();
				m.put("id", String.valueOf(i++));
				m.put("name", f.getName());
				result.add(m);
			}
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(Tools.toJSON(result));

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

