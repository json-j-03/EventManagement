package demonew;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class EventConsole extends EventManager{
	static  Scanner sc= new Scanner(System.in);
	private final Connection con;
	public EventConsole(Connection con) {
		super(con);
		this.con= con;
	}
	public void mainmenu() throws Exception{
		while(true) {			
			System.out.println("--------------------------");
			System.out.println("| Event Manager          |");
			System.out.println("|------------------------|");
			System.out.println("| 1. Enter Event Details |");
			System.out.println("|------------------------|");
			System.out.println("| 2. View Events         |");
			System.out.println("|------------------------|");
			System.out.println("| 3. Add Attendee Details|");
			System.out.println("|------------------------|");
			System.out.println("| 4. View Event Details  |");
			System.out.println("|------------------------|");
			System.out.println("| 5. Edit Event Details  |");
			System.out.println("|------------------------|");
			System.out.println("| 6. Delete Event        |");
			System.out.println("|------------------------|");
			System.out.println("| 7. Exit                |");
			System.out.println("--------------------------");
			System.out.println("Enter your choice: ");
			int n= sc.nextInt();
			switch(n) {
				case 1:
					enterEvent();
					break;
				case 2:
					showEvent();
					break;
				case 3:
					addAttendee();
					break;
				case 4:
					System.out.println("Enter Event Name: ");
					String eventName= sc.next();
					showEvent(eventName);
					break;
				case 5: 
					System.out.println("Enter event to update: ");
					String eventName1= sc.next();
					updateEvent(eventName1);
					break;
				case 6:
					System.out.println("Enter event to delete: ");
					String eventName2= sc.next();
					deleteEvent(eventName2);
					break;
				case 7: 
					System.out.println("Thanks!");
					return;
				default:
					System.out.println("Enter correct option");
					
			}
		}
	}

	public void addAttendee() {
	      System.out.println("Enter event name:");
	      String eventName = sc.next();
	      String check= "Select * from event_details where event_name=?";
			try(PreparedStatement ps= con.prepareStatement(check)){
				ps.setString(1, eventName);
				ResultSet rs= ps.executeQuery();
				if(rs.next()) {
					
					System.out.println("Enter attendee name:");
					String attendeeName = sc.next();
					
					String sql = "INSERT INTO attendee_details (attendee_name, event_name) VALUES (?, ?)";
					try (PreparedStatement statement = con.prepareStatement(sql)) {
						statement.setString(1, attendeeName);
						statement.setString(2, eventName);
						statement.executeUpdate();
						System.out.println("Attendee added successfully!");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println("event doesnt exist");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
	  }
		
	public void showEvent() {
      String sql = "SELECT * FROM event_details";
      try (PreparedStatement statement = con.prepareStatement(sql)) {
          try (ResultSet resultSet = statement.executeQuery()) {
              System.out.println("---------------------------------------------------------------------");
				System.out.printf("| %-15s | %-15s | %-10s | %-15s |\n","Event Name","Event Location","Event Date","Event Time");
              System.out.println("---------------------------------------------------------------------");
          	while(resultSet.next())
              {
              	System.out.printf("| %-15s | %-15s | %-10s | %-15s |\n",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
              }
              System.out.println("---------------------------------------------------------------------");

          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
		
	}
	public void showEvent(String eventName) {
		String query2="Select * from event_details where event_name=?";
		try (PreparedStatement statement = con.prepareStatement(query2)) {
			statement.setString(1, eventName);
			try (ResultSet resultSet = statement.executeQuery()) {
				if(resultSet.next()) {
					System.out.println("--------------------------------------------------------------------");
					System.out.printf("| %-15s | %-15s | %-10s | %-15s |\n","Event Name","Event Location","Event Date","Event Time");
					System.out.println("--------------------------------------------------------------------");
					while(resultSet.next())
					{
						System.out.printf("| %-15s | %-15s | %-10s | %-15s |\n",resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
					}
					System.out.println("--------------------------------------------------------------------");
				}
				else {
					System.out.println("Event doesn't exist");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("Task details");
		String query1="Select task_name from task_details where event_name=?";
	      try (PreparedStatement statement = con.prepareStatement(query1)) {
	    	  statement.setString(1, eventName);
	          try (ResultSet resultSet = statement.executeQuery()) {
	              System.out.println ("-------------------");
				  System.out.printf  ("| %-15s |\n","Task Name");
	              System.out.println ("-------------------");
	          	while(resultSet.next())
	              {
	              	System.out.printf("| %-15s |\n",resultSet.getString(1));
	              }
	              System.out.println("-------------------");

	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	      System.out.println();
	      System.out.println("Attended Guests");
	      String query3= "SELECT a.attendee_name FROM attendee_details a INNER JOIN "
	      		+ "event_attendee_details b ON a.attendee_name = b.attendee_name "
	      		+ "where a.event_name= ? and b.event_name= ?";
	      try (PreparedStatement statement = con.prepareStatement(query3)) {
	    	  statement.setString(1, eventName);
	    	  statement.setString(2, eventName);
	          try (ResultSet resultSet = statement.executeQuery()) {
	              System.out.println ("-------------------");
				  System.out.printf  ("| %-15s |\n","Attendee Name");
	              System.out.println ("-------------------");
	          	while(resultSet.next())
	              {
	              	System.out.printf("| %-15s |\n",resultSet.getString(1));
	              }
	              System.out.println("-------------------");

	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	      System.out.println();
	      System.out.println("Non-Attended Guests");
	      String query4= "SELECT a.attendee_name FROM event_attendee_details a "+ 
	    		  	     "Left JOIN attendee_details b "+
	    		  	     "ON a.attendee_name = b.attendee_name "+ 
	    		  	     "where a.event_name= ? "+ 
	    		  	     "and b.attendee_name is null";
	      try (PreparedStatement statement = con.prepareStatement(query4)) {
	    	  statement.setString(1, eventName);

	          try (ResultSet resultSet = statement.executeQuery()) {
	              System.out.println ("-------------------");
				  System.out.printf  ("| %-15s |\n","Attendee Name");
	              System.out.println ("-------------------");
	          	while(resultSet.next())
	              {
	              	System.out.printf("| %-15s |\n",resultSet.getString(1));
	              }
	              System.out.println("-------------------");

	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	      System.out.println();
	      System.out.println("Non-Invited Guests");
	      String query5 = " select A.attendee_name "
	      		+ "from event_attendee_details E "
	      		+ "right join attendee_details A "
	      		+ "on A.attendee_name = E.attendee_name "
	      		+ "where E.attendee_name is null and A.event_name = ?;";
	      try (PreparedStatement statement = con.prepareStatement(query5)) {
	    	  statement.setString(1, eventName);

	          try (ResultSet resultSet = statement.executeQuery()) {
	              System.out.println ("-------------------");
				  System.out.printf  ("| %-15s |\n","Attendee Name");
	              System.out.println ("-------------------");
	          	while(resultSet.next())
	              {
	              	System.out.printf("| %-15s |\n",resultSet.getString(1));
	              }
	              System.out.println("-------------------");

	          }
	      } catch (SQLException e) {
	          e.printStackTrace();
	      }
	}
	public void updateEvent(String eventName) throws Exception{
		String check= "Select * from event_details where event_name=?";
		try(PreparedStatement ps= con.prepareStatement(check)){
			ps.setString(1, eventName);
			ResultSet rs= ps.executeQuery();
			if(rs.next()) {
				String date,time,location;
				System.out.println("Enter new date");
				date= sc.next();
				System.out.println("Enter new time");
				time= sc.next();
				System.out.println("Enter new location");
				location= sc.next();
				String query= "UPDATE event_details SET event_date=?, event_time=?, event_location=? "
						+ "where event_name=?";
				try(PreparedStatement st= con.prepareStatement(query)){
					st.setString(1, date);
					st.setString(2, time);
					st.setString(3, location);
					st.setString(4, eventName);
					int rows= st.executeUpdate();
					if(rows>0) {
						System.out.println("Event updated successfully");
					}
					else {
						System.out.println("Event failed to update");
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				ps.close();
			}
			else {
				System.out.println("event doesnt exist");
			}
		}catch(Exception e) {
				e.printStackTrace();
			}
	}
	public void deleteEvent(String eventName) throws Exception{
		String check= "Select * from event_details where event_name=?";
		try(PreparedStatement ps= con.prepareStatement(check)){
			ps.setString(1, eventName);
			ResultSet rs= ps.executeQuery();
			if(rs.next()) {
				String delete1= "Delete from event_details where event_name=?";
				try(PreparedStatement ps1= con.prepareStatement(delete1)){
					ps1.setString(1,eventName);
					ps1.executeUpdate();
					System.out.println("Event deleted successfully");
					ps1.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				String delete2= "Delete from task_details where event_name=?";
				try(PreparedStatement ps1= con.prepareStatement(delete2)){
					ps1.setString(1,eventName);
					ps1.executeUpdate();
					System.out.println("Tasks deleted successfully");
					ps1.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				ps.close();
				String delete3= "Delete from attendee_details where event_name=?";
				try(PreparedStatement ps1= con.prepareStatement(delete3)){
					ps1.setString(1,eventName);
					ps1.executeUpdate();
					System.out.println("Invited guests deleted successfully");
					ps1.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
				String delete4= "Delete from event_attendee_details where event_name=?";
				try(PreparedStatement ps1= con.prepareStatement(delete4)){
					ps1.setString(1,eventName);
					ps1.executeUpdate();
					System.out.println("Attended deleted successfully");
					ps1.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("event doesnt exist");
			}
		}
	}
}
