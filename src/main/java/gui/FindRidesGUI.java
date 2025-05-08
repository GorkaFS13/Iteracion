package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;

import com.toedter.calendar.JCalendar;
import domain.CarType;
import domain.Driver;
import domain.Ride;
import domain.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;


public class FindRidesGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private final User currentUser;


	private JComboBox<String> jComboBoxOrigin = new JComboBox<String>();
	DefaultComboBoxModel<String> originLocations = new DefaultComboBoxModel<String>();

	private JComboBox<String> jComboBoxDestination = new JComboBox<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JLabel jLabelOrigin = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.LeavingFrom"));
	private JLabel jLabelDestination = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.GoingTo"));
	private final JLabel jLabelEventDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.RideDate"));
	private final JLabel jLabelEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateRideGUI.Rides"));
	private final JLabel jLabelComment = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Comment"));

	private JButton jButtonClose = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Close"));
	private JButton jButtonRequest = new JButton(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.RequestRide"));

	private JTextArea jTextAreaComment = new JTextArea(3, 30);
	{
		
		final String placeholderText = ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Comment");
		jTextAreaComment.setText(placeholderText);
		jTextAreaComment.setForeground(Color.GRAY);

		
		jTextAreaComment.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (jTextAreaComment.getText().equals(placeholderText)) {
					jTextAreaComment.setText("");
					jTextAreaComment.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (jTextAreaComment.getText().isEmpty()) {
					jTextAreaComment.setText(placeholderText);
					jTextAreaComment.setForeground(Color.GRAY);
				}
			}
		});
	}

	
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarAnt = null;
	private Calendar calendarAct = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private List<Date> datesWithRidesCurrentMonth = new Vector<Date>();

	private JTable tableRides= new JTable();

	private DefaultTableModel tableModelRides;


	private String[] columnNamesRides = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Driver"),
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NPlaces"),
			ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Price"),
			"Rating",
			"Car"
	};


	public FindRidesGUI(User currentUser)
	{
		this.currentUser = currentUser;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(700, 800));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.FindRides"));

		jLabelEventDate.setBounds(new Rectangle(457, 6, 140, 25));
		jLabelEvents.setBounds(172, 229, 259, 16);

		this.getContentPane().add(jLabelEventDate, null);
		this.getContentPane().add(jLabelEvents);

		jButtonClose.setBounds(new Rectangle(274, 419, 130, 30));
		jButtonRequest.setBounds(new Rectangle(400, 419, 130, 30));


		jButtonClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				jButton2_actionPerformed(e);
			}
		});



		jButtonRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonRequest_actionPerformed(e);
			}


		});
		BLFacade facade = MainGUI.getBusinessLogic();
		List<String> origins=facade.getDepartCities();

		for(String location:origins) originLocations.addElement(location);

		jLabelOrigin.setBounds(new Rectangle(6, 56, 92, 20));
		jLabelDestination.setBounds(6, 81, 61, 16);
		getContentPane().add(jLabelOrigin);

		getContentPane().add(jLabelDestination);

		jComboBoxOrigin.setModel(originLocations);
		jComboBoxOrigin.setBounds(new Rectangle(103, 50, 172, 20));


		List<String> aCities=facade.getDestinationCities((String)jComboBoxOrigin.getSelectedItem());
		for(String aciti:aCities) {
			destinationCities.addElement(aciti);
		}

		jComboBoxOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				destinationCities.removeAllElements();
				BLFacade facade = MainGUI.getBusinessLogic();

				List<String> aCities=facade.getDestinationCities((String)jComboBoxOrigin.getSelectedItem());
				for(String aciti:aCities) {
					destinationCities.addElement(aciti);
				}
				tableModelRides.getDataVector().removeAllElements();
				tableModelRides.fireTableDataChanged();


			}
		});


		jComboBoxDestination.setModel(destinationCities);
		jComboBoxDestination.setBounds(new Rectangle(103, 80, 172, 20));
		jComboBoxDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,	new Color(210,228,238));

				BLFacade facade = MainGUI.getBusinessLogic();

				datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
				paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);

			}
		});





		
		JScrollPane commentScrollPane = new JScrollPane(jTextAreaComment);
		commentScrollPane.setBounds(new Rectangle(172, 350, 346, 60));
		jLabelComment.setBounds(new Rectangle(172, 330, 200, 20));

		this.getContentPane().add(jLabelComment);
		this.getContentPane().add(commentScrollPane);

		this.getContentPane().add(jButtonClose, null);
		this.getContentPane().add(jButtonRequest);
		this.getContentPane().add(jComboBoxOrigin, null);
		this.getContentPane().add(jComboBoxDestination, null);


		jCalendar1.setBounds(new Rectangle(300, 50, 225, 150));


		
		jCalendar1.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent propertychangeevent)
			{

				if (propertychangeevent.getPropertyName().equals("locale"))
				{
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				}
				else if (propertychangeevent.getPropertyName().equals("calendar"))
				{
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();



					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar1.getLocale());

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);

					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) {
							
							
							calendarAct.set(Calendar.MONTH, monthAnt+1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}

						jCalendar1.setCalendar(calendarAct);

					}

					try {
						tableModelRides.setDataVector(null, columnNamesRides);
						tableModelRides.setColumnCount(6); 

						BLFacade facade = MainGUI.getBusinessLogic();
						List<domain.Ride> rides=facade.getRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),UtilDate.trim(jCalendar1.getDate()));

						if (rides.isEmpty() ) jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.NoRides")+ ": "+dateformat1.format(calendarAct.getTime()));
						else jLabelEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Rides")+ ": "+dateformat1.format(calendarAct.getTime()));
						for (domain.Ride ride:rides){
							Vector<Object> row = new Vector<Object>();
							Driver driver = ride.getDriver();
							row.add(driver.getUsername());
							row.add(ride.getnPlaces());
							row.add(ride.getPrice());

							
							float rating = driver.getAverageRating();
							int totalRatings = driver.getTotalRatings();
							String ratingText;

							if (totalRatings > 0) {
								
								ratingText = String.format("%.1f ★ (%d)", rating, totalRatings);
							} else {
								ratingText = "Not rated";
							}

							row.add(ratingText);

							
							row.add(driver.getCarType().getDisplayName());

							row.add(ride); 
							tableModelRides.addRow(row);
						}
						datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
						paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);


					} catch (Exception e1) {

						e1.printStackTrace();
					}
					
					tableRides.setRowHeight(70);

					tableRides.getColumnModel().getColumn(0).setPreferredWidth(120); 
					tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);  
					tableRides.getColumnModel().getColumn(2).setPreferredWidth(30);  
					tableRides.getColumnModel().getColumn(3).setPreferredWidth(80);  
					tableRides.getColumnModel().getColumn(4).setPreferredWidth(80);  

					
					tableRides.getColumnModel().getColumn(4).setCellRenderer(new CarTypeRenderer());

					tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(5)); 

				}
			}

		});

		this.getContentPane().add(jCalendar1, null);

		scrollPaneEvents.setBounds(new Rectangle(172, 257, 346, 150));

		scrollPaneEvents.setViewportView(tableRides);
		tableModelRides = new DefaultTableModel(null, columnNamesRides);

		tableRides.setModel(tableModelRides);

		
		tableRides.setRowHeight(70);

		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setVerticalAlignment(SwingConstants.CENTER);
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		
		for (int i = 0; i < tableRides.getColumnCount(); i++) {
			if (i != 4) { 
				tableRides.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
			}
		}

		tableModelRides.setDataVector(null, columnNamesRides);
		tableModelRides.setColumnCount(6); 

		tableRides.getColumnModel().getColumn(0).setPreferredWidth(120); 
		tableRides.getColumnModel().getColumn(1).setPreferredWidth(30);  
		tableRides.getColumnModel().getColumn(2).setPreferredWidth(30);  
		tableRides.getColumnModel().getColumn(3).setPreferredWidth(80);  
		tableRides.getColumnModel().getColumn(4).setPreferredWidth(80);  

		
		tableRides.getColumnModel().getColumn(4).setCellRenderer(new CarTypeRenderer());

		tableRides.getColumnModel().removeColumn(tableRides.getColumnModel().getColumn(5)); 

		this.getContentPane().add(scrollPaneEvents, null);
		datesWithRidesCurrentMonth=facade.getThisMonthDatesWithRides((String)jComboBoxOrigin.getSelectedItem(),(String)jComboBoxDestination.getSelectedItem(),jCalendar1.getDate());
		paintDaysWithEvents(jCalendar1,datesWithRidesCurrentMonth,Color.CYAN);




	}
	public static void paintDaysWithEvents(JCalendar jCalendar,List<Date> datesWithEventsCurrentMonth, Color color) {
		


		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		int year=calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;


		for (Date d:datesWithEventsCurrentMonth){

			calendar.setTime(d);


			
			
			
			
			
			
			
			
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(color);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);


	}



	private void jButton2_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
	BLFacade facade = MainGUI.getBusinessLogic();
	private void jButtonRequest_actionPerformed(ActionEvent e) {
		int selectedRow = tableRides.getSelectedRow();

		if (selectedRow == -1) {
			System.out.println("Select a ride");
			return;
		}

		
		Object rideObject = tableModelRides.getValueAt(selectedRow, 5);
		if (!(rideObject instanceof Ride)) {
			System.out.println("Error: el objeto seleccionado no es un Ride.");
			return;
		}

		Ride selectedRide = (Ride) rideObject;



		
		Driver rideDriver = selectedRide.getDriver();
		System.out.println("Ride seleccionada: " + selectedRide);
		System.out.println("Ride driver: " + rideDriver);

		
		
		if (rideDriver == null) {
			System.out.println("Error: No se encontró el driver del ride.");
			return;
		}

		
		String comment = jTextAreaComment.getText().trim();

		
		String placeholderText = ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Comment");
		if (comment.equals(placeholderText)) {
			comment = "";
		}

		
		boolean success = facade.requestRide(selectedRide, currentUser, rideDriver, comment);

		if (success) {
			System.out.println("Solicitud enviada con éxito.");
			System.out.println("Email del driver: " + rideDriver);
			
			placeholderText = ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.Comment");
			jTextAreaComment.setText(placeholderText);
			jTextAreaComment.setForeground(Color.GRAY);
			tableModelRides.removeRow(selectedRow);

			
			JOptionPane.showMessageDialog(this,
				ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.RequestSuccess"),
				ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.RequestSuccess"),
				JOptionPane.INFORMATION_MESSAGE);
		} else {
			System.out.println("Error al enviar la solicitud. Solucitud ya ha sido enviada anteriormenente");
			JOptionPane.showMessageDialog(this,
				ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.RequestFail"),
				ResourceBundle.getBundle("Etiquetas").getString("FindRidesGUI.RequestFail"),
				JOptionPane.ERROR_MESSAGE);
		}
	}

	
	class CarTypeRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {

			
			JLabel label = (JLabel) super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			if (value != null) {
				
				try {
					CarType carType = CarType.fromDisplayName(value.toString());
					
					ImageIcon icon = carType.getScaledImageIcon(80, 50);

					if (icon != null) {
						
						label.setIcon(icon);
						
						label.setText(carType.getDisplayName());
						label.setHorizontalTextPosition(SwingConstants.CENTER);
						label.setVerticalTextPosition(SwingConstants.BOTTOM);

						
						label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
					} else {
						
						label.setIcon(null);
						label.setText(value.toString());
					}
				} catch (Exception e) {
					
					label.setIcon(null);
					label.setText(value.toString());
				}
			} else {
				
				label.setIcon(null);
				label.setText("");
			}

			return label;
		}
	}
}
