package entity;

import java.io.Serializable;

public class Time implements Serializable{

	boolean lock;
	
	public void setLock(boolean j){
		this.lock = j;
	}
	public boolean isLocked(){
		return lock;
	}
	
	public Time(Integer day,Integer num){
		this.Day = day;
		this.Num = num;
	}
	public Time(){
	}
	Integer Day;
	public Integer getDay() {
		return Day;
	}
	public void setDay(Integer day) {
		Day = day;
	}
	public Integer getNum() {
		return Num;
	}
	public void setNum(Integer num) {
		Num = num;
	}
	Integer Num;
	public String toString(){
		return Day+"-"+Num;
	}
}
