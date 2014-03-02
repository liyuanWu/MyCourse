package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import main.Stutas;
import javax.swing.ListSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Dimension;

public class ListUi<T>{

	private Class<T> tClass;
	private Map<String, T> map;
	private List<T> list;
	private JPanel contentPane;
	private DefaultTableModel tableModel = new DefaultTableModel();
	private JTable table;

	/**
	 * Create the frame.
	 */
	public ListUi() {
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(317, 408));
		contentPane.setMaximumSize(new Dimension(250, 400));
		contentPane.setBounds(0, 0, 199, 302);
		contentPane.setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);

		JButton btnNewButton = new JButton("\u6DFB\u52A0");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tableModel.setRowCount(tableModel.getRowCount() + 1);
			}
		});
		toolBar.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("\u4FDD\u5B58");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		toolBar.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("\u5220\u9664");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.removeRow(table.getSelectedRow());
			}
		});
		toolBar.add(btnNewButton_2);


		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		table = new JTable();
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (table.getSelectedRow() == table.getRowCount() - 1) {
						tableModel.setRowCount(tableModel.getRowCount() + 1);
					}
				}
			}
		});

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setFocusCycleRoot(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setModel(tableModel);
		scrollPane.setViewportView(table);
	}
	
	public Object[] getSelecteditem(){
		return ((Vector)(tableModel.getDataVector().get(table.getSelectedRow()))).toArray();
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void refresh(){
		if(map!=null){
			setMap(map, tClass);
			contentPane.repaint();
		}
	}
	
	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}


	@SuppressWarnings("rawtypes")
	private void save() {
		table.clearSelection();
		try {
			if (map != null) {
				map = new HashMap<String, T>();
				Method[] methods = tClass.getMethods();
				for (Object row : tableModel.getDataVector().toArray()) {
					Object obj = tClass.newInstance();
					int i = 0;
					for (Object data : ((Vector) row).toArray()) {
						if(data==null){
							continue;
						}
						Object cont = data.toString();
						if (cont.equals("")) {
							continue;
						}
						Field f = tClass.getDeclaredField(tableModel
								.getColumnName(i));
						if (f.getType().equals(Integer.class)) {
							cont = Integer.parseInt(cont.toString());
						}
						if (f.getType().equals(Double.class)) {
							cont = Double.parseDouble(cont.toString());
						}
						for (Method m : methods) {
							if (m.getName().equals(
									"set"
											+ Character.toUpperCase(f.getName()
													.charAt(0))
											+ f.getName().substring(1))
									&& m.getParameterTypes()[0].equals(cont
											.getClass())) {
								m.invoke(obj, new Object[] { cont });
							}
						}
						i++;
					}
					if(obj.toString()!=null){
						map.put(obj.toString(), (T) obj);
					}
				}
			}
			Method m = Stutas.class.getMethod(
					"set"
							+ Character.toUpperCase(tClass.getSimpleName()
									.charAt(0))
							+ tClass.getSimpleName().substring(1) + "Map",
					Map.class);
			m.invoke(Stutas.class.newInstance(), map);
			Stutas.save();
			Main.getMain().refresh();
			
		} catch (IllegalAccessException | NoSuchMethodException
				| SecurityException | NoSuchFieldException
				| IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			e.printStackTrace();
		}

	}

	public void setMap(Map<String, T> map, Class cls) {
		this.map = map;
		Collection<T> values = map.values();
		List<T> list = new ArrayList<T>(values);
		setList(list, cls);
	}

	public void setList(List<T> list, Class cls) {
		this.list = list;
		try {
			this.tClass = cls;
			tableModel.setColumnCount(0);
			tableModel.setRowCount(0);
			Field[] fields = cls.getDeclaredFields();
			for (Field f : fields) {
				tableModel.addColumn(f.getName());
			}
			if (list.size() == 0) {
				tableModel.setRowCount(1);
				return;
			}
			for (T obj : list) {
				Vector<String> value = new Vector<String>();
				for (Field f : fields) {
					Method m = obj.getClass().getMethod(
							"get"
									+ Character.toUpperCase(f.getName().charAt(
											0)) + f.getName().substring(1),
							(Class<?>[]) null);
					Object ret = m.invoke(obj, (Object[]) null);
					value.add(ret == null ? null : ret.toString());
				}
				tableModel.addRow(value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
