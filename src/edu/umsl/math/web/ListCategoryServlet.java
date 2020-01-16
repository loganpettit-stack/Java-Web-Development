package edu.umsl.math.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.beans.Category;
import edu.umsl.math.beans.Problem;
import edu.umsl.math.dao.CategoryDao;
import edu.umsl.math.dao.ProblemDao;

/**
 * Servlet implementation class ListCategoryServlet
 */
@WebServlet("/ListCategory")
public class ListCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String category = request.getParameter("categoryButton");
		int categoryId = Integer.parseInt(category);
		ProblemDao probdao = null;
		CategoryDao catdao = null;
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("categoryList.jsp");

		
		try {
			probdao = new ProblemDao();

			List<Problem> problist = probdao.getProblemCategoryList(categoryId);

			request.setAttribute("problist", problist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
//		response.sendRedirect("categoryList");
	}

}
