package database_connection;


import functions.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;


public final class db_Connection {
        ResultSet rs = null;
        String url;
        //String url = "jdbc:postgresql://localhost:8080/shayeb_2015";
        //String url = "jdbc:postgresql://192.168.2.117:8080/shayeb_2015";
        String user = "postgres";
        String password = "25121986";
       public  Connection get_con1 = null;
        Statement get_st = null;
        
     
   public db_Connection()
   {  
           
            try {
                
                url="jdbc:postgresql://"+get_connection_type()+":8081/shayeb_2015";
                
                //url="jdbc:postgresql://"+get_connection_type()+":8081/shayeb_copy_before_stat";
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
                Joptionpane_message(ex.getMessage());
            }
            
   }  
   
   public void create_conn() 
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

    public String get_connection_type() throws FileNotFoundException, IOException {
        String file_name = new getMacAdress2().getMacAddress().trim();
        FileInputStream fstream = new FileInputStream(file_name + ".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;
//Read File Line By Line
//while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
//  System.out.println (strLine);
//}
        strLine = br.readLine();
        br.close();
        return strLine;
    }


    }
  
   
   

