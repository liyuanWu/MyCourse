package dbController;

import java.sql.*;
import java.util.*;

public class AdjustController {															//调整课表处理类,按照教师要求,对课表进行调整
											
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	
	public String adjustController(String teacherID,int oldtimer,int newtimer){			//调整主函数,返回字符串类型
		
		String message=new String();													//用于保存返回的字符串
		String classID=new String();													
		String roomID=new String();
		String teacherTimerInfo=new String();											//用于保存教师的一个时间片信息	
		
		char[] roomUseInfo=new char[20];												//保存教室的时间片的使用信息
		char[] teacherUseInfo=new char[20];												//保存教师的时间片的使用信息
		
		String[] scheduleInfo=new String[20];											//保存一个班级的所有时间片的信息
		
		ScheduleTabController scheCtrl=new ScheduleTabController();
		
		String sql="select top 1 timer"+(oldtimer+1)+" from TeacherScheduleTab where teacherID=?";	//获得教师在指定时间内的信息
		try{
			con=new DBcon().getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, teacherID);
			rs=pst.executeQuery();
			while(rs.next()){
				teacherTimerInfo=rs.getString(1);
				roomID=teacherTimerInfo.substring(0,5);
				classID=teacherTimerInfo.substring(5);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		sql="select top 1 * from ScheduleTab where classID=?";
		try{																			//获得一个班级的课表信息
			con=new DBcon().getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, classID);
			rs=pst.executeQuery();
			while(rs.next()){
				for(int i=1;i<21;i++){
					scheduleInfo[i-1]=rs.getString(i+1);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		roomUseInfo=new RoomInfoController().getRoomUseInfo1(roomID);
		teacherUseInfo=new TeacherTabController().getTeacherUseInfo(teacherID);
		
		if(roomUseInfo[newtimer]=='0'){													//判断教室在新的指定时间内是否可用,为'0'表示可用
			if(scheduleInfo[newtimer]==null){											//判断学生在新的指定时间内是否可用
				
				scheduleInfo[newtimer]=scheduleInfo[oldtimer];							//把学生要调整的两个时间片内容置换	
				scheduleInfo[oldtimer]="";
				roomUseInfo[newtimer]='1';
				roomUseInfo[oldtimer]='0';
				teacherUseInfo[newtimer]='1';
				teacherUseInfo[oldtimer]='0';
				
				scheCtrl.updateClassSche(classID, scheduleInfo[newtimer], newtimer, oldtimer);		//更新学生课表
				scheCtrl.updateTeachSche(teacherID, teacherTimerInfo, newtimer, oldtimer);			//更新教师课表
				new TeacherTabController().updateRoomUseInfo(teacherID, teacherUseInfo);			//更新教师的时间片使用信息
				new RoomInfoController().updateRoomUseInfo(roomID, roomUseInfo);					//更新教室的时间片使用信息
				
				message="调整成功";
				
			}else{
				message="和学生时间有冲突";
			}
		}else{
			message="和教室时间有冲突";
		}
		
		
		
		return message;
	}
	
	public void closeBD(){													//关闭数据库的操作
		
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
