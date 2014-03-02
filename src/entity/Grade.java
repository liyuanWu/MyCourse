package entity;

import java.io.Serializable;

public class Grade implements Serializable{

	Integer num;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	public String toString(){
		return num+"";
	}
}
