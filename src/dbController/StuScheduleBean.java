package dbController;

public class StuScheduleBean {							//��StuScheduleBean,����ѧ��һ��ʱ��Ƭ�Ͽα��������Ϣ
	
	private String teacherName;
	private String courseName;
	private boolean isLocked;
	
	
	
	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public void setTeacherName(String teacherName){
		this.teacherName=teacherName;
	}
	
	public void setCourseName(String courseName){
		this.courseName=courseName;
	}
	
	
	public String getTeacherName(){
		return teacherName;
	}
	
	public String getCourseName(){
		return courseName;
	}
	

}
