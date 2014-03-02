package dbController;

public class StuScheduleBean {							//类StuScheduleBean,保存学生一个时间片上课表的所有信息
	
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
