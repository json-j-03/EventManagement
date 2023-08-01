package demonew;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

abstract class EventAbstractClass {
    protected final Connection con;
    protected final Scanner sc;

    public EventAbstractClass(Connection con) {
        this.con = con;
        sc = new Scanner(System.in);
    }
    public abstract void enterEvent() throws SQLException ;
    
    public abstract void enterAttendee(String eventName) throws SQLException;

    public abstract void enterTask(String eventName) throws SQLException;
}
