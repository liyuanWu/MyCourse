package dbController;

public class TeacherScheduleBean {						//类TeacherScheduelBean,保存教师课表一个时间片上课表的所有信息
	
	private String courseName;
	private String klassName;
	private boolean isLocked;
	
	
	
	public boolean isLocked() {
		return isLocked;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getKlassName() {
		return klassName;
	}
	public void setKlassName(String klassName) {
		this.klassName = klassName;
	}
	
	

}
