package dbController;
import java.sql.*;

public class DBcon {
	
	private String driver="sun.jdbc.odbc.JdbcOdbcDriver";
	private String dbsource="jdbc:odbc:ArrangeCourseSys1";
	private Connection con=null;
	
	public Connection getConnect(){
		try{
			Class.forName(driver);      //加载驱动
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("加载驱动出现异常!");
		}
		try{
			con=DriverManager.getConnection(dbsource);     //连接数据源
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("连接数据源时出现异常!");
		}
		return con;
	}

}
