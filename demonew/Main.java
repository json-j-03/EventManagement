package demonew;
import java.sql.*;

public class Main{
	public static void main(String args[]) throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/event","root","jjjason@%&)");
		EventConsole evec= new EventConsole(con); 
		evec.mainmenu();

}
}
