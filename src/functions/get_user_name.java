/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Fayez
 */
public class get_user_name {
    
      public String user_name() throws FileNotFoundException, IOException {
        String file_name = new getMacAdress2().getMacAddress().trim();
        FileInputStream fstream = new FileInputStream(file_name + ".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
//Read File Line By Line
//while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
//  System.out.println (strLine);
//}
        strLine = br.readLine();//يحمل نوع الاتصال لا نحتاجه الان
        strLine = br.readLine();//يحمل اسم المستخدم
        br.close();
        return strLine;
    }
}
