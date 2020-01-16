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

public class CategoryDao {

	private Connection connection;
	private PreparedStatement results;
	private PreparedStatement addCategory;
	private PreparedStatement getMaxCatId;
	private List<Category> currentCategoryList;

	public CategoryDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://192.168.64.2:3306/mathprobdb", "admin", "");

			results = connection
					.prepareStatement("SELECT category_id, category_type " + "FROM category ORDER BY category_id ASC");

			addCategory = connection.prepareStatement("INSERT INTO category(category_id, category_type) VALUES (?, ?)");

			getMaxCatId = connection.prepareStatement("SELECT MAX(category_id) FROM category");
			

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new UnavailableException(exception.getMessage());
		}

	}

	public List<Category> getCategoryList() throws SQLException {
		List<Category> catlist = new ArrayList<Category>();

		try {
			ResultSet resultsRS = results.executeQuery();

			while (resultsRS.next()) {
				Category cat = new Category();

				cat.setCategoryId(resultsRS.getInt(1));
				cat.setCategoryType(resultsRS.getString(2));

				catlist.add(cat);
			}
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}

		currentCategoryList = catlist;
		System.out.println(catlist);
		return catlist;
	}

	public boolean checkCatList(String newCategory) throws SQLException {
		currentCategoryList = getCategoryList();
		
		for (int i = 0; i < currentCategoryList.size(); i++) {
			if (currentCategoryList.get(i).getCategoryType().toLowerCase().equals(newCategory.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String addCategory(String categoryType) throws SQLException {
		try {

			if(categoryType.isEmpty()) {
				return "Category input was empty";
			}
			else if (!checkCatList(categoryType)) {
				/*
				 * Get max category Id parse it to int and increment it to add a new category
				 */
				ResultSet RSmaxCategoryId = getMaxCatId.executeQuery();
				RSmaxCategoryId.next();
				String maxCatId = RSmaxCategoryId.getString(1);
				int maxCatIdNum = Integer.parseInt(maxCatId);
				maxCatIdNum += 1;

				addCategory.setInt(1, maxCatIdNum);
				addCategory.setString(2, categoryType);
				addCategory.executeUpdate();
				
				System.out.println("Category added");
				return "Category added";
			} else {
				System.out.println("Category already exists, enter new category");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "Category already exists, enter new category";
	}

}
