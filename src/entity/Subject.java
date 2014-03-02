package entity;

import java.io.Serializable;

public class Subject implements Serializable{
	String name;

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

