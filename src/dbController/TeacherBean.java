package dbController;

public class TeacherBean {										//��TeacherBean,�����ʦ��������Ϣ
	
	private String teacherID=null;
	private String teacherName=null;
	private String courseID=null;
	private String timerInfo=null;
	
	public void setTeacherID(String teacherID){
		this.teacherID=teacherID;
	}
	
	public void setTeacherName(String teacherName){
		this.teacherName=teacherName;
	}
	
	public void setCourseID(String courseID){
		this.courseID=courseID;
	}
	
	public void setTimerInfo(String timerInfo){
		this.timerInfo=timerInfo;
	}
	
	public String getTeacherID(){
		return teacherID;
	}
	
	public String getTeacherName(){
		return teacherName;
	}
	
	public String getCourseID(){
		return courseID;
	}
	
	public String getTimerInfo(){
		return timerInfo;
	}

}
