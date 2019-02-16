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
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			validateLogin(conn.getConnection(), request, response);
		} catch (SQLException | JSONException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private void validateLogin(Connection connection, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JSONException, NoSuchAlgorithmException, SQLException {
		JSONObject reqBody = new JSONObject(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		PropsManager prop = PropsManager.getInstance();
		PrintWriter out = response.getWriter();
		JSONObject jsonRet = new JSONObject();
		PreparedStatement stat = null;
		Integer counter = 0;
		try {
			String selectQuery = prop.getValue("query_Select");
			String valueSend = reqBody.getString("dataTrick");
			stat = connection.prepareStatement(selectQuery);
			stat.setString(1, valueSend);
			ResultSet res = stat.executeQuery();
			while(res.next()) {
				String newString = res.getString("data1");
				if(newString != null) {
					counter++;
				}
			}
			jsonRet.put("status",200).put("message","Word Found Times: " + counter);
			out.print(jsonRet.toString());
			stat.close();
			res.close();
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} 		
	}
}
