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
import edu.umsl.math.dao.KeywordDao;


/**
 * Servlet implementation class keywordHandlerServlet
 */
@WebServlet("/keywordHandlerServlet")
public class keywordHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryDao catdao = null;
		KeywordDao keyworddao = null;
		RequestDispatcher dispatcher = request.getRequestDispatcher("list.jsp");

		String keyword = request.getParameter("keyword");
		System.out.println(keyword);
		
		try {
			keyworddao = new KeywordDao();
			
			List<Problem> problist = keyworddao.getProblemsByKeyword(keyword);
			
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
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String keyword = request.getParameter("problemKeyword");
		String pid = request.getParameter("keywordPid");
		System.out.println(pid);
		System.out.println(keyword);
		
		try {
			KeywordDao keywordDao = new KeywordDao();
			keywordDao.addProbKeyMap(pid, keyword);
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.sendRedirect(request.getContextPath());
	}

}
