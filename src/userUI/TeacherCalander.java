package userUI;

import main.Locator;
import main.Stutas;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import dbController.TeacherScheduleBean;
import entity.Course;
import entity.Time;

public class TeacherCalander extends Calandar {

	public TeacherCalander(Composite arg0, int arg1, AdjustUI adjustUI) {
		super(arg0, arg1, adjustUI);
		initCursor();
	}
	
	private void initCursor(){
//		cursor = new TableCursor(this, SWT.NONE);
//		cursor.setTouchEnabled(true);
		cursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int column = cursor.getColumn();
				TableItem row = cursor.getRow();
				System.out.println(column + "" + row.getText(column));
				String name = row.getText(column);
				if (Stutas.klassMap.containsKey(name)) {
					adjustUI.tabFolder.setSelection(0);
					adjustUI.combo_2.setText(name);
					adjustUI.refreshKlassTable();
				}
			}
		});
		cursor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.character=='l'){
					if(selectedtTeacher!=null){
						int column = cursor.getColumn();
						int index = adjustUI.getClassCalander().getSelectionIndex();
						Course course = Stutas.getCourseMap().get(selectedtTeacher.getCourseNamTable()[column-1][index/3]);
						if(course.isLock(column-1,index/3)){
							course.unlock(column-1,index/3);
						}else{
							course.lock(column-1,index/3);
						}
						adjustUI.refreshTeacherTable();
					}
				}else if(arg0.character=='e'){
					if(selectedKlass!=null){
						int column = cursor.getColumn();
						int index = TeacherCalander.this.getSelectionIndex();
						selectedTime = new Time(column-1,index/3);
						adjustUI.status = "exchange";
						adjustUI.btnNewButton.setText(adjustUI.status);
					}
				}
			}
		});
		cursor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int column = cursor.getColumn();
				TableItem row = cursor.getRow();
				int index = TeacherCalander.this.getSelectionIndex();
				if(adjustUI.status!=null){
					if(adjustUI.status.equals("exchange")){
						if(!Locator.getExchangableCourse(selectedKlass, selectedTime.getDay(), selectedTime.getNum())[column-1][index/3]){
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("unexchangable");
						}else{
							Locator.exchangeCourse(selectedKlass, selectedTime.getDay(), selectedTime.getNum(), column-1,index/3);
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("NULL");
							adjustUI.refreshTeacherTable();
						}
						return;
					}
				}
				Course cour = Stutas.getCourseMap().get(selectedtTeacher.getCourseNamTable()[column-1][index/3]);
				System.out.println(cour);
				if(cour==null){
					return;
				}
				selectedKlass = Stutas.getKlassMap().get(cour.getKlassNam());
				boolean[][] result = Locator.getExchangableCourse(selectedKlass, column-1, index/3);
				for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 6; j++) {
							Course cours = null;
							if(result!=null&&result[i][j]){
								 cours = Stutas.getCourseMap().get(selectedKlass.getCourseNamTable()[i][j]);
								 TeacherCalander.this.getItem(j*3).setText(i+1,cours.getSubjectNam());
									TeacherCalander.this.getItem(j*3+1).setText(i+1,cours.getTeacherNam());
								TeacherCalander.this.getItem(j*3).setBackground(i+1, color_exchangeable);
								TeacherCalander.this.getItem(j*3+1).setBackground(i+1, color_exchangeable);
								TeacherCalander.this.getItem(j*3+2).setBackground(i+1, color_exchangeable);
//									table.getItem(j*4+3).setBackground(i+1, table.getDisplay().getSystemColor(SWT.COLOR_CYAN));
							}else{
								cours = Stutas.getCourseMap().get(selectedtTeacher.getCourseNamTable()[i][j]);
								TeacherCalander.this.getItem(j*3).setBackground(i+1, color_normal);
								TeacherCalander.this.getItem(j*3+1).setBackground(i+1, color_normal);
								TeacherCalander.this.getItem(j*3+2).setBackground(i+1, color_normal);
//								table.getItem(j*4+3).setBackground(i+1, table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
							}
//							if(cours==null){
//								table_2.getItem(j*3).setText(i+1,"");
//								table_2.getItem(j*3+1).setText(i+1,"");
//							}else{
//								table_2.getItem(j*3).setText(i+1,cours.getSubjectNam());
//								table_2.getItem(j*3+1).setText(i+1,cours.getTeacherNam());
//							}
						}
					}
					
				}
		});
	}
	
	public void refreshTeacherTable(TeacherScheduleBean[] scheduleInfo){
		this.removeAll();
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				item[i][j] = new TableItem(this, SWT.FULL_SELECTION);
				for(int k=1;k<6;k++){			
					if(scheduleInfo[(k-1) * 6 + i].isLocked()){
						item[i][j].setForeground(k, color_locked);
					}
					if (j == 0) {
						item[i][j].setText(0, "µÚ" + (i + 1) + "´ó½Ú");
						item[i][j].setText(k,
								scheduleInfo[(k-1) * 6 + i].getCourseName());
					} else if (j == 1) {
						item[i][j].setText(0, " ");
						item[i][j].setText(k,
								scheduleInfo[(k-1) * 6 + i].getKlassName());
					}  else {
						item[i][j].setText(k, " ");
					}	
				}
				
		
			}
		}
	}

}
