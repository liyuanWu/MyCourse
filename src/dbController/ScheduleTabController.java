package dbController;

import java.sql.*;
import java.util.*;

import entity.Course;
import entity.Klass;
import entity.Teacher;

import main.Stutas;

public class ScheduleTabController { // ��ScheduleTabController(),��ScheduleTab��TeacherScheduleTab�������

	private DBcon dbcon = new DBcon();
	private Connection con = null;
	private PreparedStatement pst = null;
	private ResultSet rs = null;
	private String sql = null;
	private boolean hasConllision = false; // ��falseʱ,��ʾû�г�ͻ

	private char[][] teacherTimer = new char[37][20]; // �����ʦ��ʱ��Ƭʹ�����
	private char[][] CAIRoomTimer = new char[11][20]; // �����ý����ҵ�ʱ��Ƭʹ�����
	private char[][] NoCAIRoomTimer = new char[9][20]; // ������ͨ����ʱ��Ƭ��ʹ�����

	private TeacherTabController TeacherCtrl = new TeacherTabController();
	private TeacherBean teacherBean = null;
	private courseController courseCtrl = new courseController();
	private CourseBean courseBean = null;

	private ArrayList scheList = new ArrayList();

	public ScheduleTabController() { // ���췽��,�Խ��Һͽ�ʦ��ʱ��Ƭ��ʼ��
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
			String CAI) { // ��ͻ��⣬�ж�ͬһʱ���Ƿ����н��һ��ʦ�İ���
							// �����а��ţ����ͻ
		hasConllision = false;

		int teacherid = new Integer(teacherID.substring(4));
		int cai = new Integer(CAI);

		if (cai == 1) { // ��ʹ���˶�ý����ҵĳ�ͻ�ļ��
			if ((CAIRoomTimer[roomID][timer] == '1')
					|| (teacherTimer[teacherid - 1][timer] == '1')) {
				hasConllision = true;
			}
		} else { // ��ʹ������ͨ���ҵĳ�ͻ�ļ��
			if ((NoCAIRoomTimer[roomID][timer] == '1')
					|| (teacherTimer[teacherid - 1][timer] == '1')) {
				hasConllision = true;
			}
		}

		if (hasConllision == false) { // ���û�г�ͻ����ѽ�ʦ��Ϣ�ͽ�����Ϣ�����뵽��Ӧʱ��Ƭ��
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

	public void addClassSche(String classID, String[] scheduleInfo) { // �Ѱ༶�α���뵽�༶�α����ݿ���

		sql = "insert into ScheduleTab values('" + classID
				+ "',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			for (int i = 1; i <= 20; i++) {
				pst.setString(i, scheduleInfo[i - 1]); // ����ʱ�α��е���Ϣд�����ݿ�����Ӧ��ʱ��Ƭ��
			}
			pst.execute();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			this.closeBD();
		}
	}

	public void updateClassSche(String classID, String newInfo, int newtime,
			int oldtime) { // ����updateClassSche(),����ѧ���α�
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

	public void addTeachSche(String teacherID, String[] teacherSchedInfo) { // ����ʦ�α���뵽��ʦ�α����ݿ���

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
			int oldtime) { // ����updateTeacherSche(),���½�ʦ�α�
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

	public String[] getTeacherTimer(String teacherID) { // ����getTeacherTimer(),��ѧ���α��л�ý�ʦ���ſ���Ϣ
		String[] timer = new String[20]; // ���ڱ���ÿ��ʱ��Ƭ�ϵĽ�ʦ��Ϣ
		String classID; // ���
		String timerInfo = null; // ��¼��ǰʱ��Ƭ����Ϣ
		sql = "select * from ScheduleTab";

		try {
			con = dbcon.getConnect();
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				classID = rs.getString(1);

				for (int i = 1; i <= 20; i++) { // �жϵ�ǰ�༶�Ŀα����Ƿ������ǰ��ʦ����Ϣ�������뵽��ʦ�α���
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

	public StuScheduleBean[] searchStuSchedule(String classID) { // ����searchStuSchedule(),�鿴ĳ���༶ѧ���α���Ϣ
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

	public TeacherScheduleBean[] searchTeachSchedule(String teacherID) { // ����searchTeacherSchedule(),�鿴ĳ����ʦ�Ŀα���Ϣ
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

	public char[][] getCAIRoomUseInfo() { // ���ÿ��CAI����һ�ܵ�ʹ�����
		return CAIRoomTimer;
	}

	public char[][] getNoCAIRoomUseInfo() { // ���ÿ��NoCAI����һ�ܵ�ʹ�����
		return NoCAIRoomTimer;
	}

	public char[][] getTeacherTimerInfo() { // ���ÿ����ʦһ�ܵ��Ͽ����
		return teacherTimer;
	}

	public void closeBD() { // �ر����ݿ�Ĳ���

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
