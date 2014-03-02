package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import main.Stutas;

public class Teacher implements Serializable{

	String name;
	List<String> courseNamList;
	String[][] courseNamTable;
	boolean multi = false;

	public Teacher(){
		courseNamList = new ArrayList<String>();
		courseNamTable = new String[5][10];
	}
	
	
	public boolean isMulti() {
		return multi;
	}


	public void setMulti(boolean multi) {
		this.multi = multi;
	}


	public void addCourse(Object obj){

		Course course = Stutas.courseMap.get(obj.toString());
		if(course==null){
			course = obj instanceof Course?(Course)obj:null;
		}
		if(course!=null){
			if(courseNamList.contains(obj.toString())){
				System.err.println("÷ÿ∏¥ÃÌº”£∫"+obj);
				new Exception().printStackTrace();
				return;
			}
			if(!courseNamList.contains(obj.toString())){
				courseNamList.add(obj.toString());
				for(Time time:course.getTimes()){
					courseNamTable[time.getDay()][time.getNum()] = obj.toString();
				}
			}
		}
		refresh();
	}
	public void refresh(){
		for(int i=0;i<courseNamTable.length;i++){
			for(int j=0;j<courseNamTable[i].length;j++){
				courseNamTable[i][j]=null;
				}
		}
		for(String obj:courseNamList){
			Course course = Stutas.courseMap.get(obj.toString());
			if(course!=null){
				for(Time time:course.getTimes()){
					courseNamTable[time.getDay()][time.getNum()] = obj.toString();
				}
			}
		}
	}

	public void removeCourse(String courseName){
		courseNamList.remove(courseName);
		for(int i=0;i<courseNamTable.length;i++){
			for(int j=0;j<courseNamTable[i].length;j++){
				if(courseNamTable[i][j]!=null&&courseNamTable[i][j].equals(courseName)){
					courseNamTable[i][j]=null;
				}
			}
		}
	}
	public List<String> getCourseNamList() {
		return courseNamList;
	}

	public void setCourseNamList(List<String> courseNamList) {
		this.courseNamList = courseNamList;
	}

	public String[][] getCourseNamTable() {
		refresh();
		return courseNamTable;
	}

	public void setCourseNamTable(String[][] courseNamTable) {
		this.courseNamTable = courseNamTable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String toString(){
		return name;
	}
}
