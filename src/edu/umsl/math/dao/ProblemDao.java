package edu.umsl.math.dao;

import edu.umsl.math.beans.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.UnavailableException;

public class ProblemDao {

	private Connection connection;
	private PreparedStatement results;
	private PreparedStatement insertPCM;
	private PreparedStatement getProblemsByCategory;
	private PreparedStatement getMaxOrderNum;
	private PreparedStatement addNewMathProblem;
	private PreparedStatement getPid;
	private PreparedStatement getProblemCategories;
	private PreparedStatement getAllCategories;
	private PreparedStatement getAllpcm;

	public ProblemDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://192.168.64.2:3306/mathprobdb", "admin", "");

			results = connection
					.prepareStatement("SELECT pid, content, order_num " + "FROM problem ORDER BY order_num DESC");

			/* Query to add insert row to prob_cat_mapping */
			insertPCM = connection.prepareStatement("INSERT INTO prob_cat_mapping(prob_id, cat_id) VALUES (?, ?) ");

			/* Query to get problem list by category */
			getProblemsByCategory = connection.prepareStatement("SELECT pr.pid, pr.content, pr.order_num "
					+ "FROM problem pr, prob_cat_mapping pcm " + "WHERE pr.pid = pcm.prob_id AND pcm.cat_id = ?");

			getMaxOrderNum = connection.prepareStatement("SELECT MAX(order_num) FROM problem");

			addNewMathProblem = connection.prepareStatement("INSERT INTO problem(content, order_num) VALUES (?, ?)");

			getPid = connection.prepareStatement("SELECT pid FROM problem WHERE order_num = ?");

			getProblemCategories = connection.prepareStatement(
					"SELECT cat.category_id, cat.category_type FROM" + " category cat, prob_cat_mapping pcm"
							+ " WHERE cat.category_id = pcm.cat_id AND pcm.prob_id = ?");

			getAllCategories = connection.prepareStatement("SELECT * FROM category");

			getAllpcm = connection.prepareCall("SELECT * FROM prob_cat_mapping");

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new UnavailableException(exception.getMessage());
		}

	}

	public List<Problem> getProblemList() throws Exception {
		List<Problem> problist = new ArrayList<Problem>();

		try {
			ResultSet resultsRS = results.executeQuery();

			while (resultsRS.next()) {
				Problem prob = new Problem();

				int pid = resultsRS.getInt(1);

				prob.setPid(pid);
				prob.setContent(resultsRS.getString(2));
				prob.setOrder_num(resultsRS.getInt(3));

				List<String> probcatlist = getProblemCategories(pid);
				prob.setCategoryList(probcatlist);
				
//				unused feature
//				KeywordDao kwDao = new KeywordDao();
//				List<String> kwList = new ArrayList<String>();
//				kwList = kwDao.getKwList(pid);
//				prob.setKeywordList(kwList);
				
				/* Unused feature */
//				List<Category> availcatlist = getAvailableCategories(probcatlist);
//				prob.setAvailableCategories(availcatlist);

				problist.add(prob);
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		return problist;
	}

	public void addProblemtoCategory(int prob_id, int cat_id) throws SQLException {
		System.out.println(prob_id + " " + cat_id);
		boolean flag = true;

		ResultSet rs = getAllpcm.executeQuery();

		try {
			List<Integer> probids = new ArrayList<>();
			List<Integer> catids = new ArrayList<>();

			while (rs.next()) {

				int probid = rs.getInt(2);
				int catid = rs.getInt(3);

				probids.add(probid);
				catids.add(catid);
			}

			for (int i = 0; i < probids.size(); i++) {
				System.out.println(probids.get(i) + " " + catids.get(i));
				if (probids.get(i).equals(prob_id) && catids.get(i).equals(cat_id)) {
					flag = false;
				}
			}

			if (flag) {
				/* Set parameters in query string */
				insertPCM.setInt(1, prob_id);
				insertPCM.setInt(2, cat_id);

				/* Execute query string */
				insertPCM.executeUpdate();

				System.out.println(prob_id + " Problem added to prob_cat_mapping to category " + cat_id);
			} else {
				System.out.println("Problem already in category");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Problem> getProblemCategoryList(int category) throws SQLException {
		List<Problem> probList = new ArrayList<>();

		try {
			/* Set parameters for query */
			getProblemsByCategory.setInt(1, category);

			/* Capture result set after query and store each row into problem */
			ResultSet RS = getProblemsByCategory.executeQuery();

			while (RS.next()) {
				Problem prob = new Problem();

				int pid = RS.getInt(1);
				prob.setPid(pid);
				prob.setContent(RS.getString(2));
				prob.setOrder_num(RS.getInt(3));

				prob.setCategoryList(getProblemCategories(pid));
				probList.add(prob);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return probList;
	}

	public void addMathProblem(String content, int categoryId) throws SQLException {
		try {

			ResultSet stringMaxOrderNum = getMaxOrderNum.executeQuery();
			System.out.println(stringMaxOrderNum);
			stringMaxOrderNum.next();

			String smaxOrderNum = stringMaxOrderNum.getString(1);
			int maxOrderNum = Integer.parseInt(smaxOrderNum);

			maxOrderNum += 1;

			System.out.println(maxOrderNum);
			/*
			 * Add math problem with content and ordernum that is 1 more than the current
			 * max order num
			 */
			addNewMathProblem.setString(1, content);
			addNewMathProblem.setInt(2, maxOrderNum);
			addNewMathProblem.executeUpdate();

			/*
			 * get the generated Pid from the newly added math problem from searching by
			 * ordernum
			 */
			getPid.setInt(1, maxOrderNum);
			ResultSet pid = getPid.executeQuery();
			pid.next();
			int problemId = pid.getInt(1);

			/*
			 * Insert the math problem Pid and provided category id into the prob cat
			 * mapping
			 */
			insertPCM.setInt(1, problemId);
			insertPCM.setInt(2, categoryId);
			insertPCM.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getProblemCategories(int pid) {
		List<String> problemCatList = new ArrayList<>();

		try {
			/* Set parameters for query */
			getProblemCategories.setInt(1, pid);

			/* Capture result set after query and store each row into problem */
			ResultSet RScategories = getProblemCategories.executeQuery();

			while (RScategories.next()) {
				Category cat = new Category();

				String numb = RScategories.getString(1);
				String category = RScategories.getString(2);
				int num = Integer.parseInt(numb);

				cat.setCategoryType(category);
				cat.setCategoryId(num);

				problemCatList.add(cat.getCategoryType());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return problemCatList;
	}

	public List<Category> getAvailableCategories(List<String> currentCats) throws SQLException {
		List<Category> availableCategories = new ArrayList<>();
		List<Category> allCategories = new ArrayList<>();

		ResultSet RScategories = getAllCategories.executeQuery();

		while (RScategories.next()) {
			Category cat = new Category();

			String numb = RScategories.getString(1);
			String category = RScategories.getString(2);
			int num = Integer.parseInt(numb);

			cat.setCategoryId(num);
			cat.setCategoryType(category);

			allCategories.add(cat);
		}

		for (int i = 0; i < allCategories.size(); i++) {

			for (int j = 0; j < currentCats.size(); j++) {
				if (allCategories.get(i).getCategoryType().equals(currentCats.get(j))) {
					allCategories.remove(i);
				}
			}
		}

		return allCategories;
	}

}
