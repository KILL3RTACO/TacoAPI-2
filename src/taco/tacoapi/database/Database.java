package taco.tacoapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import taco.tacoapi.TacoAPI;

public class Database {

	private Connection conn;
	
	public Database() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {}
		connect();
	}
	
	private void connect() throws SQLException{
		conn = DriverManager.getConnection(getConnectionString());
	}
	
	private void ensureConnection(){
		try {
			if(!this.conn.isValid(5)){
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String getConnectionString(){
		return "jdbc:mysql://" + TacoAPI.config.getMySqlServerAddress() + ":" +
				TacoAPI.config.getMySqlServerPort() + "/" +
				TacoAPI.config.getDatabaseName() + "?user=" +
				TacoAPI.config.getDatabaseUsername() + "&password=" +
				TacoAPI.config.getDatabasePassword();
	}
	
	private PreparedStatement prepareStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement stmt = this.conn.prepareStatement(sql);
		int counter = 1;
		for (Object param : params) {
			if (param instanceof Integer) {
				stmt.setInt(counter++, (Integer) param);
			} else if (param instanceof Short) {
				stmt.setShort(counter++, (Short) param);
			} else if (param instanceof Long) {
				stmt.setLong(counter++, (Long) param);
			} else if (param instanceof Double) {
				stmt.setDouble(counter++, (Double) param);
			} else if (param instanceof String) {
				stmt.setString(counter++, (String) param);
			} else if (param == null) {
				stmt.setNull(counter++, Types.NULL);
			} else if (param instanceof Object) {
				stmt.setObject(counter++, param);
			} else {
				System.out.printf("Database -> Unsupported data type %s", param.getClass().getSimpleName());
			}
		}
		return stmt;
	}
	
	/**
	 * Read from the database
	 * @param sql the query to send
	 * @param params the parameters to be used
	 * @return
	 */
	public QueryResults read(String sql, Object... params) {
		ensureConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		QueryResults results = null;
		try {
			stmt = prepareStatement(sql, params);
			rs = stmt.executeQuery();
			if(rs != null){
				results = new QueryResults(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}
	
	/**
	 * Write to the database
	 * @param sql the query to send
	 * @param params the parameters to be used
	 */
	public void write(String sql, Object... params){
		try {
			ensureConnection();
			PreparedStatement stmt = prepareStatement(sql, params);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
