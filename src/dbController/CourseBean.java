package dbController;

public class CourseBean {													//��CourseBean,�ѿγ̵�������Ϣ��װ��һ����
	
	private String courseID;
	private String courseName;
	private int totalTime;
	private int weekTime;
	private String CAI;
	
	public void setCourseID(String courseID){
		this.courseID=courseID;
	}
	
	public void setCourseName(String courseName){
		this.courseName=courseName;
	}
	
	public void setTotalTime(int totalTime){
		this.totalTime=totalTime;
	}
	
	public void setWeekTime(int weekTime){
		this.weekTime=weekTime;
	}
	
	public void setCAI(String CAI){
		this.CAI=CAI;
	}
	
	public String getCourseID(){
		return courseID;
	}
	
	public String getCourseName(){
		return courseName;
	}
	
	public int getTotalTime(){
		return totalTime;
	}
	
	public int getWeekTime(){
		return weekTime;
	}
	
	public String getCAI(){
		return CAI;
	}
	
	

}
