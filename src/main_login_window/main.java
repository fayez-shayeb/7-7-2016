package main_login_window;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;


public class main {

private static final int PORT = 9999;
private static ServerSocket socket;

    
    public static void main(String[] args) throws Exception {
        //checkIfRunning();//فحص اذا بكون هذا البرنامج شغال 
        login_form login_form_obj=new login_form();
        login_form_obj.setVisible(true);

    }
    private static void checkIfRunning() {
  try {
    //Bind to localhost adapter with a zero connection queue 
    socket = new ServerSocket(PORT,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
  }
  catch (BindException e) {
    System.exit(1);
  }
  catch (IOException e) {
    System.err.println("Unexpected error.");
    System.exit(2);
  }
}
}
