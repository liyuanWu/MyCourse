package dbController;
import java.sql.*;
import java.util.*;

public class AllClassViewController {							//��AllClassViewContorller,����ͼallClassView�Ŀ���
	
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
	
	public ArrayList getAllClassInfo(){							//����:getAllClass(),�������а༶��,�γ̺ż����ʦ�źͽ�������
		
		try{
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			while(rs.next()){
				classid=rs.getString("classID");        		//��ȡ���
				courseid=rs.getString("courseID");				//��ȡ�γ̺�
				teacherid=rs.getString("teacherID");			//��ȡ��ʦ��
				CAI=rs.getString("CAI");						//��ȡ��������
				classid=classid+courseid+teacherid+CAI;			//�Ѱ�š���ʦ�š��γ̺ż�����������Ϊ���崦�����巵��
				allClass.add(classid);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			this.closeBD();
		}
		return allClass;
	}
	
	public void closeBD(){										//�ر����ݿ�Ĳ���
		
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
