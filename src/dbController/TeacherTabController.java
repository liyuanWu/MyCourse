package dbController;
import java.sql.*;
import java.util.*;

import main.Stutas;
public class TeacherTabController {														//��TeacherTabController,�Ա�TeacherTab���д���
																				
	
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
	
	
	public TeacherBean getTeacherBean(String teacherID){								//����getTeacherBean(),��ý�ʦ��������Ϣ
		
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
	
	
	public char[] getTeacherUseInfo(String teacherID){									//����getTeacherUseInfo(),���һλ��ʦ��ʱ��Ƭʹ����Ϣ
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
	
	public void addTeacherTimerInfo(char[][] timerInfo){								//����addTeacherTimerInfo(),�����н�ʦ��ʱ��Ƭʹ����Ϣ���뵽TeacherTab��
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
	
	public void updateRoomUseInfo(String teacherID,char[] teacherUseInfo){				//����updateRoomUseInfo(),����ĳλ��ʦ��ʱ��Ƭ��Ϣ
		
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

	public void closeBD(){				//�ر����ݿ�Ĳ���
		
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
