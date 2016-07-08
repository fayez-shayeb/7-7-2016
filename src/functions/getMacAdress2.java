/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class getMacAdress2{
 public String getMacAddress() throws IOException
 {
     String command = "ipconfig /all";
       Process p = Runtime.getRuntime().exec(command);
 
       BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
       Pattern pattern = Pattern.compile(".*Physical Addres.*: (.*)");
 
       while (true) {
            String line = inn.readLine();
 
	    if (line == null)
	        break;
 
	    Matcher mm = pattern.matcher(line);
	    if (mm.matches()) {
	        return (mm.group(1));
                
	    }
	}
       return "";
 }
   
}