package edu.umsl.math.web;

import java.io.IOException;
import java.util.List;

import edu.umsl.math.beans.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.*;

/**
 * Servlet implementation class ListMathServlet
 */
@WebServlet("/listmath")
public class ListMathServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProblemDao probdao = null;
		CategoryDao catdao = null;
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");
	
		try {
			probdao = new ProblemDao();

			List<Problem> problist = probdao.getProblemList();

			request.setAttribute("problist", problist);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		
		try {
			catdao = new CategoryDao();

			List<Category> catlist = catdao.getCategoryList();

			request.setAttribute("catlist", catlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dispatcher.forward(request, response);
	}

}
