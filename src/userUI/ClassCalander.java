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

import dbController.StuScheduleBean;
import entity.Course;
import entity.Time;

public class ClassCalander extends Calandar {

	public ClassCalander(Composite arg0, int arg1, AdjustUI adjustUI) {
		super(arg0, arg1, adjustUI);
		initTableCursor();
	}
	
	private void initTableCursor(){
		
		cursor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.character == 'd'){
					if(selectedKlass!=null){
//						System.out.println("keypressed");
						int column = cursor.getColumn();
						int index = ClassCalander.this.getSelectionIndex();
						Course course = Stutas.getCourseMap().get(selectedKlass.getCourseNamTable()[column-1][index/3]);
						Locator.removeTime(course, new Time(column-1,index/3));
						adjustUI.refreshKlassTable();
					}
				}
				if(arg0.character=='l'){
					if(selectedKlass!=null){
						int column = cursor.getColumn();
						int index = ClassCalander.this.getSelectionIndex();
						Course course = Stutas.getCourseMap().get(selectedKlass.getCourseNamTable()[column-1][index/3]);
						if(course.isLock(column-1,index/3)){
							course.unlock(column-1,index/3);
						}else{
							course.lock(column-1,index/3);
						}
						adjustUI.refreshKlassTable();
					}
				}
				if(arg0.character=='m'){
					if(selectedKlass!=null){
						int column = cursor.getColumn();
						int index = ClassCalander.this.getSelectionIndex();
						selectedTime = new Time(column-1,index/3);
						adjustUI.status = "move";
						adjustUI.btnNewButton.setText(adjustUI.status);
					}
				}
				if(arg0.character=='e'){
					if(selectedKlass!=null){
						int column = cursor.getColumn();
						int index = ClassCalander.this.getSelectionIndex();
						selectedTime = new Time(column-1,index/3);
						adjustUI.status = "exchange";
						adjustUI.btnNewButton.setText(adjustUI.status);
					}
				}
				if(arg0.character=='f'){
					if(selectedKlass!=null){
						int column = cursor.getColumn();
						int index = ClassCalander.this.getSelectionIndex();
						selectedTime = new Time(column-1,index/3);
						adjustUI.status = "forceExchange";
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
				int index = ClassCalander.this.getSelectionIndex();
				if(adjustUI.status!=null){
					if(adjustUI.status.equals("forceExchange")){
						Locator.forceExchange(selectedKlass, selectedTime.getDay(), selectedTime.getNum(), column-1,index/3);
						adjustUI.status = null;
						adjustUI.btnNewButton.setText("NULL");
						adjustUI.refreshKlassTable();
						return;
					}else if(adjustUI.status.equals("exchange")){
						if(!Locator.getExchangableCourse(selectedKlass, selectedTime.getDay(), selectedTime.getNum())[column-1][index/3]){
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("unexchangable");
						}else{
							Locator.exchangeCourse(selectedKlass, selectedTime.getDay(), selectedTime.getNum(), column-1,index/3);
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("NULL");
							adjustUI.refreshKlassTable();
						}
						return;
					}else if(adjustUI.status.equals("move")){
						Course course = Stutas.getCourseMap().get(selectedKlass.getCourseNamTable()[selectedTime.getDay()][selectedTime.getNum()]);
						if(!Locator.moveCourse(course, selectedTime, new Time(column-1,index/3))){
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("Fail");
							adjustUI.refreshKlassTable();
							return;
						}
						adjustUI.status = null;
						adjustUI.btnNewButton.setText("NULL");
						adjustUI.refreshKlassTable();
						return;
					}else if(adjustUI.status.equals("locate")){
						boolean[][] locatable = Locator.getLocatable(selectedCourse);
						if(locatable[column-1][index/3]){
							selectedCourse.addTime(new Time(column-1,index/3));
							adjustUI.status = null;
							adjustUI.btnNewButton.setText("NULL");
							adjustUI.refreshKlassTable();
							return;
						}else{adjustUI.status = null;
							adjustUI.btnNewButton.setText("Fail");
							adjustUI.refreshKlassTable();
							return;
						}
						
					}
				}
//				for (int i = 0; i < table.getItems().length; i++) {
//					if (row.equals(table.getItem(i))) {
//						index = i;
//					}
//				}
//				System.out.println(index);
//				return;
				System.out.println(column + "" + row.getText(column));

//				String name;
//				if(row.getText(column).contains(" ")){
//					name = row.getText(column).split(" ")[0];
//				}else{
//					name = null;
//				}
//				table.setMenu(menu);
//				if(menu.isVisible()){
//					return;
//				}
				boolean[][] result = Locator.getExchangableCourse(selectedKlass, column-1, index/3);
				String CourseName = selectedKlass.getCourseNamTable()[column-1][index/3];
				System.out.println(CourseName);
				boolean[][] locResult = Locator.getLocatable(Stutas.getCourseMap().get(CourseName));
				for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 6; j++) {
							if(locResult!=null&&locResult[i][j]){
								ClassCalander.this.getItem(j*3).setBackground(i+1, color_locatable);
								ClassCalander.this.getItem(j*3+1).setBackground(i+1, color_locatable);
								ClassCalander.this.getItem(j*3+2).setBackground(i+1, color_locatable);
							}
							if(result!=null&&result[i][j]){
									ClassCalander.this.getItem(j*3).setBackground(i+1, color_exchangeable);
									ClassCalander.this.getItem(j*3+1).setBackground(i+1, color_exchangeable);
									ClassCalander.this.getItem(j*3+2).setBackground(i+1, color_exchangeable);
//									table.getItem(j*4+3).setBackground(i+1, table.getDisplay().getSystemColor(SWT.COLOR_CYAN));
							}else{
								ClassCalander.this.getItem(j*3).setBackground(i+1, color_normal);
								ClassCalander.this.getItem(j*3+1).setBackground(i+1, color_normal);
								ClassCalander.this.getItem(j*3+2).setBackground(i+1, color_normal);
//								table.getItem(j*4+3).setBackground(i+1, table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
							}
						}
					}
					
				}
		});
		cursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				int column = cursor.getColumn();
				TableItem row = cursor.getRow();
				System.out.println(column + "" + row.getText(column));
				String name = row.getText(column).split(" ")[0];
				if (Stutas.teacherMap.containsKey(name)) {
					adjustUI.tabFolder.setSelection(1);
					adjustUI.combo_10.setText(name);
					adjustUI.refreshTeacherTable();
				}
			}
		});
		cursor.setBackground(ClassCalander.this.getDisplay().getSystemColor(
				SWT.COLOR_LIST_SELECTION));
		cursor.setForeground(ClassCalander.this.getDisplay().getSystemColor(
				SWT.COLOR_LIST_SELECTION_TEXT));
	      cursor.addSelectionListener(new SelectionAdapter() {

	            public void widgetSelected(SelectionEvent e) {
	                int column = cursor.getColumn();
	                if (column == 1) {
	                    ClassCalander.this.setMenu(adjustUI.menu);
	                } else {
	                    ClassCalander.this.setMenu(null);
	                }
	            }
	        });
	}
	
	public void refreshClassTable(StuScheduleBean[] scheduleInfo){
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
								scheduleInfo[(k-1) * 6 + i].getTeacherName());
					}  else {
						item[i][j].setText(k, " ");
					}	
				}
				

			}
		}
	}
}
