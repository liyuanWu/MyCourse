package dbController;

import java.util.*;

import entity.Klass;

import main.Stutas;
public class ClassTab1Ctrl {														//�Ա�ClassTab����
	private ArrayList classID=new ArrayList();
	
	
	public String[] getClassID(String grade){						//����getClassID,ͨ���꼶(grade)��רҵ(profession)��ð��
		
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
