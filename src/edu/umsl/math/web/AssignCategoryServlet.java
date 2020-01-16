package edu.umsl.math.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.umsl.math.dao.ProblemDao;


@WebServlet("/AssignCategoryServlet")
public class AssignCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String category = request.getParameter("category");
		String pid = request.getParameter("pid");
		int integerPid = Integer.parseInt(pid);
		int integerCategory = Integer.parseInt(category);
		
		System.out.println(integerCategory + " " + integerPid);
		
		try {
			ProblemDao prbDao = new ProblemDao();
			
			prbDao.addProblemtoCategory(integerPid, integerCategory);
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath());
	}

}
