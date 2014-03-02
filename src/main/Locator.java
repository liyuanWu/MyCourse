package main;

import java.io.File;
import java.io.FileWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import entity.Course;
import entity.Klass;
import entity.Subject;
import entity.Teacher;
import entity.Time;

public class Locator {

	static int[][] numberMap = new int[5][6];
	static String[][] pathMap = new String[5][6];

	public static void excuteMap(Klass klass, int day, int num) {
		for (int i = 0; i < numberMap.length; i++) {
			for (int j = 0; j < numberMap[i].length; j++) {
				numberMap[i][j] = -1;
				pathMap[i][j] = "";

			}
		}
		numberMap[day][num] = 0;
		timeQueue = new LinkedList<>();
		timeQueue.add(new Time(day, num));
		Time t1;
		while ((t1 = timeQueue.poll()) != null) {
			drawmap(klass, t1.getDay(), t1.getNum());
		}
		// Stutas.print2DArray(numberMap);
		// Stutas.print2DArray(pathMap);
	}

	static Queue<Time> timeQueue;

	private static void drawmap(Klass klass, int day, int num) {
		boolean[][] booleanMap = getExchangableCourse(klass, day, num);
		for (int i = 0; i < booleanMap.length; i++) {
			for (int j = 0; j < booleanMap[i].length; j++) {
				if (booleanMap[i][j]) {
					if (numberMap[i][j] == -1
							|| numberMap[i][j] > numberMap[day][num] + 1) {
						numberMap[i][j] = numberMap[day][num] + 1;
						pathMap[i][j] = pathMap[day][num] + day + "," + num
								+ "," + i + "," + j + "," + ";";
						timeQueue.add(new Time(i, j));
					}
				}
			}
		}
	}

	public static boolean forceExchange(Klass klass, int fDay, int fNum,
			int sDay, int sNum) {
		excuteMap(klass, sDay, sNum);
		if (numberMap[sDay][sNum] == -1) {
			return false;
		} else {
			String paths[] = pathMap[fDay][fNum].split(";");
			for (int i = 0; i < paths.length; i++) {
				String content[] = paths[i].split(",");
				if (content.length != 4) {
					System.out.println();
				}
				if (!getExchangableCourse(klass, sDay, sNum)[Integer
						.valueOf(content[2])][Integer.valueOf(content[3])]) {
					System.out.println("unexchangeable! " + klass + "  " + sDay
							+ "," + sNum + "  to  "
							+ Integer.valueOf(content[2]) + ","
							+ Integer.valueOf(content[3]));
					// if(){
					// System.out.println("Judgment reject");
					// }else{
					// System.out.println("judgement passed");
					// }
					return false;
				}
				// System.out.println("exchangeCourse:"+klass+"  "+
				// Integer.valueOf(content[0])+","+
				// Integer.valueOf(content[1])+"  to  "+
				// Integer.valueOf(content[2])+","+
				// Integer.valueOf(content[3]));
				Locator.exchangeCourse(klass, sDay, sNum,
						Integer.valueOf(content[2]),
						Integer.valueOf(content[3]));
			}
			// System.out.println("Success!");
			return true;
		}

	}

	private static boolean isExchangable(Klass klass, Integer valueOf,
			Integer valueOf2, Integer valueOf3, Integer valueOf4) {
		if (klass == null) {
			return false;
		}
		Course course = Stutas.getCourseMap().get(
				klass.getCourseNamTable()[valueOf3][valueOf4]);
		if (course == null) {
			return false;
		}
		Teacher teacher = Stutas.getTeacherMap().get(course.getTeacherNam());
		String[][] time = teacher.getCourseNamTable();
		if (time[valueOf3][valueOf4] == null) {
			String[][] courNames = klass.getCourseNamTable();
			Course selectedCourse = Stutas.courseMap
					.get(courNames[valueOf3][valueOf4]);
			if (selectedCourse != null) {
				if (selectedCourse.isLock(valueOf3, valueOf4)) {
					return false;
				}
				Teacher bakTeacher = Stutas.teacherMap.get(selectedCourse
						.getTeacherNam());
				if (bakTeacher.getCourseNamTable()[valueOf][valueOf2] == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean[][] getLocatable(Course course) {
		if (course == null) {
			return null;
		}
		if (Stutas.getTeacherMap().containsKey(course.getTeacherNam())
				&& Stutas.getKlassMap().containsKey(course.getKlassNam())) {
			String[][] tTime = Stutas.getTeacherMap()
					.get(course.getTeacherNam()).getCourseNamTable();
			String[][] kTime = Stutas.getKlassMap().get(course.getKlassNam())
					.getCourseNamTable();
			boolean[][] result = new boolean[5][6];
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 6; j++) {
					if (Stutas.getTeacherMap().get(course.getTeacherNam())
							.isMulti()) {
						result[i][j] = true;
						continue;
					}
					if (tTime[i][j] == null && kTime[i][j] == null) {
						result[i][j] = true;
					}
				}
			}
			return result;
		}
		return null;

	}

	public static void exchangeCourse(Klass klass, int fDay, int fNum,
			int sDay, int sNum) {
		Course course1 = Stutas.getCourseMap().get(
				klass.getCourseNamTable()[fDay][fNum]);
		Course course2 = Stutas.getCourseMap().get(
				klass.getCourseNamTable()[sDay][sNum]);
		if (course1 != null) {
			course1.removeTime(new Time(fDay, fNum));
			course1.addTime(new Time(sDay, sNum));
		}
		if (course2 != null) {
			course2.removeTime(new Time(sDay, sNum));
			course2.addTime(new Time(fDay, fNum));
		}
	}

	public static boolean[][] getExchangableCourse(Klass klass, int Day, int Num) {
		if (klass == null) {
			return null;
		}
		Course course = Stutas.getCourseMap().get(klass.getCourseNamTable()[Day][Num]);
		if (course == null) {
			return null;
		}
		Teacher teacher = Stutas.getTeacherMap().get(course.getTeacherNam());
		String[][] time = teacher.getCourseNamTable();
		boolean[][] result = new boolean[5][6];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
					String[][] courNames = klass.getCourseNamTable();
					Course selectedCourse = Stutas.courseMap
							.get(courNames[i][j]);
					if (selectedCourse != null) {
						if (selectedCourse.isLock(i, j)) {
							result[i][j] = false;
							continue;
						}
						if((!selectedCourse.equals(course))&&selectedCourse.getTeacherNam().equals(teacher.toString()) && selectedCourse.getKlassNam().equals(klass.toString())){
							result[i][j] = true;
							continue;
						}else if(time[i][j]!=null){
							result[i][j] = false;
							continue;
						}
						Teacher bakTeacher = Stutas.teacherMap
								.get(selectedCourse.getTeacherNam());
						if (bakTeacher.getCourseNamTable()[Day][Num] == null) {
							result[i][j] = true;
						}
					}
			}
		}
		return result;
	}
	
	public static String[] checkTeachers(){
		ArrayList<String> teachers = new ArrayList<>();
		for(Teacher teacher:Stutas.getTeacherMap().values()){
			int course  = 0;
			for(int i=0;i<5;i++){
				for(int j=0;j<6;j++){
					if(teacher.getCourseNamTable()[i][j]!=null){
						course++;
					}
				}
				if(course>3){
					teachers.add(teacher.toString());
					break;
				}else{
					course  = 0;
				}
			}
			
		}
		String[] result = new String[teachers.size()];
		int i=0;
		for(String s:teachers){
			result[i] =s;
			i++;
		}
		Arrays.sort(result);
		return result;
	}

	public static void randomLocate() {
		List<Course> conflectList = new ArrayList<Course>();
		Random random = new Random();
		for (Course course : Stutas.getCourseMap().values()) {
			Klass klass = Stutas.getKlassMap().get(course.getKlassNam());
			Teacher teacher = Stutas.getTeacherMap()
					.get(course.getTeacherNam());
			// if(course.toString().equals("汤颦--美术--1-161")){
			// System.out.println("Timetogo");
			// }
			for (int i = 1; i <= course.getNumber(); i++) {
				int tries = 2000;
				while (true) {
					tries--;
					if (tries < 0) {
						conflectList.add(course);
						break;
					}

					int time = random.nextInt(30);
					if (klass.getCourseNamTable()[time / 6][time % 6] != null) {
						continue;
					} else if (teacher.getCourseNamTable()[time / 6][time % 6] != null) {
						continue;
					} else {
						course.addTime(new Time(time / 6, time % 6));
						// System.out.println(course.toString() + time / 6 +
						// "   "
						// + time % 6 + "\n");
						break;
					}
				}
			}
		}
		// for(Course course:conflectList){
		// if(!moveCourse(course, null, null)){
		// System.out.println(course.toString());
		// }
		// }
		System.out.println("Unsuccess:" + conflectList.size());
		check();
	}

	public static String check_2() {
		class CourseNum {
			public int num = 0;
			public Course course;
		}
		StringBuffer report = new StringBuffer();
		for (Klass klass : Stutas.getKlassMap().values()) {
			List<CourseNum> courses = new ArrayList<CourseNum>();
			for (String courseNam : klass.getCourseNamList()) {
				CourseNum courseNum = new CourseNum();
				courseNum.course = Stutas.getCourseMap().get(courseNam);
				courseNum.num = 0;

				for (CourseNum courseNumber : courses) {
					if (courseNumber.course.equals(Stutas.getCourseMap().get(
							courseNam))) {
						System.out.println("发现重复:" + courseNumber.course);
					}
				}
				courses.add(courseNum);
			}
			String[][] classTable = klass.getCourseNamTable();
			for (int i = 0; i < classTable.length; i++) {
				for (int j = 0; j < classTable[i].length; j++) {
					if (classTable[i][j] == null) {
						continue;
					}
					Course course = Stutas.getCourseMap().get(classTable[i][j]);
					if (course == null) {
						System.out.println("Null  name:" + classTable[i][j]
								+ "\n");
						continue;
					}
					Teacher teacher = Stutas.getTeacherMap().get(
							course.getTeacherNam());
					if (teacher == null) {
						System.out.println("null Teacher");
					}
					if (!teacher.isMulti()
							&& (teacher.getCourseNamTable()[i][j] == null || !teacher
									.getCourseNamTable()[i][j]
									.equals(classTable[i][j]))) {
						System.out.println("重复添加:" + classTable[i][j] + "," + i
								+ "," + j + ";"
								+ teacher.getCourseNamTable()[i][j]);
					}
					boolean isNew = true;
					for (CourseNum courseNum : courses) {
						if (courseNum.course == null) {
							System.out.println("courseNull\n");
							return "courseNull\n";
						}
						if (courseNum.course.equals(course)) {
							courseNum.num++;
							isNew = false;
						}
					}
					if (isNew) {
						CourseNum courNum = (new CourseNum());
						courNum.course = course;
						courNum.num++;
						courses.add(courNum);
					}
				}
			}
			for (CourseNum courseNum : courses) {
				if (courseNum.num > courseNum.course.getNumber()) {
					report.append("发现异常:" + courseNum.course + "  规定数值:"
							+ courseNum.course.getNumber() + "   实际数值:"
							+ courseNum.num + "\n");
				}
				if (courseNum.num < courseNum.course.getNumber()) {
					report.append("发现缺漏:" + courseNum.course + "  规定数值:"
							+ courseNum.course.getNumber() + "   实际数值:"
							+ courseNum.num + "\n");
				}
			}
			System.out.println("检查完毕:" + klass);
		}
		System.out.println(report);
		return report.toString();
	}

	public static void check_Sp() {
		for (Klass klass : Stutas.getKlassMap().values()) {
			String[][] classTable = klass.getCourseNamTable();
			int ysW = 0;
			for (int i = 0; i < classTable.length; i++) {
				for (int j = 0; j < classTable[i].length; j++) {
					if (classTable[i][j] == null) {
						continue;
					}
					Course course = Stutas.getCourseMap().get(classTable[i][j]);
					if (course == null) {
						System.out.println("Null  name:" + classTable[i][j]
								+ "\n");
						continue;
					}
					if (course.getSubjectNam().equals("阅读")) {
						if (!moveCourse(course, new Time(i, j), new Time(0, 5))) {
							if (forceExchange(klass, i, j, 0, 5)) {
								course.lock(0, 5);
							} else {
								System.out.println("调课失败:阅读" + klass + i + "  "
										+ j);
							}
						} else {
							course.lock(0, 5);
						}
					}
					if (course.getSubjectNam().equals("思品")) {
						if (klass.getClsNum() >= 157 && klass.getClsNum() <= 164) {
							if (!moveCourse(course, new Time(i, j), new Time(4, 3))) {
								if (forceExchange(klass, i, j, 4, 3)) {
									course.lock(4, 3);
								} else {
									System.out.println("调课失败:思品" + klass + i + "  "
											+ j);
								}
							} else {
								course.lock(4, 3);
							}
						}
					}
					if (course.getSubjectNam().equals("班队")) {
							if (!moveCourse(course, new Time(i, j), new Time(3, 5))) {
								if (forceExchange(klass, i, j, 3, 5)) {
									course.lock(3, 5);
								} else {
									System.out.println("调课失败:班队" + klass + i + "  "
											+ j);
								}
							} else {
								course.lock(3, 5);
							}
						}
						
					if (course.getSubjectNam().equals("语文")
							|| course.getSubjectNam().equals("数学")) {
						if (ysW < 2) {
							if (!moveCourse(course, new Time(i, j), new Time(2,
									ysW))) {
								if (forceExchange(klass, i, j, 2, ysW)) {
									course.lock(2, ysW);
									ysW++;
								} else {
									System.out.println("调课失败:语文|数学" + klass + i
											+ "  " + j);
								}
							} else {
								course.lock(2, ysW);
								ysW++;
							}
						}
					}
					if (course.getSubjectNam().equals("语文")) {
						List<Time> oTime = new ArrayList<>();
						for (Time time : course.getTimes()) {
							if (!time.isLocked()) {
								if (time.getDay() == 1 && time.getNum() == 5) {
									oTime.add(new Time(time.getDay(), time
											.getNum()));
								}
							}
						}
						for (Time time : oTime) {
							if (!moveCourse(course, time, null)) {
								System.out.println("调课失败:语文" + klass + "\t" + i
										+ "\t" + j);
							}
						}
					}
					if (course.getSubjectNam().equals("数学")) {
						List<Time> oTime = new ArrayList<>();
						for (Time time : course.getTimes()) {
							if (!time.isLocked()) {
								if (time.getDay() == 2 && time.getNum() >= 4) {
									oTime.add(new Time(time.getDay(), time
											.getNum()));
								}
							}
						}
						for (Time time : oTime) {
							if (!moveCourse(course, time, null)) {
								System.out.println("调课失败:数学" + klass + "\t" + i
										+ "\t" + j);
							}
						}
					}
					Teacher teacher = new Teacher();
					teacher.setMulti(true);
					teacher.setName("活动课老师");
					Stutas.getTeacherMap().put(teacher.toString(), teacher);
					Subject sub = new Subject();
					sub.setName("活动");
					Stutas.getSubjectMap().put(sub.toString(), sub);
					if (klass.getClsNum() >= 157 && klass.getClsNum() <= 164) {
						List<Time> oTime = new ArrayList<>();
						for (Time time : course.getTimes()) {
							if (!time.isLocked()) {
								if (time.getDay() < 5 && time.getNum() == 3) {
									oTime.add(new Time(time.getDay(), time
											.getNum()));
								}
							}
						}
						Course n_course = null;
						for (Time time : oTime) {
							if (moveAwayCourse(klass,time.getDay(),time.getNum())) {
								if (n_course == null) {
									n_course = new Course();
									n_course.setKlassNam(klass.toString());
									n_course.setTeacherNam(teacher.toString());
									n_course.setNumber(4);
									n_course.setSubjectNam(sub.toString());
									Stutas.getCourseMap().put(n_course.toString(), n_course);
									n_course.associate();
									Time nTime = new Time(time.getDay(),
											time.getNum());
									nTime.setLock(true);
									n_course.addTime(nTime);
								} else {
									Time nTime = new Time(time.getDay(),
											time.getNum());
									nTime.setLock(true);
									n_course.addTime(nTime);
								}
							} else {
								System.out.println("调课失败:活动" + klass + "\t" + i
										+ "\t" + j);
							}
						}
					}
				}
			}

		}
	}
	
	public static boolean moveAwayCourse(Klass klass, int day, int num){
		Course course = Stutas.courseMap.get(klass.getCourseNamTable()[day][num]);
		if(course==null){
			return false;
		}
		boolean[][] tm = getLocatable(course);
			for(int i=0;i<tm.length;i++){
				for(int j=0;j<tm[i].length;j++){
					if(tm[i][j]){
						moveCourse(course, new Time(day,num),new Time(i,j));
						return true;
					}
				}
			}
		return false;
	}

	public static void check_KT() {
		for (Klass klass : Stutas.getKlassMap().values()) {
			String[][] classTable = klass.getCourseNamTable();
			for (int i = 0; i < classTable.length; i++) {
				for (int j = 0; j < classTable[i].length; j++) {
					if (classTable[i][j] == null) {
						continue;
					}
					Course course = Stutas.getCourseMap().get(classTable[i][j]);
					if (course == null) {
						System.out.println("Null  name:" + classTable[i][j]
								+ "\n");
						continue;
					}
					Teacher teacher = Stutas.getTeacherMap().get(
							course.getTeacherNam());
					if ((!classTable[i][j].contains("体育"))
							&& (teacher.getCourseNamTable()[i][j] == null || !teacher
									.getCourseNamTable()[i][j]
									.equals(classTable[i][j]))) {
						Course tCourse = Stutas.getCourseMap().get(
								teacher.getCourseNamTable()[i][j]);
						if (tCourse == null) {
							teacher.getCourseNamTable()[i][j] = classTable[i][j];
						} else {
							Klass tklass = Stutas.getKlassMap().get(
									tCourse.getKlassNam());
							if (tklass == null) {
								teacher.getCourseNamTable()[i][j] = classTable[i][j];
							} else {
								Course kCourse = Stutas.getCourseMap().get(
										classTable[i][j]);
								kCourse.removeTime(new Time(i, j));
							}
						}
					}
				}
			}

		}
	}

	public static void check() {
		class CourseNum {
			public int num = 0;
			public Course course;
		}
		StringBuffer report = new StringBuffer();
		for (Klass klass : Stutas.getKlassMap().values()) {
			String[][] classTable = klass.getCourseNamTable();
			List<CourseNum> courses = new ArrayList<CourseNum>();
			for (String courseNam : klass.getCourseNamList()) {
				CourseNum courseNum = new CourseNum();
				courseNum.course = Stutas.getCourseMap().get(courseNam);
				courseNum.num = 0;

				for (CourseNum courseNumber : courses) {
					if (courseNumber.course.equals(Stutas.getCourseMap().get(
							courseNam))) {
						System.out.println("发现重复:" + courseNumber.course);
					}
				}
				courses.add(courseNum);
			}
			for (int i = 0; i < classTable.length; i++) {
				for (int j = 0; j < classTable[i].length; j++) {
					if (classTable[i][j] == null) {
						continue;
					}
					Course course = Stutas.getCourseMap().get(classTable[i][j]);
					if (course == null) {
						System.out.println("Null  name:" + classTable[i][j]
								+ "\n");
						continue;
					}

					boolean isNew = true;
					for (CourseNum courseNum : courses) {

						if (courseNum.course == null) {
							System.out.println("courseNull\n");
							return;
						}
						if (courseNum.course.equals(course)) {
							courseNum.num++;
							isNew = false;
						}
					}
					if (isNew) {
						CourseNum courNum = (new CourseNum());
						courNum.course = course;
						courNum.num++;
						courses.add(courNum);
					}
				}
			}
			List<Course> conflectList = new ArrayList<>();
			for (CourseNum courseNum : courses) {
				if (courseNum.num > courseNum.course.getNumber()) {
					for (int i = 0; i < courseNum.num
							- courseNum.course.getNumber(); i++) {
						removeTime(courseNum.course, null);
					}
					continue;
				}
				if (courseNum.num < courseNum.course.getNumber()) {
					for (int i = 0; i < -courseNum.num
							+ courseNum.course.getNumber(); i++) {
						conflectList.add(courseNum.course);
					}
					continue;
				}
			}
			int index = 0;
			while (true) {
				if (index == conflectList.size()) {
					break;
				}
				if (moveCourse(conflectList.get(index), null, null)) {
					conflectList.remove(index);
				} else {
					index++;
				}
				if (index == conflectList.size()) {
					break;
				}
			}
			System.out.println("检查完毕:" + klass);
		}
		System.out.println(report);
	}

	public static void removeTime(Course course, Time time) {
		if (time == null) {
			course.removeTime(course.getTimes().get(
					course.getTimes().size() - 1));
		} else {
			course.removeTime(time);
		}

	}

	public static boolean moveCourse(Course course, Time ordinaryTime,
			Time aimTime) {
		Klass klass = Stutas.getKlassMap().get(course.getKlassNam());
		Teacher teacher = Stutas.getTeacherMap().get(course.getTeacherNam());
		if (ordinaryTime != null && aimTime != null
				&& ordinaryTime.getDay() == aimTime.getDay()
				&& ordinaryTime.getNum() == aimTime.getNum()) {
			return true;
		}
		if (aimTime == null) {
			String[][] timeTable = klass.getCourseNamTable();
			// boolean[][] times = Locator.getLocatable(course);
			// for(int i=0;i<times.length;i++){
			// for(int j=0;j<times[i].length;j++){
			// if(times[i][j]){
			// if(ordinaryTime==null){
			// course.addTime(aimTime);
			// }else{
			// course.removeTime(ordinaryTime);
			// course.addTime(aimTime);
			// }
			// return true;
			// }
			// }
			// }
			for (int i = 0; i < timeTable.length; i++) {
				for (int j = 0; j < timeTable[i].length; j++) {
					if (timeTable[i][j] == null) {
						aimTime = new Time(i, j);
						if (teacher.getCourseNamTable()[i][j] == null) {
							if (ordinaryTime == null) {
								course.addTime(aimTime);
							} else {
								course.removeTime(ordinaryTime);
								course.addTime(aimTime);
							}
							return true;
						} else {
							Course course_1 = Stutas.getCourseMap().get(
									teacher.getCourseNamTable()[i][j]);
							Klass selectedKlass = Stutas.getKlassMap().get(
									course_1.getKlassNam());
							boolean[][] exchangables = getExchangableCourse(
									selectedKlass, i, j);
							for (int k = 0; k < exchangables.length; k++) {
								for (int l = 0; l < exchangables[k].length; l++) {
									if (exchangables[k][l]) {
										exchangeCourse(selectedKlass, i, j, k,
												l);
										if (ordinaryTime == null) {
											course.addTime(aimTime);
										} else {
											course.removeTime(ordinaryTime);
											course.addTime(aimTime);
										}
										return true;
									}
								}
							}
							return false;
							// aimTime = new Time(i, j);
							// nCourse =
							// Stutas.getCourseMap().get(teacher.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]);
							// if(!moveCourse(nCourse,ordinaryTime,null)){
							// continue;
							// }
						}
					}
				}
			}
			return false;
		} else {
			String[][] timeTable = klass.getCourseNamTable();
			if (timeTable[aimTime.getDay()][aimTime.getNum()] == null) {
				exchangeCourse(klass, ordinaryTime.getDay(),
						ordinaryTime.getNum(), aimTime.getDay(),
						aimTime.getNum());
				return true;
			} else {
				boolean[][] exchangables = getExchangableCourse(klass,
						ordinaryTime.getDay(), ordinaryTime.getNum());
				if (exchangables[aimTime.getDay()][aimTime.getNum()]) {
					exchangeCourse(klass, ordinaryTime.getDay(),
							ordinaryTime.getNum(), aimTime.getDay(),
							aimTime.getNum());
					return true;
				}
			}
			return false;
			// aimTime = new Time(i, j);
			// nCourse =
			// Stutas.getCourseMap().get(teacher.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]);
			// if(!moveCourse(nCourse,ordinaryTime,null)){
			// continue;
			// }
		}
	}

	// if(klass.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]!=null){
	// nCourse =
	// Stutas.getCourseMap().get(klass.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]);
	// if(!moveCourse(nCourse,aimTime,null)){
	// return false;
	// }
	// if(teacher.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]!=null){
	// nCourse =
	// Stutas.getCourseMap().get(teacher.getCourseNamTable()[aimTime.getDay()][aimTime.getNum()]);
	// if(!moveCourse(nCourse,aimTime,null)){
	// return false;
	// }
	// }
	// if(ordinaryTime==null){
	// course.addTime(aimTime);
	// }else{
	// course.removeTime(ordinaryTime);
	// }
	// }
	// return true;

	String Chromosome = "";
	Connection conn = null;
	Statement stmt1 = null;
	Statement stmt2 = null;
	Statement stmt3 = null;
	Statement stmt4 = null;
	File adaptFile = null;
	FileWriter adaptFileWriter = null;
	String strAdapt = "";
	int i = 0;

	public void connectDB(String url, String dbName, String userName,
			String userPassword) throws Exception {
		String connStr = "jdbc:microsoft:sqlserver://" + url.trim()
				+ ";DatabaseName=" + dbName.trim() + ";User=" + userName.trim()
				+ ";Password=" + userPassword.trim();
		Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver")
				.newInstance();
		conn = DriverManager.getConnection(connStr);
		stmt1 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);// 连接数据库的
		stmt2 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);// 连接数据库的
		stmt3 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);// 连接数据库的
		stmt4 = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);// 连接数据库的
		adaptFile = new File("C:\\adapt.txt");
		adaptFileWriter = new FileWriter(adaptFile);
	}

	public void getChromosome(int index) throws SQLException// 得到一条染色体
	{

		ResultSet rsJoinClass = stmt2
				.executeQuery("select Join_ID,Teacher_ID,Student_Num from SClass_Mission");// 选择所有的合班任务
		stmt4.executeUpdate("update SClass_Mission set ClassroomTime_ID = ''");
		stmt4.executeUpdate("update SClassroom_Time set isSelect = '0'");
		Chromosome = "";
		int topID = index;

		ResultSet rsClassroomID = null;
		int num = 0;
		rsJoinClass.first();
		if (rsJoinClass.getRow() == 1) {
			while (!rsJoinClass.isAfterLast())// 对所有的合班进行查找工作
			{
				// 查找满足当前Join_ID的ClassroomTime_ID,而且要满足没有硬冲突，硬冲突有：老师-时间，班级-时间,班级人数-教室容量
				rsClassroomID = stmt3
						.executeQuery("select top "
								+ Math.round(Math.random() * 5 + 1)
								+ 1
								+ " SClassroom_Time.ClassroomTime_ID from SClassroom_Time"
								+ " where SClassroom_Time.isSelect = '0'"
								+ " and Time_Quantum not in"
								+ " (    select Time_Quantum"
								+ " from SClassroom_Time, SClass_Mission"
								+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
								+ " and SClass_Mission.Teacher_ID = '"
								+ rsJoinClass.getString("Teacher_ID")
								+ "'"
								+ " )"
								+ " and Time_Quantum not in"
								+ " ("
								+ " select SClassroom_Time.Time_Quantum"
								+ " from SClassroom_Time, SClass_Mission"
								+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
								+ " and Join_ID in"
								+ " ("
								+ " select Join_ID from SClass_Mission"
								+ " where Join_ID in"
								+ " ("
								+ " select Join_Class.Join_ID from Join_Class,Class_Mission"
								+ " where Join_Class.Mission_ID= Class_Mission.Mission_ID"
								+ " and Class_ID in"
								+ " ("
								+ " select Class_Mission.Class_ID from Class_Mission,Join_Class"
								+ " where Class_Mission.Mission_ID = Join_Class.Mission_Id"
								+ " and Join_Class.Join_ID = '"
								+ rsJoinClass.getString("Join_ID") + "'" + " )"
								+ " )" + " )" + " )"
								+ " and SClassroom_Time.Classroom_Num >= "
								+ rsJoinClass.getInt("Student_Num"));

				rsClassroomID.last();// 选择满足条件的ClassroomTime_ID集合中的最后一条，目的是为了实现多样化
				if (rsClassroomID.getRow() >= 1)// 找到了组合
				{
					// 更新当前Join_ID对应的ClassroomTime_ID
					stmt4.executeUpdate("update SClass_Mission set ClassroomTime_ID = '"
							+ rsClassroomID.getString("ClassroomTime_ID")
							+ "' where Join_ID = '"
							+ rsJoinClass.getString("Join_ID") + "'");

					stmt4.executeUpdate("update SClassroom_Time set isSelect = '1' where ClassroomTime_ID = '"
							+ rsClassroomID.getString("ClassroomTime_ID") + "'");
					Chromosome += rsClassroomID.getString("ClassroomTime_ID")
							.trim();// 把基因加到染色体中
					rsJoinClass.next();

					// System.out.print(Chromosome);

				} else {
					num++;
					System.out.println("第" + num + "次基因组织查找失败\n");
				}
			}
		}
		// 存储染色体到数据库
		// System.out.print("insert into Chromosome(Chromosome_ID,Chromosome) values("
		// + String.valueOf(topID) + ",'" + Chromosome + "')");
		stmt4.executeUpdate("insert into Chromosome(Chromosome_ID,Chromosome,adapt,lastchanged) values("
				+ String.valueOf(topID) + ",'" + Chromosome + "',0,'000')");
		System.out.print(Chromosome);
		System.out.println("第" + index + "条染色体组织成功\n");
		if (topID == 29) {
			topID++;
		}
		return;
	}

	public void getAdapt() throws SQLException// 得到适应度
	{
		Chromosome = "";
		ResultSet rsChromosome = stmt1
				.executeQuery("select chromosome_ID,chromosome from Chromosome");// 得到所有的染色体，准备进行适应度判断
		ResultSet rsTmpAdapt = null;
		String teacherID = "";
		String classID = "";
		String chromosome_ID = "";
		char date = 0;// 临时的时间段
		char times = 0;// 同一天上课的次数
		int adapt = 0;// 适应度
		int tmpAdapt = 1;
		rsChromosome.first();
		while (!rsChromosome.isAfterLast())// 循环所有的染色体
		{
			adapt = 0;
			tmpAdapt = 1;
			times = 0;
			date = 0;
			chromosome_ID = "";
			classID = "";
			teacherID = "";
			Chromosome = rsChromosome.getString("Chromosome").trim();// 得到当前的染色体
			chromosome_ID = rsChromosome.getString("Chromosome_ID").trim();// 得到当前的染色体
			resetClassroomTime_ID();// 用Chromosome中的值填充SClass_Mission中的Classroomtime_ID字段，方便以后的数据库查找
			// 计算'一个教师的课不能排满一整天'适应度
			// 选择老师和他对应的时间段，按照老师和时间段的顺序
			rsTmpAdapt = stmt2
					.executeQuery("select Teacher_ID,Time_Quantum from SClassroom_Time,SClass_Mission"
							+ " where SClassroom_Time.ClassroomTime_ID=SClass_Mission.ClassroomTime_ID"
							+ " order by Teacher_ID,Time_Quantum asc");

			rsTmpAdapt.first();
			if (rsTmpAdapt.getRow() == 1)// 记录集不为空，说明老师，时间段存在
			{
				date = rsTmpAdapt.getString("Time_Quantum").charAt(0);
				teacherID = rsTmpAdapt.getString("Teacher_ID");
				rsTmpAdapt.next();
				while (!rsTmpAdapt.isAfterLast())// 循环所有的老师和他的时间段
				{

					if (teacherID == rsTmpAdapt.getString("Teacher_ID"))// 老师没有变
					{
						if (date == rsTmpAdapt.getString("Time_Quantum")
								.charAt(0))// 说明还是同一天上课
						{
							times++;// 同一天上课的次数加1
						} else // 不是同一天上课了
						{
							if (times <= 2)// 同一天上课的次数小于2
							{
								adapt = adapt + 10;
							} else// 同一天上课的次数大于2
							{
								adapt = adapt - 5;
							}
							times = 0;// 将同一天上课的次数清空
							date = rsTmpAdapt.getString("Time_Quantum").charAt(
									0);// 记录下一天的编号
						}
					} else// 老师变化了，所以同一天上课的次数得清空
					{
						teacherID = rsTmpAdapt.getString("Teacher_ID");// 得到新老师的编号
						times = 0;// 将同一天上课的次数清空
					}

					rsTmpAdapt.next();
				}
			}
			// 学生课表中的上课时间不能过分集中，应避免一天课程很满而另一天却一整天没课的情况。

			// 选择每个班级和他的上课时间段
			rsTmpAdapt = stmt2
					.executeQuery("select Class_ID,Time_Quantum from SClass_Mission,Join_Class,Class_Mission,SClassroom_Time"
							+ " where SClass_Mission.Join_ID = Join_Class.Join_ID"
							+ " and  SClassroom_Time.ClassroomTime_ID=SClass_Mission.ClassroomTime_ID"
							+ " and Join_Class.Mission_ID = Class_Mission.Mission_ID"
							+ " order by Class_ID , SClass_Mission.ClassroomTime_ID asc");

			rsTmpAdapt.first();
			if (rsTmpAdapt.getRow() == 1)// 存在记录集则循环遍历
			{
				date = rsTmpAdapt.getString("Time_Quantum").charAt(0);
				classID = rsTmpAdapt.getString("Class_ID");
				rsTmpAdapt.next();
				while (!rsTmpAdapt.isAfterLast())// 遍历所有的班级和时间段
				{

					if (classID == rsTmpAdapt.getString("Class_ID"))// 依旧是这个班级，则，将时间差相乘
					{
						tmpAdapt *= (rsTmpAdapt.getString("Time_Quantum")
								.charAt(0) - date + 1);// 时间差相乘，+1是为了防止乘积为0
					} else// 班级改变，则计算新班级的适应度
					{
						classID = rsTmpAdapt.getString("Class_ID");// 得到新班级的编号
						adapt += tmpAdapt;// 将上一个班级的适应度加到总适应度
						tmpAdapt = 1;// 防止乘积为0
					}
					rsTmpAdapt.next();
				}
			}

			// 尽量在早上安排必修课，而下午安排选修课，晚上尽量不排课

			// 选择所有的课程及其时间段
			rsTmpAdapt = stmt2
					.executeQuery("select Course.isImportant,Time_Quantum from Course,SClass_Mission,SClassroom_Time"
							+ " where Course.Course_ID = SClass_Mission.Course_ID"
							+ " and  SClassroom_Time.ClassroomTime_ID=SClass_Mission.ClassroomTime_ID");

			rsTmpAdapt.first();
			if (rsTmpAdapt.getRow() == 1)// 有记录集则遍历
			{
				while (!rsTmpAdapt.isAfterLast())// 遍历所有的课程和他的时间段
				{

					if (rsTmpAdapt.getString("isImportant").charAt(0) == '1')// 此门课程比较重要
					{
						if (rsTmpAdapt.getString("Time_Quantum").charAt(1) < '3')// 此门重要课程在上午上课，则提高适应度
						{
							adapt += 2;
						} else if (rsTmpAdapt.getString("Time_Quantum").charAt(
								1) < '5')// 此门重要课程在下午上课，则减少适应度
						{
							adapt -= 1;
						} else// 晚上上课，减少更多的适应度，因为尽量不要晚上排课
						{
							adapt -= 1;
						}
					} else {
						if (rsTmpAdapt.getString("Time_Quantum").charAt(1) < '3')// 非重要课程在上午上课，则较少适应度
						{
							adapt -= 1;
						} else if (rsTmpAdapt.getString("Time_Quantum").charAt(
								1) < '5')// 非重要课程在下午上课，则提高适应度
						{
							adapt += 2;
						} else// 晚上尽量不要上课
						{
							adapt -= 1;
						}
					}
					rsTmpAdapt.next();
				}
			}
			// 尽可能满足个别教师的特殊上课时间要求

			// 选出老师，老师权值，老师对上课时间要求和实际上课时间的对应集合
			rsTmpAdapt = stmt2
					.executeQuery("select teacher.Teacher_weight,teacher.request,time_quantum"
							+ " from  teacher,Sclass_mission,Sclassroom_time"
							+ " where SClassroom_Time.ClassroomTime_ID=SClass_Mission.ClassroomTime_ID"
							+ " and  Sclass_mission.teacher_id = teacher.teacher_id");
			rsTmpAdapt.first();
			if (rsTmpAdapt.getRow() == 1)// 记录不空，则遍历
			{
				while (!rsTmpAdapt.isAfterLast())// 遍历所有的老师，老师权值，老师对上课时间要求和实际上课时间的对应
				{
					// 按照老师的权值，提出要求的多少也不一样，权值大的老师可以提更多的要求，比如权值2，可以提2个要求，权值4,可以提4个要求
					for (int index = 1; index < rsTmpAdapt.getString(
							"Teacher_Weight").charAt(0) - 48; index++) {
						if (rsTmpAdapt.getString("request").matches(
								rsTmpAdapt.getString("time_quantum") + ".")) {// 当前老师上课的时间段和老师提的要求发生了冲突，所以适应度应该降低
							adapt -= 2 * Integer.valueOf(rsTmpAdapt
									.getString("Teacher_Weight"));// 根据权值的不同，降低的数值也不同，全值大的老师降低的多
						}
					}
					rsTmpAdapt.next();
				}
			}
			// 人数

			// 选择所有和班人数和上课教室的容纳人数集合
			rsTmpAdapt = stmt2
					.executeQuery("select SClass_Mission.Student_Num,Classroom.Classroom_Num"
							+ " from SClass_Mission,Classroom,SClassroom_Time"
							+ " where SClass_Mission.ClassroomTime_Id = SClassroom_Time.classroomTime_ID"
							+ " and Sclassroom_time.Classroom_ID = Classroom.Classroom_ID");
			rsTmpAdapt.first();
			if (rsTmpAdapt.getRow() == 1) {
				while (!rsTmpAdapt.isAfterLast()) {
					if ((rsTmpAdapt.getInt("Classroom_Num") > rsTmpAdapt
							.getInt("Student_Num") * 1.3)
							|| (rsTmpAdapt.getInt("Classroom_Num") < rsTmpAdapt
									.getInt("Student_Num") * 1.1))// 如果教室容纳人数：合班人数大于1.3或者小于1.1，则不合适，应该减少适应度
					{
						adapt -= 5;
					} else// 比率在1.1-1.3之间比较合适，应该增加适应度
					{
						adapt += 5;
					}
					rsTmpAdapt.next();
				}
			}
			// 将当前染色体的适应度存入和他对应染色体的记录中
			stmt3.executeUpdate("update Chromosome set Adapt=" + adapt
					+ "where Chromosome_ID ='" + chromosome_ID + "'");
			rsChromosome.next();
		}

	}

	public void choice() throws SQLException// 选择，将最优秀的6条染色体复制给最不优秀的6条染色体
	{
		// 选择最优秀的6条染色体
		ResultSet rsTop6Chromosome = stmt1
				.executeQuery("select top 6 chromosome from chromosome order by adapt desc");
		// 选择最不优秀的6条染色体编号，以便更新
		ResultSet rsLow6ChromosomeID = stmt2
				.executeQuery("select top 6 chromosome_ID from chromosome order by adapt asc");
		rsTop6Chromosome.first();
		rsLow6ChromosomeID.first();
		if (rsLow6ChromosomeID.getRow() != 0 && rsTop6Chromosome.getRow() != 0)// 如果最优秀的，和不优秀的都存在集合
		{
			while (!rsLow6ChromosomeID.isAfterLast()) {
				if (rsTop6Chromosome.isAfterLast())// 优秀的染色体少于6条，则返回
				{
					return;
				}
				// 更新，复制染色体操作
				stmt3.executeUpdate("update chromosome set chromosome = '"
						+ rsTop6Chromosome.getString("chromosome")
						+ "' where chromosome_ID = "
						+ rsLow6ChromosomeID.getString("chromosome_ID").trim());
				rsTop6Chromosome.next();
				rsLow6ChromosomeID.next();
			}

		}
	}

	public void exchange() throws SQLException// 交叉，等位交叉，1-2，3-4，5-6，7-8，9-10，11-12交叉
	{
		String Chromosome1 = "";// 单数染色体
		String Chromosome2 = "";// 双数染色体
		String changeChromosome = "";// 中间变量
		String subChromosome1 = "";// 交叉段1
		String subChromosome2 = "";// 交叉段2
		String chromosomeID1 = "";
		String chromosomeID2 = "";
		int index = 0;
		// 选择最优秀的12条染色体
		ResultSet rsChromosome = stmt1
				.executeQuery("select top 12 * from chromosome order by Adapt desc");

		// 选择Join_ID按照升序排列，为了对应Join_ID
		ResultSet rsJoinID = stmt2
				.executeQuery("select Join_ID from SClass_Mission order by Join_ID ASC");
		rsJoinID.first();
		if (rsJoinID.getRow() == 0) {
			return;
		}
		rsChromosome.first();
		if (rsChromosome.getRow() == 1)// 存在优秀的染色体
		{
			rsChromosome.next();
			while (!rsChromosome.isAfterLast()) {
				changeChromosome = "";
				Chromosome1 = rsChromosome.getString("chromosome");// 得到单数染色体
				chromosomeID1 = rsChromosome.getString("chromosome_ID");// 得到单数染色体bianhao
				rsChromosome.next();
				if (rsChromosome.isAfterLast()) {
					return;// 双数染色体如果不存在，则返回
				}
				Chromosome2 = rsChromosome.getString("chromosome");// 得到双数染色体
				chromosomeID2 = rsChromosome.getString("chromosome_ID");// 得到单数染色体bianhao
				index = Math.round((int) (Math.random() * Chromosome1.trim()
						.length()));// 随机生成小于染色体长度的int变量
				index = index - index % 4;// 使得int变量是4的整数倍，因为每个classroomtime_ID（一个基因）是4个长度
				rsJoinID.absolute(index / 4 + 1);// 定位到和index相对应的Join_ID,方便以后的记录更新
				subChromosome1 = Chromosome1.substring(index, index + 4);// 得到单数染色体的交叉段
				subChromosome2 = Chromosome2.substring(index, index + 4);// 得到双数染色体的交叉段
				changeChromosome = Chromosome1.substring(0, index);
				changeChromosome += subChromosome2;
				changeChromosome += Chromosome1.substring(index + 4);// 上面三句话得到了单数染色体交叉后的新染色体
				// 更新单数染色体
				stmt3.executeUpdate("update chromosome set chromosome ='"
						+ changeChromosome + "', lastchanged = '"
						+ rsJoinID.getString("Join_ID")
						+ "' where Chromosome_ID = '" + chromosomeID1.trim()
						+ "'");

				changeChromosome = "";
				changeChromosome = Chromosome2.substring(0, index);
				changeChromosome += subChromosome1;
				changeChromosome += Chromosome2.substring(index + 4);// 上面三句话得到了双数染色体交叉后的新染色体
				// 更新双数染色体
				stmt3.executeUpdate("update chromosome set chromosome ='"
						+ changeChromosome + "', lastchanged = '"
						+ rsJoinID.getString("Join_ID")
						+ "' where Chromosome_ID = '" + chromosomeID2.trim()
						+ "'");
				rsChromosome.next();
			}
		}
	}

	public void sloveYCT() throws SQLException// 用Chromosome存储当前需要判断和解决的YCT问题
	{
		ResultSet rsChromosome = stmt1.executeQuery("select * from Chromosome");// 得到所有染色体
		String lastChanged = "";
		String timeQuantum = "";
		rsChromosome.first();
		ResultSet rsRe = null;
		ResultSet rsResult = null;
		ResultSet rsResult2 = null;
		while (!rsChromosome.isAfterLast())// 针对每一个染色体
		{
			Chromosome = rsChromosome.getString("Chromosome").trim();// 得到当前的染色体
			lastChanged = "";
			timeQuantum = "";
			if (!rsChromosome.getString("lastchanged").trim().equals(""))// 如果当前染色体有修改，修改可能有交叉，变异
			{

				resetClassroomTime_ID();// 用染色体填充classroomtime_ID方便查询
				lastChanged = rsChromosome.getString("lastchanged").trim();// 得到染色体更新处所对应的Join_ID
				// 时间和老师冲突
				// 得到和改变后的Join_ID所对应的Teacher_ID有时间冲突的Join_ID
				rsRe = stmt2
						.executeQuery("select Join_ID,student_Num,Teacher_ID,SClass_Mission.classroomTime_ID from SClass_Mission, SClassroom_Time"
								+ " where SClass_Mission.classroomtime_ID = SClassroom_Time.classroomtime_ID"
								+ " and SClass_Mission.teacher_ID = (select Teacher_ID from SClass_Mission where Join_ID = '"
								+ lastChanged
								+ "'"
								+ " and SClassroom_Time.time_Quantum = (select time_Quantum from Sclass_Mission,SClassroom_time"
								+ " where SClass_Mission.classroomtime_ID = SClassroom_Time.classroomtime_ID"
								+ " and Join_ID = '" + lastChanged + "'))");
				rsRe.last();
				if (rsRe.getRow() == 2)// 存在相冲突的Join_ID
				{
					if (rsRe.getString("Join_ID").trim().equals(lastChanged)) {
						rsRe.first();// 移动记录集到非改变过的Join_ID所对应的记录集
					}
					// 找没有硬冲突的合适解
					rsResult = stmt3
							.executeQuery("select top "
									+ Math.round(Math.random() * 5 + 1 + 1)
									+ " SClassroom_Time.ClassroomTime_ID from SClassroom_Time"
									+ " where SClassroom_Time.isSelect = '0'"
									+ " and Time_Quantum not in"
									+ " (   select Time_Quantum"
									+ " from SClassroom_Time, SClass_Mission"
									+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
									+ " and SClass_Mission.Teacher_ID = '"
									+ rsRe.getString("Teacher_ID")
									+ "'"
									+ " )"
									+ " and Time_Quantum not in"
									+ " ("
									+ " select SClassroom_Time.Time_Quantum"
									+ " from SClassroom_Time, SClass_Mission"
									+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
									+ " and Join_ID in"
									+ " ("
									+ " select Join_ID from SClass_Mission"
									+ " where Join_ID in"
									+ " ("
									+ " select Join_Class.Join_ID from Join_Class,Class_Mission"
									+ " where Join_Class.Mission_Id = Class_Mission.Mission_ID"
									+ " and Class_ID in"
									+ " ("
									+ " select Class_Mission.Class_ID from Class_Mission,Join_Class"
									+ " where Class_Mission.Mission_ID = Join_Class.Mission_Id"
									+ " and Join_Class.Join_ID = '"
									+ rsRe.getString("Join_ID") + "'" + " )"
									+ " )" + " )" + " )"
									+ " and SClassroom_Time.Classroom_Num >= "
									+ rsRe.getInt("Student_Num"));
					rsResult.last();

					if (rsResult.getRow() != 0) {
						// 找到合适解，更新记录集
						stmt4.executeUpdate("update SClass_Mission set classroomtime_ID = '"
								+ rsResult.getString("ClassroomTime_ID")
								+ "' where Join_ID = "
								+ rsRe.getString("Join_ID").trim());
					} else {
						System.out.print("教室资源紧张！赶快挣钱买房子!");
					}
				}

				// 时间和班级冲突
				// 得到改变Join_ID对应的记录所上课的时间段

				rsResult2 = stmt2
						.executeQuery("select time_Quantum from SClass_Mission, SClassroom_Time"
								+ " where SClass_Mission.classroomtime_ID = SClassroom_Time.classroomtime_ID"
								+ " and Join_ID = '" + lastChanged + "'");
				rsResult2.last();
				if (rsResult2.getRow() != 0) {
					timeQuantum = rsResult2.getString("time_Quantum");
					rsRe = stmt2
							.executeQuery("select SClass_Mission.Join_ID,Teacher_ID,Student_Num,SClass_Mission.Classroomtime_ID "
									+ " from SClass_Mission,SClassroom_Time"
									+ " where SClass_Mission.Classroomtime_ID = SClassroom_time.Classroomtime_ID"
									+ " and Time_Quantum = '"
									+ timeQuantum
									+ "'"
									+ " and Join_ID in"
									+ " ( Select Join_ID from Join_Class,Class_Mission"
									+ " where Join_Class.Mission_ID = Class_Mission.Mission_ID"
									+ " and Class_ID in"
									+ " ( Select Class_ID from Join_Class,Class_mission"
									+ " where Join_Class.Mission_Id = Class_Mission.Mission_Id"
									+ " and Join_ID ='"
									+ lastChanged
									+ "'"
									+ " )" + " ) ");
					rsRe.first();
					while (!rsRe.isAfterLast()) {
						if (!rsRe.getString("join_ID").trim()
								.equals(lastChanged)) {
							rsResult = stmt3
									.executeQuery("select top "
											+ Math.round(Math.random() * 5 + 1)
											+ " SClassroom_Time.ClassroomTime_ID from SClassroom_Time"
											+ " where SClassroom_Time.isSelect = '0'"
											+ " and Time_Quantum not in"
											+ " (    select Time_Quantum"
											+ " from SClassroom_Time, SClass_Mission"
											+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
											+ " and SClass_Mission.Teacher_ID = '"
											+ rsRe.getString("Teacher_ID")
											+ "'"
											+ " )"
											+ " and Time_Quantum not in"
											+ " ("
											+ " select SClassroom_Time.Time_Quantum"
											+ " from SClassroom_Time, SClass_Mission"
											+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
											+ " and Join_ID in"
											+ " ("
											+ " select Join_ID from SClass_Mission"
											+ " where Join_ID in"
											+ " ("
											+ " select Join_Class.Join_ID from Join_Class,Class_Mission"
											+ " where Join_Class.Mission_Id = Class_Mission.Mission_ID"
											+ " and Class_ID in"
											+ " ("
											+ " select Class_Mission.Class_ID from Class_Mission,Join_Class"
											+ " where Class_Mission.Mission_ID = Join_Class.Mission_Id"
											+ " and Join_Class.Join_ID = '"
											+ rsRe.getString("Join_ID")
											+ "'"
											+ " )"
											+ " )"
											+ " )"
											+ " )"
											+ " and SClassroom_Time.Classroom_Num >= "
											+ rsRe.getInt("Student_Num"));
							rsResult.last();
							if (rsResult.getRow() != 0) {
								stmt4.executeUpdate("update SClass_Mission set classroomtime_ID = '"
										+ rsResult
												.getString("ClassroomTime_ID")
										+ "' where Join_ID = "
										+ rsRe.getString("Join_ID").trim());
							} else {
								System.out.print("教室资源紧张！赶快挣钱买房子!");
							}
						}
						rsRe.next();
					}

				}

				// 教室容量冲突！只可能由变异引起
				rsRe = stmt2
						.executeQuery("select Teacher_ID,Join_ID,Student_Num,classroom.Classroom_Num from SClass_Mission,SClassroom_Time,classroom"
								+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
								+ " and SClassroom_Time.classroom_ID = classroom.classroom_ID"
								+ " and Join_ID ='" + lastChanged + "'");
				rsRe.last();
				if (rsRe.getRow() != 0) {
					if (rsRe.getInt("Student_Num") > rsRe
							.getInt("Classroom_Num"))// 表示有冲突了
					{
						rsResult = stmt3
								.executeQuery("select top "
										+ Math.round(Math.random() * 5 + 1)
										+ " SClassroom_Time.ClassroomTime_ID from SClassroom_Time"
										+ " where SClassroom_Time.isSelect = '0'"
										+ " and Time_Quantum not in"
										+ " (    select Time_Quantum"
										+ " from SClassroom_Time, SClass_Mission"
										+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
										+ " and SClass_Mission.Teacher_ID = '"
										+ rsRe.getString("Teacher_ID")
										+ "'"
										+ " )"
										+ " and Time_Quantum not in"
										+ " ("
										+ " select SClassroom_Time.Time_Quantum"
										+ " from SClassroom_Time, SClass_Mission"
										+ " where SClass_Mission.ClassroomTime_ID = SClassroom_Time.ClassroomTime_ID"
										+ " and Join_ID in"
										+ " ("
										+ " select Join_ID from SClass_Mission"
										+ " where Join_ID in"
										+ " ("
										+ " select Join_Class.Join_ID from Join_Class,Class_Mission"
										+ " where Join_Class.Mission_Id = Class_Mission.Mission_ID"
										+ " and Class_ID in"
										+ " ("
										+ " select Class_Mission.Class_ID from Class_Mission,Join_Class"
										+ " where Class_Mission.Mission_ID = Join_Class.Mission_Id"
										+ " and Join_Class.Join_ID = '"
										+ lastChanged
										+ "'"
										+ " )"
										+ " )"
										+ " )"
										+ " )"
										+ " and SClassroom_Time.Classroom_Num >= "
										+ rsRe.getInt("Student_Num"));
						rsResult.last();
						if (rsResult.getRow() != 0) {
							stmt4.executeUpdate("update SClass_Mission set classroomtime_ID = '"
									+ rsResult.getString("ClassroomTime_ID")
									+ "' where Join_ID = "
									+ rsRe.getString("Join_ID").trim());
						} else {
							System.out.print("教室资源紧张！赶快挣钱买房子!");
						}
					}
				}

			}
			rsChromosome.next();
		}
		stmt4.executeUpdate("update chromosome set lastchanged = ''");
	}

	public void resetClassroomTime_ID() throws SQLException// 利用Chromosome填充SClass_Mission.ClassroomTime_ID,SClassroom_Time.isSelect
	{
		int index = 0;
		if (i == 1) {
			int a = 0;
			a++;
		}
		stmt2.executeUpdate("update SClassroom_Time set isSelect = '0'");
		ResultSet rsJoinID = stmt3
				.executeQuery("select Join_ID from SClass_Mission order by Join_ID ASC");
		rsJoinID.first();
		for (index = 0; index <= Chromosome.length() - 4; index += 4) {

			stmt2.executeUpdate("update SClass_Mission set ClassroomTime_ID = '"
					+ Chromosome.substring(index, index + 4)
					+ "'"
					+ " where Join_ID = '"
					+ rsJoinID.getString("Join_ID")
					+ "'");
			stmt2.executeUpdate("update SClassroom_Time set isSelect = '1' where ClassroomTime_ID = '"
					+ Chromosome.substring(index, index + 4) + "'");
			rsJoinID.next();
		}

	}

	public void variate() throws SQLException// 变异
	{
		int index = 0;
		String newChromosome = "";
		String TopClassroomTimeID = "";
		String rndClassroomTimeID = "";
		int rndNum = 0;
		ResultSet rsJoinID = stmt1
				.executeQuery("select Join_ID from SClass_Mission order by Join_ID asc");
		ResultSet rsTopClassroomTimeID = stmt3
				.executeQuery("select top 1 ClassroomTime_ID from SClassroom_Time order by ClassroomTime_ID desc");
		ResultSet rsChromosome = stmt2
				.executeQuery("select * from chromosome order by adapt desc");
		rsChromosome.first();
		rsJoinID.first();
		rsTopClassroomTimeID.first();
		if (rsTopClassroomTimeID.getRow() == 0) {
			return;
		} else {
			TopClassroomTimeID = rsTopClassroomTimeID.getString(1);
		}
		if (rsChromosome.getRow() == 1) {
			System.out.println("   最高的适应度为：" + rsChromosome.getInt("adapt")
					+ "        染色体为："
					+ rsChromosome.getString("chromosome").trim());
			strAdapt = "\r\n" + String.valueOf(rsChromosome.getInt("adapt"));

			try {
				adaptFileWriter.write(strAdapt);
				adaptFileWriter.flush();
			} catch (Exception e) {
				System.out.println("adaptFileWriter.write(strAdapt)");
			}
			rsChromosome.next();
			while (!rsChromosome.isAfterLast()) {
				if (Math.random() < 0.5) {
					rndNum = (int) Math.round(Math.random()
							* Integer.valueOf(TopClassroomTimeID));
					if (rndNum < 10) {
						rndClassroomTimeID = "000" + String.valueOf(rndNum);
					} else if (rndNum < 100) {
						rndClassroomTimeID = "00" + String.valueOf(rndNum);
					} else if (rndNum < 1000) {
						rndClassroomTimeID = "0" + String.valueOf(rndNum);
					} else {
						rndClassroomTimeID = String.valueOf(rndNum);
					}
					Chromosome = rsChromosome.getString("chromosome").trim();// 得到这个随机的染色体
					if (Chromosome.length() < 4) {
						return;
					}
					index = Math.round((int) (Math.random() * Chromosome
							.length()));
					index = index - index % 4;
					rsJoinID.absolute(index / 4 + 1);
					newChromosome = Chromosome.substring(0, index);
					newChromosome += rndClassroomTimeID;// 加上变异后的值
					newChromosome += Chromosome.substring(index + 4);
					// 更新染色体
					// System.out.print("变异片段 !"+ rndClassroomTimeID+ "!");

					// System.out.print("\n" +Chromosome + "\n");
					// System.out.print(newChromosome);
					// 将更新基因所对应的合班任务编号存入数据库
					stmt4.executeUpdate("update Chromosome set Chromosome = '"
							+ newChromosome + "', lastChanged = '"
							+ rsJoinID.getString("Join_ID")
							+ "' where Chromosome_ID = '"
							+ rsChromosome.getString("Chromosome_ID") + "'");

				}
				rsChromosome.next();
			}
		}

	}

	public void setResult() throws SQLException {
		// 得到最优秀的染色体
		i = 1;
		ResultSet rsResult = stmt1
				.executeQuery("select top 1 Chromosome from chromosome order by adapt desc");
		rsResult.first();
		if (rsResult.getRow() == 1) {
			Chromosome = rsResult.getString("Chromosome");
			resetClassroomTime_ID();
		}
	}

	public void initialize() throws SQLException {
		stmt1.executeUpdate("delete from chromosome");
		stmt1.executeUpdate("delete from SClass_Mission");
		stmt1.execute("if   exists   (select   *   from   dbo.sysobjects   "
				+ " where   id   =   object_id(N'[dbo].[a]') "
				+ " and   OBJECTPROPERTY(id,   N'IsView')   =   1)"
				+ " drop   view   [dbo].[a]");
		stmt1.execute("if   exists   (select   *   from   dbo.sysobjects   "
				+ " where   id   =   object_id(N'[dbo].[b]') "
				+ " and   OBJECTPROPERTY(id,   N'IsView')   =   1)"
				+ " drop   view   [dbo].[b]");
		stmt1.execute("create view a as select distinct Teacher_ID, course_ID,Join_ID"
				+ " from Join_Class,Class_Mission"
				+ " where Class_Mission.Mission_ID = Join_Class.Mission_ID");
		stmt1.execute("create view b as select Join_ID,sum(Class_Num) as num"
				+ " from Class_Mission,Join_Class,class"
				+ " where Class_Mission.Mission_ID = Join_Class.Mission_ID"
				+ " and Class_Mission.class_ID = class.Class_ID"
				+ " group by Join_ID");
		stmt1.execute("insert into SClass_Mission(teacher_ID,course_ID,Join_ID,Student_Num)"
				+ " select distinct Teacher_ID, course_ID,a.Join_ID,num"
				+ " from a,b" + " where a.Join_Id = b.Join_ID");
		stmt1.execute("delete from SClassroom_Time");
		ResultSet rsRoom = stmt1
				.executeQuery("select classroom_ID,classroom_Num from classroom");
		rsRoom.first();
		int index = 0;
		int index1 = 0;
		String strIndex = "";
		if (rsRoom.getRow() == 1) {
			while (!rsRoom.isAfterLast()) {

				for (index1 = 0; index1 < 25; index1++) {
					index++;
					if (index < 10) {
						strIndex = "000" + String.valueOf(index);
					} else if (index < 100) {
						strIndex = "00" + String.valueOf(index);
					} else if (index < 1000) {
						strIndex = "0" + String.valueOf(index);
					} else {
						strIndex = String.valueOf(index);
					}
					stmt2.executeUpdate("insert into SClassroom_Time(ClassroomTime_ID,Classroom_ID,Classroom_Num,Time_Quantum,isSelect)"
							+ " values ('"
							+ strIndex
							+ "','"
							+ rsRoom.getString("Classroom_ID")
							+ "',"
							+ rsRoom.getInt("classroom_Num")
							+ ",'"
							+ String.valueOf(Math.round(index1 / 5) + 1)
							+ String.valueOf(index1 % 5 + 1) + "','0')");
				}
				rsRoom.next();

			}
		}
	}

	// public static void main(String[] args) throws Exception
	// {
	// int index = 0;
	// Locator myPKSystem= new Locator();
	// myPKSystem.connectDB("localhost:1433","PKSystem","sa","123");//连接数据库
	// myPKSystem.initialize();
	// //ceshi kishi
	// //myPKSystem.sloveYCT();
	// //ceshi jiesu
	// for(int i=0; i<8; i++)
	// {
	// myPKSystem.getChromosome(i);
	// }
	// for(index = 0; index < 5; index ++)
	// {
	// System.out.print("第"+index + "代");
	// myPKSystem.getAdapt();
	// myPKSystem.choice();
	// myPKSystem.getAdapt();
	// myPKSystem.exchange();
	// myPKSystem.sloveYCT();
	// myPKSystem.variate();
	// myPKSystem.sloveYCT();
	//
	// }
	//
	// //myPKSystem.setResult();
	//
	// System.out.print("Java遗传算法排课系统顺利完成！");
	// }

}
