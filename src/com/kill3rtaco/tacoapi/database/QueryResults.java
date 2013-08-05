package com.kill3rtaco.tacoapi.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class QueryResults {

	private ArrayList<String> columns;
	private ArrayList<ArrayList<Object>> values;
	
	public QueryResults(ResultSet set) {
		columns = new ArrayList<String>();
		values = new ArrayList<ArrayList<Object>>();
		try{
			for(int i=1; i<=set.getMetaData().getColumnCount(); i++){
				columns.add(set.getMetaData().getColumnName(i));
			}
			while(set.next()){
				ArrayList<Object> row  = new ArrayList<Object>();
				for(String column : columns){
					row.add(set.getObject(column));
				}
				values.add(row);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a boolean from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the boolean found
	 * @throws DatabaseException
	 */
	public boolean getBoolean(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof Boolean){
			return (boolean) obj;
		}else{
			return false;
		}
	}

	/**
	 * Gets a double from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the double found
	 * @throws DatabaseException
	 */
	public double getDouble(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof Double){
			return (double) obj;
		}else{
			return 0D;
		}
	}
	
	/**
	 * Gets a float from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the float found
	 * @throws DatabaseException
	 */
	public float getFloat(int index, String columnName) throws DatabaseException{
		Object obj = getObject(index, columnName);
		if(obj instanceof Float){
			return (float) obj;
		}else{
			return 0F;
		}
	}
	
	/**
	 * Gets an int from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the int found
	 * @throws DatabaseException 
	 */
	public int getInteger(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof Integer){
			return (int) obj;
		}else{
			return 0;
		}
	}
	
	/**
	 * Gets a long from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the long found
	 * @throws DatabaseException 
	 */
	public long getLong(int index, String columnName) throws DatabaseException  {
		Object obj = getObject(index, columnName);
		if(obj instanceof Double){
			return (long) obj;
		}else{
			return 0L;
		}
	}
	
	/**
	 * Gets an Object from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the Object found
	 * @throws DatabaseException 
	 */
	public Object getObject(int index, String columnName) throws DatabaseException {
		int rows = rowCount();
		if(index > rows - 1) throw new DatabaseException(index + " > (rowCount() - 1), which is " + rows);
		int columnIndex = -1;
		for(int i=0; i<columns.size(); i++){
			if(columns.get(i).equalsIgnoreCase(columnName)){
				columnIndex = i;
				break;
			}
		}
		if(columnIndex == -1) throw new DatabaseException("Column with the name of '" + columnName + "' not found");
		return values.get(index).get(columnIndex);
	}
	
	/**
	 * Gets a short from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the short found
	 * @throws DatabaseException 
	 */
	public short getShort(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof Short){
			return (short) obj;
		}else{
			return 0;
		}
	}
	
	/**
	 * Gets a String from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the String found
	 * @throws DatabaseException 
	 */
	public String getString(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof String){
			return (String) obj;
		}else{
			return null;
		}
	}
	
	/**
	 * Gets a Timestamp from the query.
	 * @param index the row
	 * @param columnName the column
	 * @return the String found
	 * @throws DatabaseException
	 */
	public Timestamp getTimestamp(int index, String columnName) throws DatabaseException {
		Object obj = getObject(index, columnName);
		if(obj instanceof Timestamp){
			return (Timestamp) obj;
		}else{
			return null;
		}
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
		return values.size();
	}

}
