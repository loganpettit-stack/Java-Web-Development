package edu.umsl.math.web;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.CategoryDao;
import edu.umsl.math.dao.ProblemDao;

/**
 * Servlet implementation class AddCategoryServlet
 */
@WebServlet("/addCategory")
public class AddCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private CategoryDao catdao;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryDao catdao = null;
		String message = "";
		String categoryType = request.getParameter("newCategory");
		
		System.out.println("New category type to add: " + categoryType);
		
		try {
			catdao = new CategoryDao();
		
			message = catdao.addCategory(categoryType);
		
			
		} catch (Exception e) {
			
			System.out.println(e);
			e.printStackTrace();
		}
		
//		response.sendRedirect(request.getContextPath());
		response.sendRedirect("listmath?addCatMessage=" + URLEncoder.encode(message, "UTF-8"));
	}

}
