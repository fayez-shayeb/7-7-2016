/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Fayez
 */
public class db_bashar_connection {
   
        
        ResultSet rs = null;
        String url = "jdbc:postgresql://localhost:8081/bashar";
        String user = "postgres";
        String password = "25121986";
        Connection get_con1 = null;
        Statement get_st = null;
      
   
        
        
   public void create_conn()//after conn closed only
   {
       try {   
              {
                get_con1 = DriverManager.getConnection(url, user, password);
                get_st = get_con1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_UPDATABLE);
                }
            
        } catch (SQLException ex) {

            Joptionpane_message(ex.getMessage());
        } 
   }
   public ResultSet conn_exec(String s)
    {
        
       try {   
            if (get_st == null||get_con1==null) {
                get_con1 = DriverManager.getConnection(url, user, password);
                get_st = get_con1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_UPDATABLE);
                }
          
            rs = get_st.executeQuery(s);
            
        } catch (SQLException ex) {
           
            Joptionpane_message(ex.getMessage());
        } 
        return rs;
    }
   
   public void exec(String s)
    {
        
       try {   
            if (get_st == null||get_con1==null) {
                get_con1 = DriverManager.getConnection(url, user, password);
                get_st = get_con1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_UPDATABLE);
                }
          
            get_st.execute(s);
            
        } catch (SQLException ex) {
              
            Joptionpane_message(ex.getMessage());
        } 
        
    }
   public void close()
   {
       try {
                if (rs != null) {
                    rs.close();
                }
                if (get_st != null) {
                    get_st.close();
                }
                if (get_con1 != null) {
                    get_con1.close();
                }

            } catch (SQLException ex) {
                Joptionpane_message(ex.getMessage());
                
            } 
   }
public Statement get_st()
{
    return get_st;
}
public Connection get_con()
{
    return get_con1;
}

public ResultSet get_resultset()
{
    return rs;
}
public void Joptionpane_message(String message)
{
    JOptionPane.showMessageDialog(null,message);

}

 
}
