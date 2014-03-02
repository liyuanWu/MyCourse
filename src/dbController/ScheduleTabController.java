package dbController;

import java.sql.*;
import java.util.*;

import entity.Course;
import entity.Klass;
import entity.Teacher;

import main.Stutas;

public class ScheduleTabController { // 类ScheduleTabController(),对ScheduleTab及TeacherScheduleTab处理的类

	private DBcon dbcon = new DBcon();
	private Connection con = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String sql = null;
	private boolean hasConllision = false; // 当false时,表示没有冲突

	private char[][] teacherTimer = new char[37][20]; // 保存教师的时间片使用情况
	private char[][] CAIRoomTimer = new char[11][20]; // 保存多媒体教室的时间片使用情况
	private char[][] NoCAIRoomTimer = new char[9][20]; // 保存普通教室时间片的使用情况

	private TeacherTabController TeacherCtrl = new TeacherTabController();
	private TeacherBean teacherBean = null;
	private courseController courseCtrl = new courseController();
	private CourseBean courseBean = null;

	private ArrayList scheList = new ArrayList();

	public ScheduleTabController() { // 构造方法,对教室和教师的时间片初始化
		for (int i = 0; i < teacherTimer.length; i++)
			for (int j = 0; j < teacherTimer[i].length; j++)
				teacherTimer[i][j] = '0';
		for (int i = 0; i < CAIRoomTimer.length; i++) {
			for (int j = 0; j < CAIRoomTimer[i].length; j++) {
				CAIRoomTimer[i][j] = '0';
			}
		}

		for (int i = 0; i < NoCAIRoomTimer.length; i++) {
			for (int j = 0; j < NoCAIRoomTimer[i].length; j++) {
				NoCAIRoomTimer[i][j] = '0';
			}
		}
	}

	public boolean hasConllision(int timer, String teacherID, int roomID,
			String CAI) { // 冲突检测，判断同一时间是否已有教室或教师的安排
							// 若已有安排，则冲突
		hasConllision = false;

		int teacherid = new Integer(teacherID.substring(4));
		int cai = new Integer(CAI);

		if (cai == 1) { // 对使用了多媒体教室的冲突的检测
			if ((CAIRoomTimer[roomID][timer] == '1')
					|| (teacherTimer[teacherid - 1][timer] == '1')) {
				hasConllision = true;
			}
		} else { // 对使用了普通教室的冲突的检测
			if ((NoCAIRoomTimer[roomID][timer] == '1')
					|| (teacherTimer[teacherid - 1][timer] == '1')) {
				hasConllision = true;
			}
		}

		if (hasConllision == false) { // 如果没有冲突，则把教师信息和教室信息都加入到相应时间片中
			if (cai == 1) {
				CAIRoomTimer[roomID][timer] = '1';
				teacherTimer[teacherid - 1][timer] = '1';
			} else {
				NoCAIRoomTimer[roomID][timer] = '1';
				teacherTimer[teacherid - 1][timer] = '1';
			}
		}

		return hasConllision;
	}

	public void addClassSche(String classID, String[] scheduleInfo) { // 把班级课表插入到班级课表数据库中

		sql = "insert into ScheduleTab values('" + classID
				+ "',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			for (int i = 1; i <= 20; i++) {
				pst.setString(i, scheduleInfo[i - 1]); // 把临时课表中的信息写到数据库中相应的时间片上
			}
			pst.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}
	}

	public void updateClassSche(String classID, String newInfo, int newtime,
			int oldtime) { // 方法updateClassSche(),更新学生课表
		sql = "update ScheduleTab set timer" + (newtime + 1) + "=?,timer"
				+ (oldtime + 1) + "=null where classID=?";
		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			pst.setString(1, newInfo);
			pst.setString(2, classID);
			pst.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}

	}

	public void addTeachSche(String teacherID, String[] teacherSchedInfo) { // 把老师课表插入到教师课表数据库中

		sql = "insert into TeacherScheduleTab values('" + teacherID
				+ "',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			for (int i = 1; i <= 20; i++) {
				pst.setString(i, teacherSchedInfo[i - 1]);
			}
			pst.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}

	}

	public void updateTeachSche(String teacherID, String newInfo, int newtime,
			int oldtime) { // 方法updateTeacherSche(),更新教师课表
		sql = "update TeacherScheduleTab set timer" + (newtime + 1)
				+ "=?,timer" + (oldtime + 1) + "=null where teacherID=?";
		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			pst.setString(1, newInfo);
			pst.setString(2, teacherID);
			pst.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}

	}

	public String[] getTeacherTimer(String teacherID) { // 方法getTeacherTimer(),从学生课表中获得教师的排课信息
		String[] timer = new String[20]; // 用于保存每个时间片上的教师信息
		String classID; // 班号
		String timerInfo = null; // 记录当前时间片的信息
		sql = "select * from ScheduleTab";

		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				classID = rs.getString(1);

				for (int i = 1; i <= 20; i++) { // 判断当前班级的课表中是否包含当前教师的信息，并加入到教师课表中
					timerInfo = rs.getString(i + 1);
					if (timerInfo != null) {
						if (timerInfo.contains(teacherID)) {
							timer[i - 1] = (timerInfo.substring(0, 5) + classID);
						}
					}

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}

		return timer;
	}

	public StuScheduleBean[] searchStuSchedule(String classID) { // 方法searchStuSchedule(),查看某个班级学生课表信息
		StuScheduleBean[] scheduleInfo = new StuScheduleBean[30];
		Klass klass = Stutas.getKlassMap().get(classID);

		for (int i = 0; i < 30; i++) {
			scheduleInfo[i] = new StuScheduleBean();
			scheduleInfo[i].setCourseName("");
			scheduleInfo[i].setTeacherName("");
		}
		if(klass==null){
			return scheduleInfo;
		}
		for (String courseNam : klass.getCourseNamList()) {
			Course course = Stutas.getCourseMap().get(courseNam);
			if (course != null) {
				for (entity.Time time : course.getTimes()) {
					scheduleInfo[time.getDay() * 6 + time.getNum()]
							.setCourseName(course.getSubjectNam());
					scheduleInfo[time.getDay() * 6 + time.getNum()]
							.setTeacherName(course.getTeacherNam()+"  "+course.getNumber());
					scheduleInfo[time.getDay() * 6 + time.getNum()].setLocked(time.isLocked());
				}
			}
		}
		return scheduleInfo;
	}

	public TeacherScheduleBean[] searchTeachSchedule(String teacherID) { // 方法searchTeacherSchedule(),查看某个教师的课表信息
		TeacherScheduleBean[] scheduleInfo = new TeacherScheduleBean[30];
		Teacher teacher = Stutas.getTeacherMap().get(teacherID);
		
		for (int i = 0; i < 30; i++) {
			scheduleInfo[i] = new TeacherScheduleBean();
			scheduleInfo[i].setCourseName("");
			scheduleInfo[i].setKlassName("");
		}
		for (String courseNam : teacher.getCourseNamList()) {
			Course course = Stutas.getCourseMap().get(courseNam);
			if (course != null) {
				for (entity.Time time : course.getTimes()) {
					scheduleInfo[time.getDay() * 6 + time.getNum()]
							.setCourseName(course.getSubjectNam());
					scheduleInfo[time.getDay() * 6 + time.getNum()]
							.setKlassName(course.getKlassNam());
					scheduleInfo[time.getDay() * 6 + time.getNum()].setLocked(time.isLocked());
				}
			}
		}
		return scheduleInfo;

	}

	public char[][] getCAIRoomUseInfo() { // 获得每个CAI教室一周的使用情况
		return CAIRoomTimer;
	}

	public char[][] getNoCAIRoomUseInfo() { // 获得每个NoCAI教室一周的使用情况
		return NoCAIRoomTimer;
	}

	public char[][] getTeacherTimerInfo() { // 获得每个教师一周的上课情况
		return teacherTimer;
	}

	public void closeBD() { // 关闭数据库的操作

		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}


	public String[] getKlassIds() {
		String[] strs = new String[Stutas.getKlassMap().size()];
		int i=0;
		for(Object s:Stutas.getKlassMap().values().toArray()){
			strs[i] = s.toString();
			i++;
		}
		Arrays.sort(strs);
		return strs;
	}
}
