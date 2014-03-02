package userUI;


import main.ExcelOperate;
import main.Locator;
import main.Stutas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.swtdesigner.SWTResourceManager;

import dbController.AdjustController;
import dbController.ClassTab1Ctrl;
import dbController.RoomInfoController;
import dbController.ScheduleTabController;
import dbController.StuScheduleBean;
import dbController.TeacherScheduleBean;
import dbController.TeacherTabController;
import entity.Course;
import entity.Klass;
import entity.Teacher;
import entity.Time;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;

public class AdjustUI {
	
	private TeacherCalander teacherCalander;
	private ClassCalander classCalander;
	public  Combo combo_2;
	protected Shell shell;

	public CCombo combo_10;
	private TableCursor cursor;
	private TableCursor tableCursor_1;

	String status;
	
	private Color color_exchangeable;
	private Color color_normal;
	private Color color_locatable;
	public  Menu menu;
	protected Color color_locked;
	private Composite composite_1;
	private Label label_6;

	
	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdjustUI window = new AdjustUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void refreshTeacherTable() {
		String classID = null;
		if (combo_10.getText() != null) {
				classID = combo_10.getText();
		} else {
			label_6.setText("请输入正确的班号");
		}

		TeacherScheduleBean[] scheduleInfo = new ScheduleTabController()
				.searchTeachSchedule(classID);
		teacherCalander.selectedtTeacher = Stutas.getTeacherMap().get(classID);
		if(item[0][0]!=null){
			if(item[0][0].getParent().equals(classCalander)){
				teacherCalander.removeAll();
			}
		}
		teacherCalander.refreshTeacherTable(scheduleInfo);
		label_6.setText(classID + "班的课表:");
		
	}
	
	public void refreshKlassTable(){
		String classID = null;
		if (combo_2.getText() != null) {
				classID = combo_2.getText();
		} else {
			label_6.setText("请输入正确的班号");
		}

		StuScheduleBean[] scheduleInfo = new ScheduleTabController()
				.searchStuSchedule(Klass.getNam(classID));
		classCalander.selectedKlass = Stutas.getKlassMap().get(Klass.getNam(classID));
//		composite_19 = new Composite(composite_1, SWT.NONE);
//		composite_19.setBounds(737, 80, 54, 411);
//		composite_19.setLayout(null);
//		for(int i=0;i<selectedKlass.getCourseNamList().size();i++){
//			Course cou = Stutas.getCourseMap().get(selectedKlass.getCourseNamList().get(i));
//			if(cou!=null){
//				Button button = new Button(composite_19,SWT.NULL);
//				button.setText(cou.getSubjectNam()+"  "+(cou.getNumber()-cou.getTimes().size()));
//				button.setBounds(3, 3+30*i, 50, 25);
//				button.setData(cou);
//				button.addSelectionListener(new SelectionAdapter() {
//					@Override
//					public void widgetSelected(SelectionEvent arg0) {
//						boolean[][] time = Locator.getLocatable((Course)arg0.widget.getData());
//						for (int i = 0; i < 5; i++) {
//							for (int j = 0; j < 6; j++) {
//								if(time!=null&&time[i][j]){
//									table.getItem(j*3).setBackground(i+1, color_locatable);
//									table.getItem(j*3+1).setBackground(i+1, color_locatable);
//									table.getItem(j*3+2).setBackground(i+1, color_locatable);
//								}
//							}
//						}
//					}
//				});
////				composite_19.add(button);
//
//			}
//		}
		classCalander.removeAll();
		classCalander.refreshClassTable(scheduleInfo);
		label_6.setText(classID + "班的课表:");
		
		Klass klass = Stutas.getKlassMap().get(Klass.getNam(classID));
		for(int i=0;i<menu_2.getItemCount();i++){
			menu_2.getItem(i).dispose();
		}
		for(String courseNam:klass.getCourseNamList()){
			Course course = Stutas.getCourseMap().get(courseNam);
			MenuItem mntmNewItem_3 = new MenuItem(menu_2, SWT.NONE);
			mntmNewItem_3.setText(course.getSubjectNam() + "  "+ (course.getNumber()-course.getTimes().size()));
			mntmNewItem_3.setData(course);
			mntmNewItem_3.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent arg0) {
//					selectedCourse = (Course)arg0.widget.getData();
					status = "locate";
					btnNewButton.setText(status);
				}
			});
		}
	}
	
	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	final TableItem[][] item = new TableItem[6][4];
	final TableItem[] item1 = new TableItem[4];
	private List list;
	public Button btnNewButton;
	private List list_1;
	private MenuItem menuItem;
	private Menu menu_2;
	public TabFolder tabFolder;
	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell(SWT.MIN);
		shell.setSize(862, 691);
		shell.setText("排课系统-调整课程");

		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 80, 845, 527);

		final TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("班级");

		composite_1 = new Composite(tabFolder, SWT.NONE);
		composite_1.setLayout(null);

		final Composite composite_2 = new Composite(composite_1, SWT.BORDER);
		composite_2.setBounds(0, 10, 721, 64);

		final Composite composite_3 = new Composite(composite_1, SWT.BORDER);
		composite_3.setBounds(0, 80, 733, 411);

		final Label label = new Label(composite_2, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("华文新魏", 12, SWT.NONE));
		label.setAlignment(SWT.CENTER);
		label.setText("查找班级");
		label.setBounds(10, 0, 71, 19);



		label_6 = new Label(composite_3, SWT.NONE);
		label_6.setAlignment(SWT.CENTER);
		label_6.setBackground(SWTResourceManager.getColor(255, 128, 128));
		label_6.setText("当前没有可显示项");
		label_6.setBounds(84, 0, 645, 18);
		
				final Label label_4 = new Label(composite_2, SWT.NONE);
				label_4.setBounds(20, 28, 40, 22);
				label_4.setText("班号");
				
						combo_2 = new Combo(composite_2, SWT.NONE);
						combo_2.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent arg0) {
								refreshKlassTable();
							}
						});
						combo_2.setBounds(66, 25, 72, 23);
						combo_2.setItems(new ScheduleTabController().getKlassIds());
						
						ScrolledComposite scrolledComposite = new ScrolledComposite(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
						scrolledComposite.setBounds(144, 12, 563, 48);
						scrolledComposite.setExpandHorizontal(true);
						scrolledComposite.setExpandVertical(true);
						
						list_1 = new List(scrolledComposite, SWT.BORDER);
						scrolledComposite.setContent(list_1);
						scrolledComposite.setMinSize(list_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		classCalander = new ClassCalander(composite_3, SWT.FULL_SELECTION, this);
		classCalander.setLinesVisible(true);
		classCalander.setHeaderVisible(true);
		classCalander.setBounds(0, 25, 719, 386);
		
		color_exchangeable = classCalander.getDisplay().getSystemColor(SWT.COLOR_CYAN);
		color_normal = classCalander.getDisplay().getSystemColor(SWT.COLOR_WHITE);
		color_locatable = classCalander.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		color_locked = classCalander.getDisplay().getSystemColor(SWT.COLOR_GRAY);
		
		

		final TableColumn newColumnTableColumn = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn.setWidth(50);
		newColumnTableColumn.setText("时间");

		final TableColumn newColumnTableColumn_1 = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn_1.setWidth(130);
		newColumnTableColumn_1.setText("星期一");

		final TableColumn newColumnTableColumn_2 = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn_2.setWidth(130);
		newColumnTableColumn_2.setText("星期二");

		final TableColumn newColumnTableColumn_3 = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn_3.setWidth(130);
		newColumnTableColumn_3.setText("星期三");

		final TableColumn newColumnTableColumn_4 = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn_4.setWidth(130);
		newColumnTableColumn_4.setText("星期四");

		final TableColumn newColumnTableColumn_5 = new TableColumn(classCalander,
				SWT.FULL_SELECTION);
		newColumnTableColumn_5.setWidth(130);
		newColumnTableColumn_5.setText("星期五");

		
	        final Menu menu = new Menu(classCalander);
	        MenuItem item2 = new MenuItem(menu, SWT.PUSH);
	        item2.setText("Hello Menu");
//		menu = new Menu(table);
//		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
//		item2.setEnabled(false);
//		item2.setText("\u653E\u7F6E");
//		
//		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
//		mntmNewItem.setEnabled(false);
//		mntmNewItem.setText("\u4EA4\u6362");

		// tableCursor = new TableCursor(table, SWT.NONE);
		// tableCursor.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseDoubleClick(MouseEvent arg0) {
		// String name = tableCursor.getRow().getText();
		// System.out.println(name);
		// if(Stutas.teacherMap.containsKey(name)){
		// tabFolder.setSelection(1);
		// combo_10.setText(name);
		// button_3.getListeners(SWT.MouseUp)[0].handleEvent(null);
		// }
		// }
		// });

		final Button button_1 = new Button(composite_3, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter() {
			public void mouseUp(final MouseEvent e) {

				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						for (int k = 0; k < 6; k++) {
							item[i][j].setText(k, " ");
						}
					}
				}
			}
		});
		button_1.setText("清除");
		button_1.setBounds(10, 0, 68, 18);
		tabItem.setControl(composite_1);
		
		Button button_2 = new Button(composite_1, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				list_1.setItems(Locator.check_2().split("\n"));
			}
		});
		button_2.setText("\u68C0\u67E5");
		button_2.setBounds(753, 29, 54, 25);
		
		final TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("教师");

		final Composite composite_6 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_6);

		final Composite composite_7 = new Composite(composite_6, SWT.BORDER);
		composite_7.setBounds(10, 10, 721, 64);

		final Label label_7 = new Label(composite_7, SWT.NONE);
		label_7.setFont(SWTResourceManager.getFont("华文新魏", 12, SWT.NONE));
		label_7.setText("查看教师");
		label_7.setBounds(10, 0, 71, 19);
		
				final Label label_15 = new Label(composite_7, SWT.NONE);
				label_15.setText("\u6559\u5E08\u59D3\u540D");
				label_15.setLocation(10, 25);
				label_15.setSize(48, 20);
				
						combo_10 = new CCombo(composite_7, SWT.BORDER);
						combo_10.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent arg0) {
								refreshTeacherTable();
							}
						});
						combo_10.setLocation(68, 29);
						combo_10.setSize(136, 21);
						combo_10.setItems(new TeacherTabController().getTeacherIds());

		final Composite composite_14 = new Composite(composite_6, SWT.BORDER);
		composite_14.setBounds(10, 80, 721, 410);

		teacherCalander = new TeacherCalander(composite_14, SWT.BORDER | SWT.FULL_SELECTION, this);
		teacherCalander.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				 if (e.button == 3) {
					 refreshTeacherTable();
				 }
			}
		});
		teacherCalander.setLinesVisible(true);
		teacherCalander.setHeaderVisible(true);
		teacherCalander.setBounds(0, 0, 711, 396);

		final TableColumn newColumnTableColumn_12 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_12.setWidth(50);
		newColumnTableColumn_12.setText("时间");

		final TableColumn newColumnTableColumn_13 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_13.setWidth(130);
		newColumnTableColumn_13.setText("星期一");

		final TableColumn newColumnTableColumn_14 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_14.setWidth(130);
		newColumnTableColumn_14.setText("星期二");

		final TableColumn newColumnTableColumn_15 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_15.setWidth(130);
		newColumnTableColumn_15.setText("星期三");

		final TableColumn newColumnTableColumn_16 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_16.setWidth(130);
		newColumnTableColumn_16.setText("星期四");

		final TableColumn newColumnTableColumn_17 = new TableColumn(teacherCalander,
				SWT.NONE);
		newColumnTableColumn_17.setWidth(130);
		newColumnTableColumn_17.setText("星期五");

		
		
		Button button = new Button(composite_6, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String[] teachers = Locator.checkTeachers();
				list.setItems(teachers);
			}
		});
		button.setBounds(758, 10, 54, 25);
		button.setText("\u68C0\u67E5");
		
		list = new List(composite_6, SWT.BORDER);
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				combo_10.setText(list.getItem(list.getSelectionIndex()));
				refreshTeacherTable();
			}
		});
		list.setBounds(737, 42, 90, 448);


		final Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBounds(0, 0, 819, 64);

		final Label label_5 = new Label(composite, SWT.NONE);
		label_5.setForeground(SWTResourceManager.getColor(0, 128, 255));
		label_5.setAlignment(SWT.CENTER);
		label_5.setFont(SWTResourceManager.getFont("华文新魏", 24, SWT.NONE));
		label_5.setText("智能排课系统");
		label_5.setBounds(0, 10, 250, 50);
		
		Menu menu_1 = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu_1);
		
		menuItem = new MenuItem(menu_1, SWT.CASCADE);
		menuItem.setText("\u589E\u6DFB");
		
		menu_2 = new Menu(menuItem);
		menuItem.setMenu(menu_2);
		
		MenuItem mntmNewItem_4 = new MenuItem(menu_2, SWT.NONE);
		mntmNewItem_4.setText("New Item");
		

		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Stutas.save();
			}
		});
		mntmNewItem.setText("\u4FDD\u5B58");
		
		MenuItem mntmNewItem_1 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				for(String klassNam: new ScheduleTabController().getKlassIds()){
					Klass klass = Stutas.getKlassMap().get(klassNam);
					ExcelOperate.writeKlassesXls(klass.toString(), klass.getCourseNamTable());
				}
				for(String teacherNam:new TeacherTabController().getTeacherIds()){
					Teacher teacher = Stutas.getTeacherMap().get(teacherNam);
					ExcelOperate.writeTeachersXls(teacher.toString(), teacher.getCourseNamTable());
				}
				ExcelOperate.reset();
			}
		});
		mntmNewItem_1.setText("\u5BFC\u51FA");
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		mntmNewItem_2.setText("\u9000\u51FA");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(91, 619, 728, 15);
		lblNewLabel.setText("New Label");
		
		btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				btnNewButton.setText("NULL");
				status = null;
			}
		});
		btnNewButton.setBounds(10, 614, 75, 25);
		btnNewButton.setText("New Button");
		//
	}
	public TeacherCalander getTeacherCalander() {
		return teacherCalander;
	}
	public void setTeacherCalander(TeacherCalander teacherCalander) {
		this.teacherCalander = teacherCalander;
	}
	public ClassCalander getClassCalander() {
		return classCalander;
	}
	public void setClassCalander(ClassCalander classCalander) {
		this.classCalander = classCalander;
	}
}
