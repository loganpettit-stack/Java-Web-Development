package edu.umsl.math.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.UnavailableException;

import edu.umsl.math.beans.Problem;

public class KeywordDao {

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
	///
	private PreparedStatement insertPkm;
	private PreparedStatement getKid;
	private PreparedStatement insertKeyword;
	private PreparedStatement getProblemsByKeyword;
	private PreparedStatement getProblemKeywords;
	
	public KeywordDao() throws Exception {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://192.168.64.2:3306/mathprobdb", "admin", "");

			insertPkm = connection.prepareStatement("INSERT INTO prob_keyword_mapping(keyid, probid) VALUES (? ,?) ");

			getKid = connection.prepareStatement("SELECT kid FROM keywords WHERE keyword = ? ");

			insertKeyword = connection.prepareStatement("INSERT INTO keywords(keyword) VALUES (?)");

			getProblemsByKeyword = connection
					.prepareStatement("SELECT pr.pid, pr.content, pr.order_num FROM problem pr,"
							+ " prob_keyword_mapping pkm WHERE pr.pid = pkm.probid AND pkm.keyid = ?");
			
			getProblemKeywords = connection.prepareStatement("SELECT kw.kid, kw.keyword FROM keywords kw, prob_keyword_mapping pkm WHERE"
					+ " kw.kid = pkm.pkid AND pkm.probid = ?");

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new UnavailableException(exception.getMessage());
		}
	}

	public void addProbKeyMap(String pid, String keyword) {

		boolean flag = false;
		int kid = -1;
		int numPid = Integer.parseInt(pid);
		int newKidnum = -1;

		try {
			getKid.setString(1, keyword);
			ResultSet rs = getKid.executeQuery();

			/* Get keyword Id if one exists */
			while (rs.next()) {
				kid = rs.getInt(1);
				if (kid != -1) {
					flag = true;
				}
			}

			if (flag) {

				System.out.println("found kid: " + kid);

				insertPkm.setInt(1, kid);
				insertPkm.setInt(2, numPid);
				insertPkm.executeUpdate();

			} else {
				System.out.println("keyword not found making one");

				insertKeyword.setString(1, keyword);
				insertKeyword.executeUpdate();

				getKid.setString(1, keyword);
				ResultSet newKid = getKid.executeQuery();

				while (newKid.next()) {
					newKidnum = newKid.getInt(1);
					System.out.println(newKidnum);
				}

				insertPkm.setInt(1, newKidnum);
				insertPkm.setInt(2, numPid);
				insertPkm.execute();

				System.out.println("Problem and keyword mapping created");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Problem> getProblemsByKeyword(String keyword) throws Exception {
		ProblemDao probdao = new ProblemDao();
		List<Problem> problist = new ArrayList<Problem>();
		List<String> keywordList = new ArrayList<String>();
		int kid = 0;

		StringTokenizer tokenizer = new StringTokenizer(keyword, ",");
		while (tokenizer.hasMoreTokens()) {
			keywordList.add(tokenizer.nextToken());
		}

		try {
			for (int i = 0; i < keywordList.size(); i++) {
				System.out.println(keywordList.get(i));

				String currentKeyword = keywordList.get(i);
				currentKeyword = currentKeyword.replaceAll("\\s", "");

				getKid.setString(1, currentKeyword);
				ResultSet rsKid = getKid.executeQuery();

				while (rsKid.next()) {
					kid = rsKid.getInt(1);
				}

				getProblemsByKeyword.setInt(1, kid);
				ResultSet rsProblist = getProblemsByKeyword.executeQuery();

				while (rsProblist.next()) {
					Problem prob = new Problem();
					
					int pid = rsProblist.getInt(1);
					
					prob.setPid(pid);
					prob.setContent(rsProblist.getString(2));
					prob.setOrder_num(rsProblist.getInt(3));
					
					prob.setCategoryList(probdao.getProblemCategories(pid));
					
//					List<String> probkwList = new ArrayList<String>();
//					probkwList = getKwList(pid);
//						
//					prob.setKeywordList(probkwList);
					
					problist.add(prob);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return problist;
	}
	
	
//	public List<String> getKwList(int pid) throws SQLException{
//		
//		getProblemKeywords.setInt(1, pid);
//		ResultSet probkw = getProblemKeywords.executeQuery();
//		
//		List<String> probkwList = new ArrayList<String>();
//		
//		while(probkw.next()) {
//			String kw = probkw.getString(2);
//
//			probkwList.add(kw);
//		}
//		
//		return probkwList;
//	}

}
