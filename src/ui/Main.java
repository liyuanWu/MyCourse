package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import main.Stutas;
import entity.Course;
import entity.Grade;
import entity.Klass;
import entity.Rule;
import entity.Subject;
import entity.Teacher;
import entity.Time;
import javax.swing.BoxLayout;
import java.awt.event.MouseAdapter;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import net.miginfocom.swing.MigLayout;

public class Main {

	public static Main main;
	
	private JFrame frame;
	private JComboBox<Object> comboBox_2;
	private JTable table;
	private DefaultTableModel tableModel;
	private DefaultTableModel tableModel_1;
	private JPanel panel_1;
	private String[][] renderTable = new String[5][10];
	
	ListUi<Teacher> teacherList;
	ListUi<Subject> subjectist ;
	ListUi<Klass> klassList;
	ListUi<Course> courselist;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		if(main==null){
			main=this;
		}
		initialize();
	}
	
	public static Main getMain(){
		return main;
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 825, 478);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		panel.add(toolBar, BorderLayout.NORTH);
		
		JButton btnNewButton_6 = new JButton("\u5237\u65B0");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String klassNam = comboBox_2.getSelectedItem().toString();
				Klass klass = Stutas.getKlassMap().get(klassNam);
				String[][] courseNamTable = klass.getCourseNamTable();
				tableModel = new DefaultTableModel();
				tableModel.setColumnCount(5);
				tableModel.setRowCount(10);
				for(int i=0;i<courseNamTable.length;i++){
					for(int j=0;j<courseNamTable[i].length;j++){
						tableModel.setValueAt(courseNamTable[i][j], j, i);
					}
				}
				table.setModel(tableModel);
				panel_1.removeAll();
				for(String courseNam:klass.getCourseNamList()){
					panel_1.add(new itemPanel(Stutas.courseMap.get(courseNam)));
				}
				for(int i=0;i<table.getColumnCount();i++){
					table.setDefaultRenderer(table.getColumnClass(i), new MuRender());
				}
				renderTable = new String[5][10];
			}
		});
		
		JButton button = new JButton("\u5220\u9664");
		toolBar.add(button);
		
		comboBox_2 = new JComboBox<Object>();
		toolBar.add(comboBox_2);
		comboBox_2.setEditable(true);
		toolBar.add(btnNewButton_6);
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(selecting){
					for(int i:table.getSelectedRows()){
						for(int j:table.getSelectedColumns()){
							tableModel.setValueAt(selectedCourse.getSubjectNam(), i, j);
							Course course = Stutas.courseMap.get(selectedCourse.toString());
							course.addTime(new Time(j,i));
							
						}
					}
					Course course = Stutas.courseMap.get(selectedCourse.toString());
					renderTable = new String[5][10];
					table.repaint();
					Stutas.save();
					selecting = false;
				}
			}
		});
		scrollPane.setViewportView(table);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane.setRowHeaderView(scrollPane_1);
		
		
		panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar_1 = new JToolBar();
		panel_2.add(toolBar_1, BorderLayout.NORTH);
		
		JButton button_1 = new JButton("\u5220\u9664");
		toolBar_1.add(button_1);
		
		comboBox = new JComboBox<Object>();
		comboBox.setEditable(true);
		toolBar_1.add(comboBox);
		
		JButton button_2 = new JButton("\u5237\u65B0");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String teacherNam = comboBox.getSelectedItem().toString();
				Teacher teacher = Stutas.getTeacherMap().get(teacherNam);
				String[][] courseNamTable = teacher.getCourseNamTable();
				tableModel_1 = new DefaultTableModel();
				tableModel_1.setColumnCount(5);
				tableModel_1.setRowCount(10);
				for(int i=0;i<courseNamTable.length;i++){
					for(int j=0;j<courseNamTable[i].length;j++){
						tableModel_1.setValueAt(courseNamTable[i][j], j, i);
					}
				}
				table_1.setModel(tableModel_1);
				for(int i=0;i<table.getColumnCount();i++){
					table.setDefaultRenderer(table.getColumnClass(i), new MuRender());
				}
				renderTable = new String[5][10];
				
			}
		});
		toolBar_1.add(button_2);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_2.add(scrollPane_2);
		
		table_1 = new JTable();
		table_1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table_1.setColumnSelectionAllowed(true);
		table_1.setCellSelectionEnabled(true);
		scrollPane_2.setViewportView(table_1);
		

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_3, null);
		
		Dimension standardSize = new Dimension((int)(frame.getWidth()*0.2),(int)(frame.getHeight()*0.7));
		
		teacherList = new ListUi<Teacher>();
		teacherList.setMap(Stutas.getTeacherMap(), Teacher.class);
		teacherList.getContentPane().setMinimumSize(standardSize);
		
		subjectist = new ListUi<Subject>();
		subjectist.setMap(Stutas.getSubjectMap(), Subject.class);
		subjectist.getContentPane().setMinimumSize(standardSize);

		klassList = new ListUi<Klass>();
		klassList.setMap(Stutas.getKlassMap(), Klass.class);
		klassList.getContentPane().setMinimumSize(standardSize);

		courselist = new ListUi<Course>();
		courselist.setMap(Stutas.getCourseMap(), Course.class);
		courselist.getContentPane().setMinimumSize(standardSize);
		
		
		
		panel_3.add(teacherList.getContentPane());	
		panel_3.setLayout(new MigLayout("", "[1px][1px][][]", "[2px][]"));
		JSeparator separator = new JSeparator();
		panel_3.add(separator, "cell 0 0,alignx left,aligny top");
		panel_3.add(subjectist.getContentPane());
		JSeparator separator_1 = new JSeparator();
		panel_3.add(separator_1, "cell 1 0,alignx left,aligny top");
		panel_3.add(klassList.getContentPane());
		panel_3.add(separator_1, "cell 1 0,alignx left,aligny top");
		panel_3.add(courselist.getContentPane());
		
		JButton button_3 = new JButton("\u751F\u6210");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String teacherName = teacherList.getSelecteditem()[0].toString();
				String subjectName = subjectist.getSelecteditem()[0].toString();
				String klassName = klassList.getSelecteditem()[0]+"-"+klassList.getSelecteditem()[1];

				if(!Stutas.teacherMap.containsKey(teacherName)){
					Teacher teacher = new Teacher();
					teacher.setName(teacherName);
					Stutas.teacherMap.put(teacherName, teacher);
					comboBox.addItem(teacher);
				}
				if(!Stutas.subjectMap.containsKey(subjectName)){
					Subject subject = new Subject();
					subject.setName(subjectName);
					Stutas.subjectMap.put(subjectName, subject);
				}
				if(!Stutas.klassMap.containsKey(klassName)){
					if(klassName.contains("-")){
						Grade grade = new Grade();
						Integer clsNum = 0;
						grade.setNum(Integer.parseInt(klassName.split("-")[0]));
						clsNum = Integer.parseInt(klassName.split("-")[1]);
						Klass klass = new Klass();
						klass.setGrade(grade);
						klass.setClsNum(clsNum);
						Stutas.klassMap.put(klassName, klass);
						comboBox_2.addItem(klass);
					}
				}
				
				Course course = new Course();
				course.setTeacherNam(teacherName);
				course.setSubjectNam(subjectName);
				course.setKlassNam(klassName);
				if(Stutas.courseMap.containsKey(course.toString())){
					return;
				}
				Stutas.courseMap.put(course.toString(), course);
				course.associate();
				refresh();
				Stutas.save();
			}
		});
		panel_3.add(button_3, "cell 0 1 3 1");

		

		refresh();
	}
	@SuppressWarnings("serial")
	class itemPanel extends JPanel{
		
		private JButton btnNewButton;
		private JLabel lblNewLabel_2;
		private JLabel lblNewLabel_1;
		private JLabel lblNewLabel;
		private Course course;

		public Course getCourse() {
			return course;
		}

		public void setCourse(Course course) {
			this.course = course;
		}

		/**
		 * Create the panel.
		 */
		public itemPanel(Course course) {
			this.course = course;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			lblNewLabel = new JLabel(course.getSubjectNam());
			add(lblNewLabel);
			
			lblNewLabel_1 = new JLabel(course.getTeacherNam());
			add(lblNewLabel_1);
			
			lblNewLabel_2 = new JLabel(course.getTimes().toString());
			add(lblNewLabel_2);
			
			btnNewButton = new JButton("ADD");
			add(btnNewButton);
			btnNewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selectTime(getCourse());
					table.repaint();
				}
			});
		}
	}
	public class MuRender extends DefaultTableCellRenderer{
        public  Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
         Component com = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
         if(isSelected){
        	 super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
         }
         if(getRenderTable()[column][row]!=null){
	         if(getRenderTable()[column][row].equals("green")){
	          com.setBackground(Color.GREEN);
	         }else if(getRenderTable()[column][row].equals("red")){
	          com.setBackground(Color.RED);
	         }
         }else{
        	 com.setBackground(Color.WHITE);
         }
            return com;
        }
    } 
	Course selectedCourse;
	boolean selecting = false;
	private JComboBox<Object> comboBox;
	private JTable table_1;
	public void selectTime(Course course){
		this.selectedCourse = course;
		Teacher teacher = Stutas.getTeacherMap().get(course.getTeacherNam());
		String[][] timeTable = teacher.getCourseNamTable();
		for(int i=0;i<timeTable.length;i++){
			for(int j=0;j<timeTable[i].length;j++){
				if(timeTable[i][j]!=null){
					getRenderTable()[i][j] = "red";
				}else{
					getRenderTable()[i][j] = "green";
				}
			}
		}
		Klass kalss = Stutas.getKlassMap().get(course.getKlassNam());
		timeTable = kalss.getCourseNamTable();
		for(int i=0;i<timeTable.length;i++){
			for(int j=0;j<timeTable[i].length;j++){
				if(timeTable[i][j]!=null){
					getRenderTable()[i][j] = "red";
				}
			}
		}
		selecting = true;

	}
	
	public String[][] getRenderTable() {
		return renderTable;
	}

	public void setRenderTable(String[][] renderTable) {
		this.renderTable = renderTable;
	}

	public void refresh(){
		comboBox_2.removeAllItems();
		if(Stutas.teacherMap!=null){
			for(Object obj:Stutas.teacherMap.values().toArray()){
				comboBox.addItem(obj);
			}
		}

		if(Stutas.klassMap!=null){
			for(Object obj:Stutas.klassMap.values().toArray()){
				comboBox_2.addItem(obj);
			}
		}
		courselist.refresh();
		teacherList.refresh();
		klassList.refresh();
		subjectist.refresh();
	}
}
