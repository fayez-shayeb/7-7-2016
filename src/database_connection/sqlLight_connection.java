/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database_connection;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 Table name =var
 * columns = var_id ; var_name  ;var_value
 */
public class sqlLight_connection {
    ResultSet rs = null;
    Connection con = null;
    Statement st = null;
    

    
      public ResultSet rs_conn_exec(String s)
    {
          
      try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:variables.db");
            System.out.println("Opened database successfully");
            
            st = con.createStatement();
            rs=st.executeQuery(s);
            //st.close();

        } catch (SQLException | ClassNotFoundException ex) {
            Joptionpane_message(ex.getMessage());
        }
        return rs;
    }
      
      public void exec(String s)
    {
        
      try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:variables.db");
            System.out.println("Opened database successfully");
            
            st = con.createStatement();
            st.executeUpdate(s);
            st.close();

        } catch (SQLException | ClassNotFoundException ex) {
            Joptionpane_message(ex.getMessage());
        }
    }
      public void Joptionpane_message(String message)
{
    JOptionPane.showMessageDialog(null,message);

}
}
