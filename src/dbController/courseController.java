package dbController;
import java.sql.*;
public class courseController {						//类courseController,对courseTab表进行处理
	private int weektime;
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	String sql=null;
	private CourseBean courseBean=new CourseBean();
	
	
	public CourseBean getCourseBean(String courseID){		//方法getCourseBean,根据courseID获得该课程的信息
		
		sql="select top 1 * from courseTab where courseID=?";
		try{
			con=new DBcon().getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, courseID);
			rs=pst.executeQuery();
			
			while(rs.next()){
				courseBean.setCourseID(rs.getString(1));
				courseBean.setCourseName(rs.getString(2));
				courseBean.setTotalTime(rs.getInt(3));
				courseBean.setWeekTime(rs.getInt(4));
				courseBean.setCAI(rs.getString(5));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return courseBean;
	}
	
	public int getWeekTime(String courseID){			//获得每周的上课次数
		sql="select top 1 weekTime from courseTab where courseID=?";
		try{
			con=new DBcon().getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, courseID);
			rs=pst.executeQuery();
			
			while(rs.next()){
				weektime=rs.getInt(1);
				weektime=weektime/2;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return weektime;
	}
	
	public void closeBD(){						//关闭数据库的操作
		
		try{
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(pst!=null){
					pst.close();
					pst=null;
				}
				if(con!=null){
					con.close();
					con=null;
				}
				
			}catch(Exception ex){
				
				ex.printStackTrace();
			}
	}	
}
