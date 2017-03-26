package me.F_o_F_1092.PayForCommand.PluginManager;

public class Math {

	public static boolean isNumber(String strg) {  
	  try  {  
	    Integer.parseInt(strg);  
	  } catch(NumberFormatException e) {  
	    return false;  
	  }  
	  
	  return true;  
	}
	
}
