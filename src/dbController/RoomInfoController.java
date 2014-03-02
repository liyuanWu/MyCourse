package dbController;
import java.sql.*;
import java.util.*;

public class RoomInfoController {											//��RoomInfoController,�Ա�RoomTab���д���

	private ArrayList haveCAI=new ArrayList();
	private ArrayList haveNoCAI=new ArrayList();
	
	private DBcon dbcon=new DBcon();
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	
	private String roomID=null;
	
	private String sql=null;
	public ArrayList getCAI(){												//����getCAI(),������ж�ý�����
			
		sql="select * from RoomTab where CAI=?";
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, "1");
			
			rs=pst.executeQuery();
			while(rs.next()){
				haveCAI.add(rs.getObject(1));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return haveCAI;
	}
	
	public ArrayList getNoCAI(){											//����getNoCAI(),���������ͨ����
		
		sql="select * from RoomTab where CAI=?";
		try{
			con=dbcon.getConnect();											
			pst=con.prepareStatement(sql);
			pst.setString(1, "0");
			
			rs=pst.executeQuery();
			
			while(rs.next()){
				haveNoCAI.add(rs.getObject(1));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return haveNoCAI;
	}
	
	public ArrayList getRoomID(int timer,String CAI){						//����getRoomID(),���ݽ������ͺ�ʱ��Ƭ,��ø�ʱ��Ƭ�Ŀ��н��Һ�
		ArrayList roomID=new ArrayList();
		String room=null;
		sql="select roomID,useInfo from RoomTab";
		String sql1="select roomID,useInfo from RoomTab where CAI=?";
		try{
			con=new DBcon().getConnect();
			if(CAI!=null){													//�ж��Ƿ��н������͵�Ҫ��
				pst=con.prepareStatement(sql1);
				pst.setString(1, CAI);
			}else{
				pst=con.prepareStatement(sql);
			}
			
			rs=pst.executeQuery();
			while(rs.next()){
				room=rs.getString(1);
				if(rs.getString(2).charAt(timer)=='0'){
					roomID.add(room);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		return roomID;
	}
	
	public String[] getRoomUseInfo(String roomID){							//����getRoomUseInfo(),���ݽ��Һ�,��øý���һ���ڵ�ʹ�����
		String[] useInfo=new String[20];
		String SuseInfo=null;
		sql="select top 1 useInfo from RoomTab where roomID=?";
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, roomID);
			rs=pst.executeQuery();
			while(rs.next()){
				SuseInfo=rs.getString(1);
				for(int i=0;i<20;i++){
					if(SuseInfo.charAt(i)=='1'){
						useInfo[i]="�п�";
					}else{
						useInfo[i]="  ";
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return useInfo;
	}
	
	public char[] getRoomUseInfo1(String roomID){							//����getRoomUseInfo1(),���ݽ��Һ�,��øý���һ���ڵ�ʹ�����
		char[] useInfo=new char[20];
		String SuseInfo=null;
		sql="select top 1 useInfo from RoomTab where roomID=?";
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.setString(1, roomID);
			rs=pst.executeQuery();
			while(rs.next()){
				SuseInfo=rs.getString(1);
				for(int i=0;i<20;i++){
					useInfo[i]=SuseInfo.charAt(i);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
		return useInfo;
		
		
	}

	public void addCAIRoomUseInfo(char[][] roomUseInfo){					//����addCAIRoomUseInfo(),��CAI���ҵ�ʹ���������RoomTab
		
		String useInfo=null;
		sql="update RoomTab set useInfo=? where roomID=?";
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			for(int i=0;i<roomUseInfo.length;i++){
				useInfo=new String(roomUseInfo[i]);
				roomID=haveCAI.get(i).toString();
				pst.setString(1, useInfo);
				pst.setString(2, roomID);
				pst.execute();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
	}
	
	public void addNoCAIRoomUseInfo(char[][] roomUseInfo){					//����addNoCAIRoomUseInfo(),��NoCAI���ҵ�ʹ���������RoomTab
		
		String useInfo=null;
		sql="update RoomTab set useInfo=? where roomID=?";
		try{
			con=dbcon.getConnect();
			for(int i=0;i<roomUseInfo.length;i++){
				
				useInfo=new String(roomUseInfo[i]);
				pst=con.prepareStatement(sql);
				roomID=haveNoCAI.get(i).toString();
				pst.setString(1, useInfo);
				pst.setString(2, roomID);
				pst.execute();
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
	}
	
	public void updateRoomUseInfo(String roomID,char[] roomUseInfo){		//����updateRoomUseInfo(),���½��ҵ�ʹ�����
		
		String useInfo=new String(roomUseInfo);
		sql="update RoomTab set useInfo=? where roomID=?";
		try{
				con=dbcon.getConnect();
				pst=con.prepareStatement(sql);
				pst.setString(1, useInfo);
				pst.setString(2, roomID);
				pst.execute();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		
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
