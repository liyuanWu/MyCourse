package dbController;
import java.sql.*;

public class DBcon {
	
	private String driver="sun.jdbc.odbc.JdbcOdbcDriver";
	private String dbsource="jdbc:odbc:ArrangeCourseSys1";
	private Connection con=null;
	
	public Connection getConnect(){
		try{
			Class.forName(driver);      //��������
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("�������������쳣!");
		}
		try{
			con=DriverManager.getConnection(dbsource);     //��������Դ
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("��������Դʱ�����쳣!");
		}
		return con;
	}

}
