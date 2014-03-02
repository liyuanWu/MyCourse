package dbController;
import java.sql.*;
import java.util.*;

import main.Stutas;
public class TeacherTabController {														//类TeacherTabController,对表TeacherTab进行处理
																				
	
	private DBcon dbcon=new DBcon();
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	private String sql=null;
	private TeacherBean teacherBean=new TeacherBean();
	
	public String[] getTeacherIds() {
		String[] strs = new String[Stutas.getTeacherMap().size()];
		int i=0;
		for(Object s:Stutas.getTeacherMap().values().toArray()){
			strs[i] = s.toString();
			i++;
		}
		Arrays.sort(strs);
		return strs;
	}
	
	
	public TeacherBean getTeacherBean(String teacherID){								//方法getTeacherBean(),获得教师的所有信息
		
		sql="select top 1 * from TeacherTab where teacherID=?";
		
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, teacherID);
			rs=pst.executeQuery();
			
			while(rs.next()){
				teacherBean.setTeacherID(rs.getString(1));
				teacherBean.setTeacherName(rs.getString(2));
				teacherBean.setCourseID(rs.getString(3));
				teacherBean.setTimerInfo(rs.getString(4));
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return teacherBean;
	}
	
	
	public char[] getTeacherUseInfo(String teacherID){									//方法getTeacherUseInfo(),获得一位教师的时间片使用信息
		char[] timeInfo=new char[20];
		String SuseInfo=null;
		sql="select top 1 timerInfo from TeacherTab where teacherID=?";
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, teacherID);
			rs=pst.executeQuery();
			while(rs.next()){
				SuseInfo=rs.getString(1);
				for(int i=0;i<20;i++){
					timeInfo[i]=SuseInfo.charAt(i);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return timeInfo;
	}
	
	public void addTeacherTimerInfo(char[][] timerInfo){								//方法addTeacherTimerInfo(),把所有教师的时间片使用信息加入到TeacherTab中
		String teacherID=null;
		sql="update TeacherTab set TimerInfo=? where TeacherID=?";
		
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			for(int i=1;i<=37;i++){
				if(i<10){
					teacherID="09000"+i;
				}else{
					teacherID="0900"+i;
				}
				pst.setString(1, new String(timerInfo[i-1]));
				pst.setString(2, teacherID);
				pst.execute();
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void updateRoomUseInfo(String teacherID,char[] teacherUseInfo){				//方法updateRoomUseInfo(),更新某位教师的时间片信息
		
		String useInfo=new String(teacherUseInfo);
		sql="update TeacherTab set timerInfo=? where teacherID=?";
		try{
				con=dbcon.getConnect();
				pst=con.prepareStatement(sql);
				pst.setString(1, useInfo);
				pst.setString(2, teacherID);
				pst.execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
	}

	public void closeBD(){				//关闭数据库的操作
		
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
