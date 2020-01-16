package edu.umsl.math.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.ProblemDao;

/**
 * Servlet implementation class AddProblemServlet
 */
@WebServlet("/addMathProblem")
public class AddProblemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProblemDao probdao = null;
		
		String probcat = request.getParameter("problemCategory");
		String mathprob = request.getParameter("newMathProblem");
		int catid = Integer.parseInt(probcat);
		
		System.out.println(mathprob + " " + catid);
		
		try {
			probdao = new ProblemDao();
			
			probdao.addMathProblem(mathprob, catid);
			
		} catch (Exception e) {
			
			System.out.println(e);
			e.printStackTrace();
		}
		
		System.out.println("Problem inserted");
		response.sendRedirect(request.getContextPath());
	}

}
