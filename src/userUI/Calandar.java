package userUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import entity.Course;
import entity.Klass;
import entity.Teacher;
import entity.Time;

public class Calandar extends Table {

	public static final Color color_exchangeable;
	public static final Color color_normal;
	public static final Color color_locatable;
	public static final Color color_locked;
static{
	color_exchangeable = Display.getDefault().getSystemColor(SWT.COLOR_CYAN);
	color_normal = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	color_locatable = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	color_locked = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
}


	protected Klass selectedKlass;
	protected Teacher selectedtTeacher;
	protected Course selectedCourse;
	protected Time selectedTime;
	
	final TableItem[][] item = new TableItem[6][4];
	final TableItem[] item1 = new TableItem[4];
	TableCursor cursor = new TableCursor(Calandar.this, SWT.NONE);;
	AdjustUI adjustUI;
	
	public Calandar(Composite arg0, int arg1, AdjustUI adjustUI) {
		super(arg0, arg1);
		this.adjustUI = adjustUI;
	}
	
	protected void checkSubclass(){
		System.out.println("CheckSubClass: " + this.handle);
	}

}
