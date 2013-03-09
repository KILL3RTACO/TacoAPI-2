package taco.tacoapi.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import org.bukkit.ChatColor;

/**
 * A utility chat specialized for String formatting. The following codes can be used to format text:<br/><br/>
 * <pre>
 * 			{@code &a     GREEN}
 * 			{@code &b     AQUA}
 * 			{@code &c     RED}
 * 			{@code &d     LIGHT_PURPLE}
 * 			{@code &e     YELLOW}
 * 			{@code &f     WHITE}
 * 			{@code &0     BLACK}
 * 			{@code &1     DARK_BLUE}
 * 			{@code &2     DARK_GREEN}
 * 			{@code &3     DARK_AQUA}
 * 			{@code &4     DARK_RED}
 * 			{@code &5     DARK_PURPLE}
 * 			{@code &6     GOLD}
 * 			{@code &7     GRAY}
 * 			{@code &8     DARK_GRAY}
 * 			{@code &9     BLUE}
 * 			{@code &k     MAGIC}
 * 			{@code &l     BOLD}
 * 			{@code &m     STRIKETHROUGH}
 * 			{@code &n     UNDERLINE}
 * 			{@code &o     ITALICS}
 * 			{@code &r     RESET}
 * </pre>
 * @author Taco
 *
 */
public class ChatUtils {      
	
	private final String codes = "0123456789abcdefklmnor";
	
	/**
	 * Creates a header with a default border color of &6
	 * @param title - The title of the header
	 * @return A formatted header
	 */
	public String createHeader(String title){
		return createHeader('6', title);
	}
	
	/**
	 * Creates a header with a specified border color
	 * @param title - The title of the header
	 * @return A formatted header
	 */
	public String createHeader(char borderColor, String title){
		return formatMessage("&" + borderColor + "=====[&f"+ title + "&" + borderColor + "]=====");
	}
	
	/**
	 * Uses format codes (preceding by {@code &}) to format the given message.
	 * @param message - The message to format
	 * @return The formatted message
	 */
	public String formatMessage(String message){
		for(int i=0; i<codes.length(); i++){
			char code = codes.charAt(i);
			if(message.contains("&" + code))
				message = message.replaceAll("&" + code, ChatColor.getByChar(code).toString());
		}
		return message;
	}
	
	/**
	 * Opposite effect if one where to use {@code formatMessage()}. Instead of replacing the color code with a {@code ChatColor} object,
	 * it removes the color codes as if by:
	 * <pre>
	 * 		if(s.contains(colorcode))
	 * 			s = s.replaceAll(colorcode, "");
	 * </pre>
	 * @param s - The String which to remove the color codes from
	 * @return The same string with text formattting removed
	 */
	public String removeColorCodes(String s){
		for(int i=0; i<codes.length(); i++){
			if(s.contains("&" + codes.charAt(i)))
				s = s.replaceAll("&" + codes.charAt(i), "");
		}
		return s;
	}
	
	/**
	 * Tests whether a String is an int.
	 * @param s
	 * @return Whether or not the String is an int
	 */
	public boolean isNum(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public String makeStringFromArray(String[] args){
		String result = args[0];
		args = removeFirstArg(args);
		for(String s : args){
			result += " " + s;
		}
		return result;
	}
	
	/**
	 * Removes the specified indices from a String array (everything before {@code startIndex} is kept). Code is from DeityAPI
	 * @param array - Array in which the specified indices before {@code startIndex} is removed
	 * @param startIndex - Any index before this is removed
	 * @return The same array given with the specified indices removed
	 */
	public String[] removeArgs(String[] array, int startIndex){
	    if (array.length == 0)
	      return array;
	    if (array.length < startIndex)
	      return new String[0];
	    
	    String[] newSplit = new String[array.length - startIndex];
	    System.arraycopy(array, startIndex, newSplit, 0, array.length - startIndex);
	    return newSplit;
	}

	/**
	 * Removes the first index in a String array. Code is from DeityAPI
	 * @param array - The array in which the first index is removed
	 * @return The same array given with the first index removed
	 */
	public String[] removeFirstArg(String[] array){
	    return removeArgs(array, 1);
	}
	
	/**
	 * Converts a String to ProperCase.
	 * @param s the String tto be converted
	 * @return the converted String
	 */
	public String toProperCase(String s){
		String result = "";
		for(String str : s.split(" ")){
			String newStr = (str.charAt(0) + "").toUpperCase();
			if(str.length() > 1) newStr += str.substring(1).toLowerCase();
			result += newStr + " ";
		}
		return result.substring(0, result.length() - 1);
	}
	
	public <E> E getRandomElement(ArrayList<E> list){
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}
	
	public String getFriendlyTimestamp(Timestamp time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time.getTime());
		String weekday = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH);
		String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		String ampm = (calendar.get(Calendar.AM_PM) == Calendar.AM ? "a" : "p");
		return weekday + ", " + month + " " + day + ", " + (hour > 12 ? hour - 12 : hour) + ":" + (min < 10 ? "0" + min : min) + ampm;
	}
	
}
