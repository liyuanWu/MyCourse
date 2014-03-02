package dbController;
import java.sql.*;

public class ScheduleDel {										//��ScheduleDel,�Կα����ɾ��
	
	private DBcon dbcon=new DBcon(); 
	private Connection con=null;
	private PreparedStatement pst=null;
	private ResultSet rs=null;
	
	public void del(){
		
		String sql="delete from ScheduleTab";
		String sql1="delete from TeacherScheduleTab";
		try{
		
			con=dbcon.getConnect();
			pst=con.prepareStatement(sql);
			pst.execute();
			pst=con.prepareStatement(sql1);
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
