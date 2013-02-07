package taco.tacoapi.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResults {

	private ResultSet set;
	
	public QueryResults(ResultSet set) {
		this.set = set;
	}
	
	/**
	 * Gets a boolean from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the boolean found
	 * @throws SQLException
	 */
	public boolean getBoolean(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getBoolean(columnName);
	}

	/**
	 * Gets a double from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the double found
	 * @throws SQLException
	 */
	public double getDouble(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getDouble(columnName);
	}
	
	/**
	 * Gets a float from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the float found
	 * @throws SQLException
	 */
	public float getFloat(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getFloat(columnName);
	}
	
	/**
	 * Gets an int from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the int found
	 * @throws SQLException
	 */
	public int getInteger(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getInt(columnName);
	}
	
	/**
	 * Gets a long from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the long found
	 * @throws SQLException
	 */
	public long getLong(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getLong(columnName);
	}
	
	/**
	 * Gets a short from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the short found
	 * @throws SQLException
	 */
	public short getShort(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getShort(columnName);
	}
	
	/**
	 * Gets a String from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the String found
	 * @throws SQLException
	 */
	public String getString(int index, String columnName) throws SQLException {
		set.first();
		for(int i=0; i<index; i++){
			set.next();
		}
		return set.getString(columnName);
	}
	
	/**
	 * Test whether this query has rows or not
	 * @return true if the row count is > 0
	 */
	public boolean hasRows(){
		return rowCount() > 0;
	}
	
	/**
	 * Gets the amount of rows in the query
	 * @return the amount of rows 
	 */
	public int rowCount(){
		int rows = 0;
		try {
			set.first();
			while(set.next()) rows++;
		} catch (SQLException e) {}
		return rows;
		
	}

}
