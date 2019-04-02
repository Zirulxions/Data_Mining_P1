package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Connection.DataBase;
import Connection.PropsManager;

@WebServlet("/Filter")
public class Filter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataBase conn = new DataBase();
       
    public Filter() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("application/json");
    	try {
			getInformation(conn.getConnection(), request, response);
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
    }
    
	private void getInformation(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		JSONObject jsonRet = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		PropsManager props = new PropsManager();
		PrintWriter out = response.getWriter();
		PreparedStatement stat = null;
		ResultSet res = null;
		String[] wantedWord = new String[5];
		Integer[] wantedTimes = new Integer[5];
		Integer[] wantedId = new Integer[5];
		Integer i = 0;
		try {
			String moreWanted = props.getValue("query_MoreWanted");
			stat = connection.prepareStatement(moreWanted);
			res = stat.executeQuery();
			while(res.next()) {
				wantedWord[i] = res.getString("data1");
				wantedTimes[i] = res.getInt("search_times");
				wantedId[i] = res.getInt("id");
				i++;
			}
			stat.close();
			res.close();
			connection.close();
			jsonArr.put(wantedWord).put(wantedTimes).put(wantedId);
			jsonRet.put("status", 200).put("message", "OK.").put("resultList", i).put("array", jsonArr);
			out.print(jsonRet);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
			jsonRet.put("status", 500).put("message", "Something is wrong. Try Reloading the Page.");
			out.print(jsonRet);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			executeConn(conn.getConnection(), request, response);
		} catch (SQLException | JSONException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private void executeConn(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JSONException, NoSuchAlgorithmException, SQLException {
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		JSONArray jsonArr = new JSONArray();
		PropsManager prop = PropsManager.getInstance();
		PrintWriter out = response.getWriter();
		JSONObject jsonRet = new JSONObject();
		PreparedStatement stat = null;
		Integer counter = 0;
		String[] wordsFound;
		Integer[] foundTimes;
		String valueSend = reqBody.getString("dataTrick");
		ResultSet res = null;
		try {
			stat = connection.prepareStatement(prop.getValue("query_SelectCount"));
			stat.setString(1, valueSend);
			res = stat.executeQuery();
			if(res.next()) {
				counter = res.getInt("count");
			}
			stat = null;
			wordsFound = new String[counter];
			foundTimes = new Integer[counter];
			stat = connection.prepareStatement(prop.getValue("query_Select"));
			stat.setString(1, valueSend);
			res = stat.executeQuery();
			Integer i = 0;
			while(res.next()) {
				wordsFound[i] = res.getString("data1");
				foundTimes[i] = res.getInt("search_times");
				i++;
			}
			stat = null;
			stat = connection.prepareStatement(prop.getValue("query_UpdateWanted"));
			stat.setString(1, valueSend);
			stat.executeUpdate();
			jsonArr.put(wordsFound).put(foundTimes);
			jsonRet.put("status",200).put("message","Word Found Times: " + counter).put("arraysFound", jsonArr);
			out.print(jsonRet.toString());
			stat.close();
			res.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
			jsonRet.put("status", 500).put("message", "Somethig is not good. Reload the page or try another word.");
			out.print(jsonRet);
		} 		
	}
}
