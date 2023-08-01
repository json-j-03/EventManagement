package demonew;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class EventManager extends EventAbstractClass{
	public EventManager(Connection con) {
		super(con);
	}
	static Scanner sc= new Scanner(System.in);
	
	@Override
	public void enterEvent() throws SQLException{
		String eventName,eventDate, eventLocation, eventTime;
		System.out.println("Enter Event Name: ");
		eventName= sc.next();
		System.out.println("Enter Event Date (YYYY/MM/DD): ");
		eventDate= sc.next();
		System.out.println("Enter Event Location: ");
		eventLocation= sc.next();
		System.out.println("Enter Event Time: ");
		eventTime=sc.next();
//		System.out.println(eventName+" "+eventDate+" "+eventLocation+" "+eventTime);
		try {
			String query= "INSERT INTO "
					+ "event_details(event_name,event_date,event_location, event_time) "
					+ "VALUES(?,?,?,?)";
			PreparedStatement st= con.prepareStatement(query); 
			st.setString(1, eventName);
			st.setString(2,eventDate);
			st.setString(3, eventLocation);
			st.setString(4, eventTime);
			int rows= st.executeUpdate();
			if(rows>0) {
				System.out.println("Data successfully entered");
			}
			else {
				System.out.println("Data failed to enter");
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while(true) {
			System.out.println("------------------------------");
			System.out.println("| Current Event Manager      |");
			System.out.println("|----------------------------|");
			System.out.println("| 1. Enter Task Details      |");
			System.out.println("|----------------------------|");
			System.out.println("| 2. Enter Attendee Details  |");
			System.out.println("|----------------------------|");
			System.out.println("| 3. Exit                    |");
			System.out.println("------------------------------");
			System.out.println("Enter your choice: ");
			int n= sc.nextInt();
			switch(n) {
				case 1:
					enterTask(eventName);
					break;
				case 2:
					enterAttendee(eventName);
					break;
				case 3: 
					System.out.println("Thanks!");
					return;
				default:
					System.out.println("Enter correct option");
					
			}
		}
	}
	@Override
	public void enterAttendee(String eventName) throws SQLException{
	    System.out.println("Enter attendee name :");
	    String attendeeName = sc.next();
	
	    String sql = "INSERT INTO event_attendee_details (attendee_name, event_name) VALUES (?, ?)";
	    try (PreparedStatement statement = con.prepareStatement(sql)) {
	        statement.setString(1, attendeeName);
	        statement.setString(2, eventName);
	        statement.executeUpdate();
	        System.out.println("Attendee added successfully!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void enterTask(String eventName) throws SQLException {
		String taskName;
		System.out.println("Enter task_name: ");
		taskName= sc.next();
		String query= "INSERT INTO task_details (event_name,task_name) VALUES(?,?)";
		PreparedStatement ps= con.prepareStatement(query);
	      try (PreparedStatement statement = con.prepareStatement(query)) {
	    	  ps.setString(1, eventName);
	    	  ps.setString(2, taskName);
	    	  ps.executeUpdate();
	    	  System.out.println("Task added successfully!");
	  } catch (SQLException e) {
	      e.printStackTrace();
	  }
	}
}
