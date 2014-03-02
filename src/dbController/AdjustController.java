package dbController;

import java.sql.*;
import java.util.*;

public class AdjustController {															//�����α�����,���ս�ʦҪ��,�Կα���е���
											
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	
	public String adjustController(String teacherID,int oldtimer,int newtimer){			//����������,�����ַ�������
		
		String message=new String();													//���ڱ��淵�ص��ַ���
		String classID=new String();													
		String roomID=new String();
		String teacherTimerInfo=new String();											//���ڱ����ʦ��һ��ʱ��Ƭ��Ϣ	
		
		char[] roomUseInfo=new char[20];												//������ҵ�ʱ��Ƭ��ʹ����Ϣ
		char[] teacherUseInfo=new char[20];												//�����ʦ��ʱ��Ƭ��ʹ����Ϣ
		
		String[] scheduleInfo=new String[20];											//����һ���༶������ʱ��Ƭ����Ϣ
		
		ScheduleTabController scheCtrl=new ScheduleTabController();
		
		String sql="select top 1 timer"+(oldtimer+1)+" from TeacherScheduleTab where teacherID=?";	//��ý�ʦ��ָ��ʱ���ڵ���Ϣ
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
		try{																			//���һ���༶�Ŀα���Ϣ
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
		
		if(roomUseInfo[newtimer]=='0'){													//�жϽ������µ�ָ��ʱ�����Ƿ����,Ϊ'0'��ʾ����
			if(scheduleInfo[newtimer]==null){											//�ж�ѧ�����µ�ָ��ʱ�����Ƿ����
				
				scheduleInfo[newtimer]=scheduleInfo[oldtimer];							//��ѧ��Ҫ����������ʱ��Ƭ�����û�	
				scheduleInfo[oldtimer]="";
				roomUseInfo[newtimer]='1';
				roomUseInfo[oldtimer]='0';
				teacherUseInfo[newtimer]='1';
				teacherUseInfo[oldtimer]='0';
				
				scheCtrl.updateClassSche(classID, scheduleInfo[newtimer], newtimer, oldtimer);		//����ѧ���α�
				scheCtrl.updateTeachSche(teacherID, teacherTimerInfo, newtimer, oldtimer);			//���½�ʦ�α�
				new TeacherTabController().updateRoomUseInfo(teacherID, teacherUseInfo);			//���½�ʦ��ʱ��Ƭʹ����Ϣ
				new RoomInfoController().updateRoomUseInfo(roomID, roomUseInfo);					//���½��ҵ�ʱ��Ƭʹ����Ϣ
				
				message="�����ɹ�";
				
			}else{
				message="��ѧ��ʱ���г�ͻ";
			}
		}else{
			message="�ͽ���ʱ���г�ͻ";
		}
		
		
		
		return message;
	}
	
	public void closeBD(){													//�ر����ݿ�Ĳ���
		
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
