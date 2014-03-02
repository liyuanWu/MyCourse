package dbController;
import java.sql.*;
import java.util.*;

public class AllClassViewController {							//类AllClassViewContorller,对视图allClassView的控制
	
	private ArrayList allClass=new ArrayList();
	
	private String classid;
	private String courseid;
	private String teacherid;
	private String CAI;
	
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	private DBcon dbcon=new DBcon();
	
	private String sql="select * from allClassView ";
	
	public ArrayList getAllClassInfo(){							//方法:getAllClass(),返回所有班级号,课程号及其教师号和教室类型
		
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				classid=rs.getString("classID");        		//获取班号
				courseid=rs.getString("courseID");				//获取课程号
				teacherid=rs.getString("teacherID");			//获取教师号
				CAI=rs.getString("CAI");						//获取教室类型
				classid=classid+courseid+teacherid+CAI;			//把班号、教师号、课程号及教室类型作为整体处理，整体返回
				allClass.add(classid);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		return allClass;
	}
	
	public void closeBD(){										//关闭数据库的操作
		
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
