package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Course;
import entity.Klass;
import entity.Rule;
import entity.Subject;
import entity.Teacher;
import entity.Time;

public class Stutas {

	public static Map<String, Teacher> teacherMap;
	public static Map<String, Klass> klassMap;
	public static Map<String, Subject> subjectMap;
	public static Map<String, Rule> ruleMap;
	public static Map<String, Course> courseMap;

	static{
		if(read()){
			//init();
		}
	}
	public static Map<String, Course> getCourseMap() {
		return courseMap;
	}

	public static void init() {
		try {
			Field[] fields = Stutas.class.getFields();
			for (Field f : fields) {
				Method get = Stutas.class.getMethod(
						"get" + Character.toUpperCase(f.getName().charAt(0))
								+ f.getName().substring(1), (Class<?>[]) null);
				Object obj = get.invoke(Stutas.class.newInstance(),
						(Object[]) null);
				if (obj == null) {
					Method m = Stutas.class.getMethod(
							"set"
									+ Character.toUpperCase(f.getName().charAt(
											0)) + f.getName().substring(1),
							f.getType());

					m.invoke(Stutas.class.newInstance(),
							HashMap.class.newInstance());
				}
			}
			String[][] xlsResult = ExcelOperate.readXls("13下工作安排 (1).xls");
			int num = 0;
			String klassNam;
			String grade;
			String teacherNam;
			String subjectNam;
			for (int i = 2; !xlsResult[1][i].equals("合计"); i++) {
				subjectNam = xlsResult[1][i];
				if (subjectNam == null || subjectNam.equals("")) {
					continue;
				}
				Subject subject;
				if (!subjectMap.containsKey(subjectNam)) {
					subject = new Subject();
					subject.setName(subjectNam);
					subjectMap.put(subject.toString(), subject);
				} else {
					subject = subjectMap.get(subjectNam);
				}
				for (int j = 2; xlsResult[j] != null && xlsResult[j][i] != null; j++) {
					if (xlsResult[j][i].equals("")) {
						continue;
					}
					if (isNum(xlsResult[j][i])) {
						num = Integer.parseInt(xlsResult[j][i]);
						continue;
					} else {
						teacherNam = xlsResult[j][i];
						Teacher teacher;
						if (!teacherMap.containsKey(teacherNam)) {
							teacher = new Teacher();
							teacher.setName(teacherNam);
							teacherMap.put(teacher.toString(), teacher);
						} else {
							teacher = teacherMap.get(teacherNam);
						}
						klassNam = xlsResult[j][1];
						Klass klass;
						grade = null;
						if (xlsResult[j][0].trim().equals("一")) {
							grade = "1";
						} else if (xlsResult[j][0].trim().equals("二")) {
							grade = "2";
						} else if (xlsResult[j][0].trim().equals("三")) {
							grade = "3";
						} else if (xlsResult[j][0].trim().equals("四")) {
							grade = "4";
						} else if (xlsResult[j][0].trim().equals("五")) {
							grade = "5";
						} else if (xlsResult[j][0].trim().equals("六")) {
							grade = "6";
						}
						if (!klassMap
								.containsKey(Klass.getNam(grade, klassNam))) {
							klass = new Klass();
							klass.setClsNum(Integer.parseInt(klassNam));
							klass.setGrade(grade);
							klassMap.put(klass.toString(), klass);
						} else {
							klass = klassMap.get(Klass.getNam(grade, klassNam));
						}
						Course course = new Course();
						course.setKlassNam(klass.toString());
						course.setSubjectNam(subject.toString());
						course.setTeacherNam(teacher.toString());
						course.setNumber(num);
						courseMap.put(course.toString(), course);
						course.associate();
					}
				}
			}

			final String[] c1 = new String[] { "语", "语文", "数", "数学", "英", "英语",
					"阅", "书法", "音", "音乐", "体", "体育", "美", "美术", "科", "科学", "劳",
					"劳技", "技",	"劳技","品", "思品", "队", "班队", "研", "研究", "电", "信息", "校",
					"阅读", "生", "国学", "健", "国学", "书", "书法" };
			String[][] cResult = txtOperator.readTxtFile("12_course.txt", 23,
					31);
			String[][] classTemp = null;
			for (int i = 0; i < 23; i++) {
				classTemp = new String[5][6];
				for (int j = 1; j < cResult[i].length; j++) {
					for (int k = 0; k < c1.length; k++) {
						if (cResult[i][j] != null
								&& cResult[i][j].equals(c1[k])) {
							classTemp[(j-1)/6][(j-1)%6] = c1[k+1];
//							String kName = cResult[i][0];
//							if (!klassMap.containsKey(kName)) {
//								System.out.println(kName);
//								continue;
//							}
//							for (String courseNam : klassMap.get(kName)
//									.getCourseNamList()) {
//								Course course = courseMap.get(courseNam);
//								if (course.getSubjectNam().equals(subName)) {
//									course.addTime(new Time((j - 1) / 6,
//											(j - 1) % 6));
//									break;
//								}
//							}
						}
					}
				}
				System.out.println(cResult[i][0]);
				locateCourseTime(classTemp,cResult[i][0]);
				System.out.println("\n");
			}
			Locator.check_KT();
			Locator.check();
			Locator.check_Sp();
			Locator.check_2();
			
			save();
			
//			String[][] arrange13 = ExcelOperate.readXls("13下工作安排.xls");
//			teacherMap.clear();
//			for (int i = 2; !arrange13[1][i].equals("合计"); i++) {
//				subjectNam = arrange13[1][i];
//				if (subjectNam == null || subjectNam.equals("")) {
//					continue;
//				}
//				Subject subject;
//				if (!subjectMap.containsKey(subjectNam)) {
//					subject = new Subject();
//					subject.setName(subjectNam);
//					subjectMap.put(subject.toString(), subject);
//				} else {
//					subject = subjectMap.get(subjectNam);
//				}
//				for (int j = 3; arrange13[j] != null && arrange13[j][i] != null && arrange13[j][1] ==null; j++) {
//					if (arrange13[j][i].equals("")) {
//						continue;
//					}
//					if (isNum(arrange13[j][i])) {
//						num = Integer.parseInt(arrange13[j][i]);
//						continue;
//					}
//					klassNam = arrange13[j][1];
//					Klass klass;
//					grade = null;
//					if (arrange13[j][0].trim().equals("一")) {
//						grade = "1";
//					} else if (arrange13[j][0].trim().equals("二")) {
//						grade = "2";
//					} else if (arrange13[j][0].trim().equals("三")) {
//						grade = "3";
//					} else if (arrange13[j][0].trim().equals("四")) {
//						grade = "4";
//					} else if (arrange13[j][0].trim().equals("五")) {
//						grade = "5";
//					} else if (arrange13[j][0].trim().equals("六")) {
//						grade = "6";
//					}
//					if (!klassMap.containsKey(Klass.getNam(grade, klassNam))) {
//						klass = new Klass();
//						klass.setClsNum(Integer.parseInt(klassNam));
//						klass.setGrade(grade);
//						klassMap.put(klass.toString(), klass);
//					} else {
//						klass = klassMap.get(Klass.getNam(grade, klassNam));
//					}
//
//					teacherNam = arrange13[j][i];
//					if(teacherNam==null){
//						for (String courseNam : klass.getCourseNamList()) {
//							Course course = Stutas.getCourseMap().get(courseNam);
//							if (course.getSubjectNam().equals(subject.getName())) {
//								courseMap.remove(course);
//								break;
//							}
//						}
//						continue;
//					}
//					Teacher teacher;
//					if (!teacherMap.containsKey(teacherNam)) {
//						teacher = new Teacher();
//						teacher.setName(teacherNam);
//						teacherMap.put(teacher.toString(), teacher);
//					} else {
//						teacher = teacherMap.get(teacherNam);
//					}
//					boolean hasSet = false;
//					for (String courseNam : klass.getCourseNamList()) {
//						Course course = Stutas.getCourseMap().get(courseNam);
//						if (course.getSubjectNam().equals(subject.getName())) {
//							course.setTeacherNam(teacher.toString());
//							hasSet = true;
//							break;
//						}
//					}
//					if(!hasSet){
//						Course course = new Course();
//						course.setKlassNam(klass.toString());
//						course.setSubjectNam(subject.toString());
//						course.setTeacherNam(teacher.toString());
//						course.setNumber(num);
//						courseMap.put(course.toString(), course);
//						course.associate();
//					}
//				}
//			}
//			Locator.check();
//			Locator.check_2();

		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		save();
	}

	private static void locateCourseTime(String[][] classTemp,String klassNam) {
		for (int i = 0; i < classTemp.length; i++) {
			
			for (int j = 0; j < classTemp[i].length; j++) {

				System.out.print(classTemp[i][j] + "\t\t");
				if(classTemp[i][j]==null){
					continue;
				}
				if (!klassMap.containsKey(klassNam)) {
					System.out.println(klassNam);
					continue;
				}
				for (String courseNam : klassMap.get(klassNam)
						.getCourseNamList()) {
					Course course = courseMap.get(courseNam);
					if (course.getSubjectNam().equals(classTemp[i][j])) {
						course.addTime(new Time(i,j));
						break;
					}
				}

			}

			System.out.println();

		}
		
	}

	public static void print2DArray(int[][] array){
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + "\t\t");
			}
			System.out.println();
		}
	}
	
	public static void print2DArray(String[][] array){
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + "\t\t");
			}
			System.out.println();
		}
	}
	
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static void setCourseMap(Map<String, Course> courseMap) {
		Stutas.courseMap = courseMap;
	}

	public static Map<String, Teacher> getTeacherMap() {
		return teacherMap;
	}

	public static void setTeacherMap(Map<String, Teacher> teacherMap) {
		Stutas.teacherMap = teacherMap;
	}

	public static Map<String, Klass> getKlassMap() {
		return klassMap;
	}

	public static void setKlassMap(Map<String, Klass> klassMap) {
		Stutas.klassMap = klassMap;
	}

	public static Map<String, Subject> getSubjectMap() {
		return subjectMap;
	}

	public static void setSubjectMap(Map<String, Subject> subjectMap) {
		Stutas.subjectMap = subjectMap;
	}

	public static Map<String, Rule> getRuleMap() {
		return ruleMap;
	}

	public static void setRuleMap(Map<String, Rule> ruleMap) {
		Stutas.ruleMap = ruleMap;
	}

	public static boolean read() {
		try {
			Field[] fields = Stutas.class.getFields();
			for (Field f : fields) {
				File file = new File(f.getName());
				if (!file.exists()) {
					continue;
				}
				FileInputStream fs = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fs);
				Method m = Stutas.class.getMethod(
						"set" + Character.toUpperCase(f.getName().charAt(0))
								+ f.getName().substring(1), f.getType());
				m.invoke(Stutas.class.newInstance(), ois.readObject());
				ois.close();
			}
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException
				| ClassNotFoundException | IOException e) {
			return false;
		}

		return true;
	}

	public static void save() {
		Object[] teachers = teacherMap.values().toArray();
		for (int i = 0; i < teachers.length; i++) {
			Teacher teacher = (Teacher) teachers[i];
			List<String> delList = new ArrayList<String>();
			for (String courseNam : teacher.getCourseNamList()) {
				if (!courseMap.containsKey(courseNam)) {
					delList.add(courseNam);
				}
			}
			for (String del : delList) {
				teacher.removeCourse(del);
			}
		}
		Object[] klasses = klassMap.values().toArray();
		for (int i = 0; i < klasses.length; i++) {
			Klass klass = (Klass) klasses[i];
			List<String> delList = new ArrayList<String>();
			for (String courseNam : klass.getCourseNamList()) {
				if (!courseMap.containsKey(courseNam)) {
					delList.add(courseNam);
				}
			}
			for (String del : delList) {
				klass.removeCourse(del);
			}

		}

		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					Field[] fields = Stutas.class.getFields();
					for (Field f : fields) {
						FileOutputStream fs = new FileOutputStream(f.getName());
						ObjectOutputStream os = new ObjectOutputStream(fs);
						Method m = Stutas.class.getMethod(
								"get"
										+ Character.toUpperCase(f.getName()
												.charAt(0))
										+ f.getName().substring(1),
								(Class<?>[]) null);
						os.writeObject(m.invoke(Stutas.class.newInstance(),
								(Object[]) null));
						os.flush();
						os.close();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		};

		Thread thread = new Thread(task);
		// thread.start();
		task.run();
	}

}
