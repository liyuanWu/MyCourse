package userUI;

public class CountTimer {

	public int getTimer(String weekday,String daytime){
		int timer=0;
		int weekday1=0;
		int daytime1=0;
		
		if(weekday.equals("����һ")){
			weekday1=0;
		}else 
		if(weekday.equals("���ڶ�")){
			weekday1=1;
		}else 
		if(weekday.equals("������")){
			weekday1=2;
		}else
		if(weekday.equals("������")){
			weekday1=3;
		}else if(weekday.equals("������")){
			weekday1=4;
		}
		
		if(daytime.equals("��һ���")){
			daytime1=0;
		}else if(daytime.equals("�ڶ����")){
			daytime1=1;
		}else if(daytime.equals("�������")){
			daytime1=2;
		}else if(daytime.equals("���Ĵ��")){
			daytime1=3;
		}
		
		timer=4*weekday1+daytime1;
		return timer;
	}
	
}
