package dbController;

import java.util.*;

import entity.Klass;

import main.Stutas;
public class ClassTab1Ctrl {														//对表ClassTab处理
	private ArrayList classID=new ArrayList();
	
	
	public String[] getClassID(String grade){						//方法getClassID,通过年级(grade)和专业(profession)获得班号
		
		Set<String> klassSet = Stutas.getKlassMap().keySet();
		StringBuffer sbf = new StringBuffer();
		for(String klass:klassSet){
			Klass kla = Stutas.getKlassMap().get(klass);
			if(kla.getGrade().toString().equals(grade)){
				sbf.append(kla+";");
			}
		}

		String[] result = sbf.toString().split(";");
		sbf = null;
		return result;
	}
	
}
