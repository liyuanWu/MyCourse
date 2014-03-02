package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.Stutas;

public class Course implements Serializable{

	List<Time> times;
	String subjectNam;
	String teacherNam;
	String klassNam;
	Integer number;
	
	public Course(){
		super();
		times = new ArrayList<Time>();
	}
	
	public List<Time> getTimes() {
		if(times==null){
			times = new ArrayList<Time>();
		}
		return times;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}
	public void addTime(Time time){
		if(times.size()>=number){
			System.out.println("¹ý¶ÈÌí¼Ó:"+teacherNam+"--"+subjectNam+"--"+klassNam);
		}
		if(times==null){
			times = new ArrayList<Time>();
		}
		if(time!=null){
			times.add(time);
		}
		Stutas.getKlassMap().get(klassNam).refresh();
		Stutas.getTeacherMap().get(teacherNam).refresh();
		
	}
	public String getSubjectNam() {
		return subjectNam;
	}
	public void setSubjectNam(String subjectNam) {
		this.subjectNam = subjectNam;
	}
	public String getTeacherNam() {
		return teacherNam;
	}
	public void setTeacherNam(String teacherNam) {
//		if(this.teacherNam==null){
			this.teacherNam = teacherNam;
//			return;
//		}
//		String odName = toString();
//		this.teacherNam = teacherNam;
//		Stutas.getCourseMap().remove(this);
//		Stutas.getCourseMap().put(this.toString(), this);
//		Klass klass = Stutas.getKlassMap().get(getKlassNam());
//		int index = klass.getCourseNamList().indexOf(odName);
//		klass.getCourseNamList().set(index, toString());
//		klass.refresh();
//		Teacher teacher = Stutas.getTeacherMap().get(getTeacherNam());
//		index = teacher.getCourseNamList().indexOf(odName);
//		teacher.addCourse(this);
////		teacher.getCourseNamList().set(index, toString());
//		teacher.refresh();
	}
	public void associate(){
		Klass klass = Stutas.klassMap.get(klassNam);
		klass.addCourseNam(this);
		Teacher teacher = Stutas.getTeacherMap().get(teacherNam);
		teacher.addCourse(this);
	}
	public String getKlassNam() {
		return klassNam;
	}
	public void setKlassNam(String klassNam) {
		this.klassNam = klassNam;
	}
	public String toString(){
		return teacherNam+"--"+subjectNam+"--"+klassNam;
	}

	public void removeTime(Time ordinaryTime) {
		int del = -1;
		for(Time time:times){
			if(time.getDay().intValue() == ordinaryTime.getDay().intValue() && time.getNum().intValue()==ordinaryTime.getNum().intValue()){
				del = times.indexOf(time);
			}
		}
		if(del!=-1){
			times.remove(del);
		}
		Stutas.getKlassMap().get(klassNam).refresh();
		Stutas.getTeacherMap().get(teacherNam).refresh();
	}

	public boolean isLock(int i, int j) {
		for(Time time:times){
			if(time.getDay().equals(i)&&time.getNum().equals(j)){
				return time.isLocked();
			}
		}
		return false;
		
	}

	public void lock(int i, int j) {
		for(Time time:times){
			if(time.getDay().equals(i)&&time.getNum().equals(j)){
				time.setLock(true);
				return;
			}
		}
	}
	
	public void unlock(int i, int j) {
		for(Time time:times){
			if(time.getDay().equals(i)&&time.getNum().equals(j)){
				time.setLock(false);
				return;
			}
		}
	}

	

}
