package userUI;

public class CountTimer {

	public int getTimer(String weekday,String daytime){
		int timer=0;
		int weekday1=0;
		int daytime1=0;
		
		if(weekday.equals("星期一")){
			weekday1=0;
		}else 
		if(weekday.equals("星期二")){
			weekday1=1;
		}else 
		if(weekday.equals("星期三")){
			weekday1=2;
		}else
		if(weekday.equals("星期四")){
			weekday1=3;
		}else if(weekday.equals("星期五")){
			weekday1=4;
		}
		
		if(daytime.equals("第一大节")){
			daytime1=0;
		}else if(daytime.equals("第二大节")){
			daytime1=1;
		}else if(daytime.equals("第三大节")){
			daytime1=2;
		}else if(daytime.equals("第四大节")){
			daytime1=3;
		}
		
		timer=4*weekday1+daytime1;
		return timer;
	}
	
}
