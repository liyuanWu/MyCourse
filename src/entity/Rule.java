package entity;

import java.io.Serializable;

public class Rule implements Serializable{

	String subjectNam;
	
	public String getSubjectNam() {
		return subjectNam;
	}
	public void setSubjectNam(String subjectNam) {
		this.subjectNam = subjectNam;
	}
	public String getKlassNam() {
		return klassNam;
	}
	public void setKlassNam(String klassNam) {
		this.klassNam = klassNam;
	}
	public String getTeacherNam() {
		return teacherNam;
	}
	public void setTeacherNam(String teacherNam) {
		this.teacherNam = teacherNam;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	String klassNam;
	String teacherNam;
	Time time;
	String type;
}
