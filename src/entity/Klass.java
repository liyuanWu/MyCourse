package entity;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import main.Stutas;

public class Klass implements Serializable{

	
	Grade grade;
	Integer clsNum;
	String[][] courseNamTable;
	List<String> courseNamList;
	
	public Klass(){
		courseNamList = new ArrayList<String>();
		courseNamTable = new String[5][6];
	}
	
	public String[][] getCourseNamTable() {
		if(courseNamTable==null){
			courseNamTable = new String [5][6];
		}
		refresh();
		return courseNamTable;
	}
	public void setCourseNamTable(String[][] courseNamTable) {
		this.courseNamTable = courseNamTable;
	}
	public boolean hasClass(Integer Day,Integer num){
		return courseNamTable[Day][num]!=null;
	}

	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	public void setGrade(String grade) {
		this.grade = new Grade();
		this.grade.setNum(Integer.parseInt(grade));
	}
	public Integer getClsNum() {
		return clsNum;
	}
	public void setClsNum(Integer clsNum) {
		this.clsNum = clsNum;
	}
	public List<String> getCourseNamList() {
		if(courseNamList==null){
			courseNamList = new ArrayList<String>();
		}
		return courseNamList;
	}
	public void setCourseNamList(List<String> courseNamList) {
		this.courseNamList = courseNamList;
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
					courseNamTable[time.getDay()][time.getNum()] = obj;
				}
			}
		}
	}
	Exception exp =null;
	public void addCourseNam(Object obj) {
		
		Course course = Stutas.courseMap.get(obj.toString());
		if(course==null){
			course = obj instanceof Course?(Course)obj:null;
		}
		if(course!=null){
			if(courseNamList.contains(obj.toString())){
				System.err.println("重复添加："+obj);
				new Exception().printStackTrace();
				System.err.println("第一次添加："+obj);
				exp.printStackTrace();
				return;
			}else{
				exp = new Exception();
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
	public void removeCourse(String courseNam) {
		this.courseNamList.remove(courseNam);
		for(int i=0;i<courseNamTable.length;i++){
			for(int j=0;j<courseNamTable[i].length;j++){
				if(courseNamTable[i][j]!=null&&courseNamTable[i][j].equals(courseNam)){
					courseNamTable[i][j]=null;
				}
			}
		}
	}
	
	
	public String toString(){
		return ""+clsNum;
	}

	public static String getNam(String grade2, String klassNam) {
		return klassNam;
	}

	public static String getNam(String classID) {
		return classID;
	}
	
	
	
}
