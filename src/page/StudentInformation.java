package page;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import db.DBOperations;
import dto.Counselor;
import dto.CounselorDetail;
import dto.Course;
import dto.CourseType;
import user.Admin;
import user.ResearchAssistant;
import user.Student;
import user.TeachingStaff;
import user.User;
import util.Constants;
import util.Enums;
import util.StringUtil;

public class StudentInformation implements ItemListener {
	JPanel panel; // a panel that uses CardLayout
	
	
	private static DBOperations db;
	private User loginUser;
	private String lastScreen;
	private String courseListLastScreen;
	private String courseActionName;
	private List<Course> courses;
	private List<Course> selectedCourses;
	private List<Course> studentCourseList;
	private List<Counselor> counselorList;
	private Counselor counselor;
	private Course selectedCourse;

	public void addComponentToPane(Container pane) {
		panel = new JPanel(new CardLayout());
		addLoginForm();
		addRegisterForm();
		addAdminMainForm();
		addLessonModifyForm();
		addLessonCreateForm();
		addStudentMainForm();
		addChangePasswordPanel();
		addModifyUserForm();
		addTeachingStaffMainForm();
//		addModifyUserLessonForm();
//		addCourseListForm();
//		addSelectedCourseListForm();
		pane.add(panel);
	}

	private void addLoginForm() {
		JPanel login = new JPanel();
		JLabel lblKullancAd = new JLabel("Kullan\u0131c\u0131 Ad\u0131 :");
		JTextField txtTxtusername = new JTextField();
		txtTxtusername.setColumns(10);
		JLabel lblifre = new JLabel("\u015Eifre        ");
		JPasswordField pwdTxtpassword = new JPasswordField();
		JButton btnLogin = new JButton("Giri\u015F");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtTxtusername.getText() == null || txtTxtusername.getText().trim().equals("")
						|| pwdTxtpassword.getPassword() == null) {
					JOptionPane.showMessageDialog(panel, "Kullanýcý Adý veya Þifre boþ olamaz.");
				} else {
					String userId = txtTxtusername.getText();
					String password = new String(pwdTxtpassword.getPassword());
					if (db.login(userId, password)) {
						loginUser = db.getUserInfo(userId);
						if (loginUser instanceof Admin) {
							changeLayout(Constants.PanelNames.ADMIN_MAIN_PANEL);
						} else if (loginUser instanceof TeachingStaff) {
							changeLayout(Constants.PanelNames.TEACHING_STAFF_MAIN_PANEL);
						} else if (loginUser instanceof ResearchAssistant) {
							changeLayout(Constants.PanelNames.RESEARCH_ASSISTANT_MAIN_PANEL);
						} else if (loginUser instanceof Student) {
							changeLayout(Constants.PanelNames.STUDENT_MAIN_PANEL);
						}
					} else {
						JOptionPane.showMessageDialog(panel, "Kullanýcý Adý veya Þifre yanlýþ.");
					}
				}
			}
		});
		login.add(lblKullancAd);
		login.add(txtTxtusername);
		login.add(lblifre);
		login.add(pwdTxtpassword);
		login.add(btnLogin);
		GroupLayout gl_contentPane = new GroupLayout(login);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addGap(47)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addComponent(btnLogin)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblKullancAd).addComponent(lblifre))
								.addGap(33)
								.addGroup(gl_contentPane
										.createParallelGroup(Alignment.LEADING, false).addComponent(pwdTxtpassword)
										.addComponent(txtTxtusername, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))))
				.addContainerGap(27, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtTxtusername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblKullancAd))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblifre).addComponent(
						pwdTxtpassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 83, Short.MAX_VALUE).addComponent(btnLogin).addGap(60)));
		login.setLayout(gl_contentPane);
		panel.add(login, Constants.PanelNames.LOGIN_PANEL);
	}

	private void addModifyUserForm() {
		JTextField txtName;
		JTextField txtSurname;
		JTextField txtPersonalNo;
		JTextField txtEmail;
		JTextField txtAddress;
		JPanel modifyUser = new JPanel();
		modifyUser.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblAd = new JLabel("Ad");

		txtName = new JTextField();
		txtName.setColumns(10);

		JLabel lblSoyad = new JLabel("Soyad");

		txtSurname = new JTextField();
		txtSurname.setColumns(10);

		JLabel lblnvan = new JLabel("\u00DCnvan");

		JComboBox cmbTitle = new JComboBox();
		Map<String, String> types = db.getUserTypes();
		for (Map.Entry<String, String> entry : types.entrySet()) {
			cmbTitle.addItem(entry.getKey());
		}

		JLabel lblPersonalNo = new JLabel("Personal No");

		txtPersonalNo = new JTextField();
		txtPersonalNo.setEditable(true);
		txtPersonalNo.setColumns(10);

		JButton btnModify = new JButton("G\u00FCncelle");

		JButton btnCancel = new JButton("\u0130ptal");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.ADMIN_MAIN_PANEL);
			}
		});

		JComboBox cmbGender = new JComboBox();
		cmbGender.addItem("E");
		cmbGender.addItem("K");

		JLabel lblCinsiyet = new JLabel("Cinsiyet");

		JLabel lblEmail = new JLabel("E-Mail");

		txtEmail = new JTextField();
		txtEmail.setColumns(30);

		JLabel lblAdres = new JLabel("Adres");

		txtAddress = new JTextField();
		txtAddress.setColumns(30);
		JLabel lblKullancGncelleme = new JLabel("Kullan\u0131c\u0131 G\u00FCncelleme");

		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User modifiedUser = new User();
				modifiedUser.setAddress(txtAddress.getText());
				modifiedUser.setEmail(txtEmail.getText());
				modifiedUser.setMidname("");
				modifiedUser.setName(txtName.getText());
				modifiedUser.setSurname(txtSurname.getText());
				modifiedUser.setTitle(types.get(cmbTitle.getSelectedItem().toString()));
				modifiedUser.setGender(cmbGender.getSelectedItem().toString());
				modifiedUser.setId(txtPersonalNo.getText());
				if (StringUtil.isNotNullOrEmpty(modifiedUser.getAddress())
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getEmail())
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getName())
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getSurname())
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getTitle())
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getGender()) 
						&& StringUtil.isNotNullOrEmpty(modifiedUser.getId())) {
					if (db.modifyUser(modifiedUser)) {
						txtAddress.setText("");
						txtEmail.setText("");
						txtName.setText("");
						txtSurname.setText("");
						txtPersonalNo.setText("");
						cmbTitle.setSelectedIndex(0);
						cmbGender.setSelectedIndex(0);
						modifiedUser = new User();
					} else {
						JOptionPane.showMessageDialog(modifyUser, "Kullanýcý güncellenemedi.");
					}
				} else {
					JOptionPane.showMessageDialog(modifyUser, Constants.EMPTY_VALUE_ERROR);
				}
			}
		});
		
		txtPersonalNo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				User u = db.getUserInfo(txtPersonalNo.getText());
				txtName.setText(u.getName());
				txtSurname.setText(u.getSurname());
				txtEmail.setText(u.getEmail());
				txtAddress.setText(u.getAddress());
				cmbGender.setSelectedItem(u.getGender());
				cmbTitle.setSelectedItem(Enums.getUserTypes().get(u.getTitle()));
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				User u = db.getUserInfo(txtPersonalNo.getText());
				txtName.setText(u.getName());
				txtSurname.setText(u.getSurname());
				txtEmail.setText(u.getEmail());
				txtAddress.setText(u.getAddress());
				cmbGender.setSelectedItem(u.getGender());
				cmbTitle.setSelectedItem(Enums.getUserTypes().get(u.getTitle()));
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(modifyUser);
		gl_contentPane.setHorizontalGroup(gl_contentPane
				.createParallelGroup(
						Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(
								gl_contentPane
										.createParallelGroup(
												Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup().addGap(62)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
														.addComponent(lblEmail).addComponent(lblPersonalNo)
														.addComponent(lblnvan).addComponent(lblSoyad)
														.addComponent(lblAd).addComponent(lblAdres)
														.addComponent(lblCinsiyet))
												.addGap(18)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
																.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 336,
																		Short.MAX_VALUE)
																.addComponent(txtSurname, GroupLayout.DEFAULT_SIZE, 336,
																		Short.MAX_VALUE)
																.addComponent(cmbTitle, 0, 336, Short.MAX_VALUE)
																.addComponent(txtPersonalNo, GroupLayout.DEFAULT_SIZE,
																		336, Short.MAX_VALUE)
																.addComponent(cmbGender, 0, 336, Short.MAX_VALUE))))
										.addGroup(gl_contentPane.createSequentialGroup().addGap(246)
												.addComponent(btnModify).addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 73,
														GroupLayout.PREFERRED_SIZE)))
						.addGap(216))
				.addGroup(gl_contentPane.createSequentialGroup().addGap(252).addComponent(lblKullancGncelleme)
						.addContainerGap(331, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap().addComponent(lblKullancGncelleme).addGap(24)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblAd).addComponent(
						txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblSoyad).addComponent(
						txtSurname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblnvan).addComponent(
						cmbTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblPersonalNo)
						.addComponent(txtPersonalNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmbGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCinsiyet))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
								.addGap(185).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnModify).addComponent(btnCancel)))
						.addComponent(lblAdres))
				.addGap(42)));
		modifyUser.setLayout(gl_contentPane);
		panel.add(modifyUser, Constants.PanelNames.MODIFY_USER_PANEL);
	}
	
	
	private void addModifyUserLessonForm() {
		JTextField txtName;
		JTextField txtSurname;
		JTextField txtPersonalNo;
		JTextField txtEmail;
		JPanel modifyUserLesson = new JPanel();
		modifyUserLesson.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblAd = new JLabel("Ad");

		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setColumns(10);

		JLabel lblSoyad = new JLabel("Soyad");

		txtSurname = new JTextField();
		txtSurname.setEditable(false);
		txtSurname.setColumns(10);

		JLabel lblnvan = new JLabel("\u00DCnvan");

		JLabel lblPersonalNo = new JLabel("Personal No");

		txtPersonalNo = new JTextField();
		txtPersonalNo.setEditable(false);
		txtPersonalNo.setColumns(10);

		JButton btnModify = new JButton("G\u00FCncelle");

		JButton btnCancel = new JButton("\u0130ptal");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(courseListLastScreen);
			}
		});

		JLabel lblEmail = new JLabel("E-Mail");

		txtEmail = new JTextField();
		txtEmail.setEditable(false);
		txtEmail.setColumns(30);
		JLabel lblKullancGncelleme = new JLabel("Kullan\u0131c\u0131 G\u00FCncelleme");

		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String personnalNo = txtPersonalNo.getText();
				int courseId = selectedCourse.getId();
				String midterm = txtMidTerm.getText();
				String finalScore = txtFinalNote.getText();
				if (StringUtil.isNotNullOrEmpty(personnalNo)
						&& StringUtil.isNotNullOrEmpty(midterm)
						&& StringUtil.isNotNullOrEmpty(finalScore) ) {
					BigDecimal mid = new BigDecimal(midterm);
					BigDecimal fin = new BigDecimal(finalScore);
					if (db.modifyUserScore(personnalNo,selectedCourse.getId(),mid,fin)) {
						JOptionPane.showMessageDialog(modifyUserLesson, "Notlar güncellendi.");
					} else {
						JOptionPane.showMessageDialog(modifyUserLesson, "Kullanýcý güncellenemedi.");
					}
				} else {
					JOptionPane.showMessageDialog(modifyUserLesson, Constants.EMPTY_VALUE_ERROR);
				}
			}
		});
		
		JLabel lblMidTerm = new JLabel("MidTerm");
		
		txtMidTerm = new JTextField();
		txtMidTerm.setColumns(10);
		
		JLabel lblFinal = new JLabel("Final");
		
		txtFinalNote = new JTextField();
		txtFinalNote.setColumns(10);
		
		JLabel lblDersinAd = new JLabel("Dersin Ad\u0131");
		
		txtCourseName = new JTextField();
		txtCourseName.setEditable(false);
		txtCourseName.setColumns(10);
		
		JLabel lblDanman = new JLabel("Dan\u0131\u015Fman");
		
		txtMentor = new JTextField();
		txtMentor.setEditable(false);
		txtMentor.setColumns(10);
		
		txtTitle = new JTextField();
		txtTitle.setEditable(false);
		txtTitle.setColumns(10);
		
		txtCourseName.setText(selectedCourse.getName());
		txtMidTerm.setText(selectedCourse.getMidterm().toPlainString());
		txtFinalNote.setText(selectedCourse.getFinalScore().toPlainString());
		User u = db.getUserInfo(selectedCourse.getStudentId());
		txtName.setText(u.getName());
		txtSurname.setText(u.getSurname());
		txtPersonalNo.setText(u.getId());
		txtEmail.setText(u.getEmail());
		txtTitle.setText(Enums.getUserTypes().get(u.getTitle()));
		txtMentor.setText(u.getMentorName());

		GroupLayout gl_contentPane = new GroupLayout(modifyUserLesson);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(95)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblPersonalNo)
								.addComponent(lblnvan)
								.addComponent(lblSoyad)
								.addComponent(lblAd)
								.addComponent(lblEmail)
								.addComponent(lblDersinAd)
								.addComponent(lblDanman))
							.addGap(18))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblMidTerm)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblFinal)
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtTitle, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtMidTerm, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtMentor, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtCourseName, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtEmail, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtSurname, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtPersonalNo, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(txtFinalNote, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
					.addGap(271))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(316)
					.addComponent(lblKullancGncelleme)
					.addContainerGap(423, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(291)
					.addComponent(btnModify)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(404, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(20)
					.addComponent(lblKullancGncelleme)
					.addGap(68)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAd)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSoyad)
						.addComponent(txtSurname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblnvan)
						.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPersonalNo)
						.addComponent(txtPersonalNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmail)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDersinAd)
						.addComponent(txtCourseName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(22)
							.addComponent(lblDanman))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(30)
							.addComponent(txtMentor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(34)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtMidTerm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMidTerm))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtFinalNote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFinal))
					.addGap(93)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnModify)
						.addComponent(btnCancel))
					.addGap(54))
		);
		modifyUserLesson.setLayout(gl_contentPane);
		panel.add(modifyUserLesson, Constants.PanelNames.MODIFY_USER_LESSON);
	}

	JTable table = new JTable();
	DefaultTableModel model = null;
	private JTextField txtMidTerm;
	private JTextField txtFinalNote;
	private JTextField txtCourseName;
	private JTextField txtMentor;
	private JTextField txtTitle;
	
	private String getCourseType(String type){
		String response = "";
		if(type.equals("1") || type.equals("2")  || type.equals("3") || type.equals("4") || type.equals("5")){
			response = "IN (1,2,3)";
		}else if(type.equals("6")){
			response = "IN (1,2)";
		}else if(type.equals("7")){
			response = "IN (3)";
		}else if(type.equals("8")){
			response = "IN (1,2,3)";
		}
		return response;
	}
	class ButtonRenderer extends JButton implements TableCellRenderer {

		  public ButtonRenderer() {
		    setOpaque(true);
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    if (isSelected) {
		      setForeground(table.getSelectionForeground());
		      setBackground(table.getSelectionBackground());
		    } else {
		      setForeground(table.getForeground());
		      setBackground(UIManager.getColor("Button.background"));
		    }
		    setText((value == null) ? "" : value.toString());
		    return this;
		  }
		}

		/**
		 * @version 1.0 11/09/98
		 */

		class ButtonEditor extends DefaultCellEditor {
		  protected JButton button;

		  private String label;

		  private boolean isPushed;

		  public ButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton();
		    button.setOpaque(true);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        fireEditingStopped();
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {
		    if (isSelected) {
		      button.setForeground(table.getSelectionForeground());
		      button.setBackground(table.getSelectionBackground());
		    } else {
		      button.setForeground(table.getForeground());
		      button.setBackground(table.getBackground());
		    }
		    label = (value == null) ? "" : value.toString();
		    button.setText(label);
		    isPushed = true;
		    return button;
		  }

		  public Object getCellEditorValue() {
		    if (isPushed) {
		    	if(courseActionName.equals("ADD")){
		    		if(db.assignCourse(courses.get(table.getSelectedRow()), loginUser.getId())){
				    	  button.setEnabled(false);
				      }
		    	}else if (courseActionName.equals("DROP")){
		    		if(db.dropCourse(selectedCourses.get(table.getSelectedRow()), loginUser.getId())){
		    			button.setEnabled(false);
		    		}
		    	} else if (courseActionName.equals("DETAIL")){
		    		selectedCourse = studentCourseList.get(table.getSelectedRow());
		    		addModifyUserLessonForm();
		    		changeLayout(Constants.PanelNames.MODIFY_USER_LESSON);
		    	} else if (courseActionName.equals("COUNSELOR_DETAIL")){
		    		counselor = counselorList.get(table.getSelectedRow());
		    		addCounselorStudentDetailForm();
		    		changeLayout(Constants.PanelNames.SHOW_COUNSELOR_DETAIL);
		    	}
		    }
		    isPushed = false;
		    return new String(label);
		  }

		  public boolean stopCellEditing() {
		    isPushed = false;
		    return super.stopCellEditing();
		  }

		  protected void fireEditingStopped() {
		    super.fireEditingStopped();
		  }
		}
		  
	private void addCourseListForm(){
		JPanel courseList = new JPanel();
		courseActionName = "ADD";
		Object[] columnNames = {"Kodu", "Dersin Adý", "Öðretmen", "Asistan", "Kontenjan","Button"};
		courses = db.getCourseList(getCourseType(loginUser.getTitle()),loginUser.getId());
		Object[][] data = new Object[courses.size()][columnNames.length]; 
		for(int i = 0; i < courses.size(); i++){
			data[i][0] = courses.get(i).getCode();
			data[i][1] = courses.get(i).getName();
			data[i][2] = courses.get(i).getTeacher();
			data[i][3] = courses.get(i).getResearchAssistant();
			data[i][4] = courses.get(i).getSeatsLeft();
			data[i][5] = " Ekle ";
		}
		model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row) && isColumnSelected(column)) {
                    ((JComponent) c).setBorder(new LineBorder(Color.red));
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        JScrollPane scrollTable = new JScrollPane(table);
        panel.add(scrollTable, BorderLayout.CENTER);
        table.getColumn("Button").setCellRenderer(new ButtonRenderer());
        table.getColumn("Button").setCellEditor(
            new ButtonEditor(new JCheckBox()));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		panel.add(courseList,Constants.PanelNames.COURSE_LIST_PANEL);
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(courseListLastScreen);
			}
		});
		GroupLayout gl_courseList = new GroupLayout(courseList);
		gl_courseList.setHorizontalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGroup(gl_courseList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(btnGeri))
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_courseList.setVerticalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_courseList.createSequentialGroup()
					.addGap(18)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 547, Short.MAX_VALUE)
					.addComponent(btnGeri)
					.addGap(61))
		);
		courseList.setLayout(gl_courseList);
	}
	
	
	private void addSelectedCourseListForm(){
		JPanel selectedCourseList = new JPanel();
		Object[] columnNames = {"Kodu", "Dersin Adý", "Öðretmen", "Asistan", "Kontenjan","Midterm","Final","Kayýt"};
		selectedCourses = db.getSelectedCourseList(getCourseType(loginUser.getTitle()),loginUser.getId());
		Object[][] data = new Object[selectedCourses.size()][columnNames.length]; 
		for(int i = 0; i < selectedCourses.size(); i++){
			data[i][0] = selectedCourses.get(i).getCode();
			data[i][1] = selectedCourses.get(i).getName();
			data[i][2] = selectedCourses.get(i).getTeacher();
			data[i][3] = selectedCourses.get(i).getResearchAssistant();
			data[i][4] = selectedCourses.get(i).getSeatsLeft();
			data[i][5] = selectedCourses.get(i).getMidterm();
			data[i][6] = selectedCourses.get(i).getFinalScore();
			data[i][7] = " Sil ";
		}
		model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row) && isColumnSelected(column)) {
                    ((JComponent) c).setBorder(new LineBorder(Color.red));
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        table.getColumn("Kayýt").setCellRenderer(new ButtonRenderer());
        table.getColumn("Kayýt").setCellEditor(
            new ButtonEditor(new JCheckBox()));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		panel.add(selectedCourseList,Constants.PanelNames.SELECTED_COURSE_LIST_PANEL);
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(courseListLastScreen);
			}
		});
		GroupLayout gl_courseList = new GroupLayout(selectedCourseList);
		gl_courseList.setHorizontalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGroup(gl_courseList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(btnGeri)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_courseList.setVerticalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGap(18)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnGeri)
					.addGap(61))
		);
		selectedCourseList.setLayout(gl_courseList);
	}
	
	private void addStudentListForm(){
		JPanel selectedCourseList = new JPanel();
		courseActionName = "DETAIL";
		Object[] columnNames = {"Kodu", "Dersin Adý", "Öðrenci", "Asistan", "Kontenjan","Midterm","Final","Detay"};
		studentCourseList = db.getStudentCourseList(loginUser.getId());
		Object[][] data = new Object[studentCourseList.size()][columnNames.length]; 
		for(int i = 0; i < studentCourseList.size(); i++){
			data[i][0] = studentCourseList.get(i).getCode();
			data[i][1] = studentCourseList.get(i).getName();
			data[i][2] = studentCourseList.get(i).getStudentName();
			data[i][3] = studentCourseList.get(i).getResearchAssistant();
			data[i][4] = studentCourseList.get(i).getSeatsLeft();
			data[i][5] = studentCourseList.get(i).getMidterm();
			data[i][6] = studentCourseList.get(i).getFinalScore();
			data[i][7] = " Detay ";
		}
		model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row) && isColumnSelected(column)) {
                    ((JComponent) c).setBorder(new LineBorder(Color.red));
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        table.getColumn("Detay").setCellRenderer(new ButtonRenderer());
        table.getColumn("Detay").setCellEditor(
            new ButtonEditor(new JCheckBox()));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(courseListLastScreen);
			}
		});
		GroupLayout gl_courseList = new GroupLayout(selectedCourseList);
		gl_courseList.setHorizontalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGroup(gl_courseList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(btnGeri)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_courseList.setVerticalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGap(18)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnGeri)
					.addGap(61))
		);
		selectedCourseList.setLayout(gl_courseList);
		panel.add(selectedCourseList,Constants.PanelNames.SELECTED_STUDENT_LIST);
	}
	
	
	private void addCounselorStudentsListForm(){
		JPanel counselorStudentsList = new JPanel();
		courseActionName = "COUNSELOR_DETAIL";
		Object[] columnNames = {"Öðrenci-Ad", "Öðrenci-Soyad","Öðrenci-No","Öðrenci-Mail","Detay"};
		counselorList = db.getCounselorStudentsList(loginUser.getId());
		Object[][] data = new Object[counselorList.size()][columnNames.length]; 
		for(int i = 0; i < counselorList.size(); i++){
			data[i][0] = counselorList.get(i).getName();
			data[i][1] = counselorList.get(i).getSurname();
			data[i][2] = counselorList.get(i).getUserId();
			data[i][3] = counselorList.get(i).getEmail();
			data[i][4] = " Detay ";
		}
		model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row) && isColumnSelected(column)) {
                    ((JComponent) c).setBorder(new LineBorder(Color.red));
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        table.getColumn("Detay").setCellRenderer(new ButtonRenderer());
        table.getColumn("Detay").setCellEditor(
            new ButtonEditor(new JCheckBox()));
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(courseListLastScreen);
			}
		});
		GroupLayout gl_courseList = new GroupLayout(counselorStudentsList);
		gl_courseList.setHorizontalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGroup(gl_courseList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(btnGeri)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_courseList.setVerticalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGap(18)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnGeri)
					.addGap(61))
		);
		counselorStudentsList.setLayout(gl_courseList);
		panel.add(counselorStudentsList,Constants.PanelNames.SHOW_COUNSELOR_STUDENTS);
	}
	
	private void addCounselorStudentDetailForm(){
		JPanel counselorStudentDetail = new JPanel();
		courseActionName = "COUNSELOR_DETAIL";
		Object[] columnNames = {"Öðrenci-Ad", "Öðrenci-Soyad","Öðrenci-No","Öðrenci-Mail","Ders","Mid-Term","Final"};
		List<CounselorDetail> counselorDetailList = db.getCounselorStudentDetailList(loginUser.getId(),counselor.getUserId());
		Object[][] data = new Object[counselorDetailList.size()][columnNames.length]; 
		for(int i = 0; i < counselorDetailList.size(); i++){
			data[i][0] = counselorDetailList.get(i).getStudentName();
			data[i][1] = counselorDetailList.get(i).getStudentSurname();
			data[i][2] = counselorDetailList.get(i).getUserId();
			data[i][3] = counselorDetailList.get(i).getStudentEmail();
			data[i][4] = counselorDetailList.get(i).getLessonName();
			data[i][5] = counselorDetailList.get(i).getMidtermScore();
			data[i][6] = counselorDetailList.get(i).getFinalScore();
		}
		model = new DefaultTableModel(data, columnNames) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model) {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row) && isColumnSelected(column)) {
                    ((JComponent) c).setBorder(new LineBorder(Color.red));
                }
                return c;
            }
        };
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		JButton btnGeri = new JButton("Geri");
		btnGeri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(courseListLastScreen);
			}
		});
		GroupLayout gl_courseList = new GroupLayout(counselorStudentDetail);
		gl_courseList.setHorizontalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGroup(gl_courseList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_courseList.createSequentialGroup()
							.addGap(50)
							.addComponent(btnGeri)))
					.addContainerGap(50, Short.MAX_VALUE))
		);
		gl_courseList.setVerticalGroup(
			gl_courseList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_courseList.createSequentialGroup()
					.addGap(18)
					.addComponent(table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
					.addComponent(btnGeri)
					.addGap(61))
		);
		counselorStudentDetail.setLayout(gl_courseList);
		panel.add(counselorStudentDetail,Constants.PanelNames.SHOW_COUNSELOR_DETAIL);
	}
	
	private void changeLayout(String layoutName) {
		CardLayout cl = (CardLayout) panel.getLayout();
		cl.show(panel, layoutName);
	}

	private String generatePersonnalNumber() {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		return ft.format(dNow);
	}

	private void addRegisterForm() {
		JPanel register = new JPanel();
		register.setBorder(new EmptyBorder(5, 5, 5, 5));
		JTextField txtName;
		JTextField txtSurname;
		JTextField txtPersonalNo;
		JTextField txtEmail;
		JTextField txtAddress;
		JLabel lblAd = new JLabel("Ad");

		txtName = new JTextField();
		txtName.setColumns(10);

		JLabel lblSoyad = new JLabel("Soyad");

		txtSurname = new JTextField();
		txtSurname.setColumns(10);

		JLabel lblnvan = new JLabel("\u00DCnvan");

		JComboBox cmbTitle = new JComboBox();
		Map<String, String> types = db.getUserTypes();
		for (Map.Entry<String, String> entry : types.entrySet()) {
			cmbTitle.addItem(entry.getKey());
		}
		
		JLabel lblPersonalNo = new JLabel("Personal No");

		txtPersonalNo = new JTextField();
		txtPersonalNo.setEditable(false);
		txtPersonalNo.setColumns(10);
		txtPersonalNo.setText(generatePersonnalNumber());

		JButton btnSave = new JButton("Kaydet");

		JButton btnCancel = new JButton("\u0130ptal");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.ADMIN_MAIN_PANEL);
			}
		});

		JComboBox cmbGender = new JComboBox();
		cmbGender.addItem("E");
		cmbGender.addItem("K");

		JLabel lblCinsiyet = new JLabel("Cinsiyet");

		JLabel lblEmail = new JLabel("E-Mail");

		txtEmail = new JTextField();
		txtEmail.setColumns(30);

		JLabel lblAdres = new JLabel("Adres");

		txtAddress = new JTextField();
		txtAddress.setColumns(30);
		Map<String,String> list = db.getTeachingStaffList(Boolean.FALSE);
		
		txtName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}
			}
		});

		txtSurname.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String name = txtName.getText();
				String surname = txtSurname.getText();
				if (name != null && surname != null) {
					txtEmail.setText(name + "." + surname + "@ube.gov.tr");
				}
			}
		});
		
		JLabel lblMentor = new JLabel("Mentor");
		
		JComboBox cmbMentor = new JComboBox();
		for (Map.Entry<String, String> entry : list.entrySet()) {
			cmbMentor.addItem(entry.getKey());
		}
		lblMentor.setVisible(false);
    	cmbMentor.setVisible(false);
		
		ActionListener titleActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) cmbTitle.getSelectedItem();
				switch (s) {
                case "Tezli Y.L":
                	lblMentor.setVisible(true);
                	cmbMentor.setVisible(true);
                    break;
                case "Tezsiz Y.L":
                	lblMentor.setVisible(true);
                	cmbMentor.setVisible(true);
                    break;
                case "Doktora":
                	lblMentor.setVisible(true);
                	cmbMentor.setVisible(true);
                    break;
                default:
                	lblMentor.setVisible(false);
                	cmbMentor.setVisible(false);
                    break;
            }
			}
		};
		
		cmbTitle.addActionListener(titleActionListener);
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User createdUser = new User();
				createdUser.setAddress(txtAddress.getText());
				createdUser.setEmail(txtEmail.getText());
				createdUser.setMidname("");
				createdUser.setName(txtName.getText());
				createdUser.setSurname(txtSurname.getText());
				createdUser.setTitle(types.get(cmbTitle.getSelectedItem().toString()));
				createdUser.setGender(cmbGender.getSelectedItem().toString());
				createdUser.setMentorName(cmbMentor.getSelectedItem().toString());
				createdUser.setMentorId(list.get(cmbMentor.getSelectedItem().toString()));
				if (StringUtil.isNotNullOrEmpty(createdUser.getAddress())
						&& StringUtil.isNotNullOrEmpty(createdUser.getEmail())
						&& StringUtil.isNotNullOrEmpty(createdUser.getName())
						&& StringUtil.isNotNullOrEmpty(createdUser.getSurname())
						&& StringUtil.isNotNullOrEmpty(createdUser.getTitle())
						&& StringUtil.isNotNullOrEmpty(createdUser.getGender())) {
					if (db.createUser(createdUser, txtPersonalNo.getText(), "test")) {
						txtAddress.setText("");
						txtEmail.setText("");
						txtName.setText("");
						txtSurname.setText("");
						txtPersonalNo.setText(generatePersonnalNumber());
						cmbTitle.setSelectedIndex(0);
						cmbGender.setSelectedIndex(0);
						createdUser = new User();
					} else {
						JOptionPane.showMessageDialog(register, "Kullanýcý oluþturulamadý.");
					}
				} else {
					JOptionPane.showMessageDialog(register, Constants.EMPTY_VALUE_ERROR);
				}
			}
		});
		
		
		GroupLayout gl_contentPane = new GroupLayout(register);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(62)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblEmail)
								.addComponent(lblPersonalNo)
								.addComponent(lblnvan)
								.addComponent(lblSoyad)
								.addComponent(lblAd)
								.addComponent(lblAdres)
								.addComponent(lblCinsiyet)
								.addComponent(lblMentor))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
									.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
									.addComponent(txtSurname, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
									.addComponent(cmbTitle, 0, 340, Short.MAX_VALUE)
									.addComponent(txtPersonalNo, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
									.addComponent(cmbGender, 0, 340, Short.MAX_VALUE))
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(cmbMentor, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(txtAddress, Alignment.LEADING))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(167)
							.addComponent(btnSave)
							.addGap(95)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
					.addGap(216))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(53)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAd)
						.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSoyad)
						.addComponent(txtSurname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblnvan)
						.addComponent(cmbTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPersonalNo)
						.addComponent(txtPersonalNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(cmbGender, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCinsiyet))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
							.addGap(185)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnSave)
								.addComponent(btnCancel)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblAdres)
							.addGap(108)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMentor)
								.addComponent(cmbMentor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(42))
		);
		register.setLayout(gl_contentPane);
		panel.add(register, Constants.PanelNames.REGISTER_PANEL);
	}

	private void addAdminMainForm() {
		JPanel adminMain = new JPanel();
		adminMain.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnKullancYaratma = new JButton("Kullan\u0131c\u0131 Yaratma");
		btnKullancYaratma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(Constants.PanelNames.REGISTER_PANEL);
			}
		});

		JButton btnDersYaratma = new JButton("Ders Yaratma");
		btnDersYaratma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changeLayout(Constants.PanelNames.LESSON_CREATE_PANEL);
			}
		});

		JButton btnDersGncelle = new JButton("Ders G\u00FCncelle");
		btnDersGncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.LESSON_MODIFY_PANEL);
			}
		});

		JButton btnKullancGncelleme = new JButton("Kullan\u0131c\u0131 G\u00FCncelleme");
		btnKullancGncelleme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.MODIFY_USER_PANEL);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(adminMain);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(27)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnKullancGncelleme, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnKullancYaratma, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(18)
						.addComponent(btnDersYaratma, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
						.addGap(27)
						.addComponent(btnDersGncelle, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(67, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(28)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(btnDersGncelle)
								.addComponent(btnKullancYaratma).addComponent(btnDersYaratma))
						.addGap(18).addComponent(btnKullancGncelleme).addContainerGap(147, Short.MAX_VALUE)));
		adminMain.setLayout(gl_contentPane);
		panel.add(adminMain, Constants.PanelNames.ADMIN_MAIN_PANEL);
	}

	private void addLessonCreateForm() {
		JPanel lessonCreate = new JPanel();
		lessonCreate.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblDersAd = new JLabel("Ders Ad\u0131");
		JTextField txtLessonName;
		JTextField txtLessonCode;
		txtLessonName = new JTextField();
		txtLessonName.setColumns(100);

		JLabel lblDersTipi = new JLabel("Ders Tipi");

		JComboBox cmbLessonType = new JComboBox();
		List<CourseType> types = db.getCourseTypes();
		for (CourseType type : types) {
			cmbLessonType.addItem(type.getName());
		}

		JLabel lblDersKodu = new JLabel("Ders Kodu");

		txtLessonCode = new JTextField();
		txtLessonCode.setColumns(10);

		JLabel lblDersiVeren = new JLabel("Dersi Veren");

		JComboBox cmbTeacher = new JComboBox();
		Map<String, String> staffList = db.getTeachingStaffList(Boolean.FALSE);
		for (Map.Entry<String, String> entry : staffList.entrySet()) {
			cmbTeacher.addItem(entry.getKey());
		}

		JLabel lblAratrmaGrevlisi = new JLabel("Ara\u015Ft\u0131rma G\u00F6revlisi");

		JComboBox cmbResearchAssistant = new JComboBox();
		Map<String, String> researchAssistantMap = db.getTeachingStaffList(Boolean.TRUE);
		for (Map.Entry<String, String> entry : researchAssistantMap.entrySet()) {
			cmbResearchAssistant.addItem(entry.getKey());
		}
		JLabel lblDersKayt = new JLabel("Ders Kay\u0131t");
		lblDersKayt.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnKaydet = new JButton("Kaydet");
		btnKaydet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Course course = new Course();
				course.setCode(txtLessonCode.getText());
				course.setName(txtLessonName.getText());
				course.setTeacher(staffList.get(cmbTeacher.getSelectedItem().toString()));
				course.setResearchAssistant(
						researchAssistantMap.get(cmbResearchAssistant.getSelectedItem().toString()));
				course.setType(getCourseType(types, cmbLessonType.getSelectedItem().toString()));
				if (course.getType() != null && StringUtil.isNotNullOrEmpty(course.getCode())
						&& StringUtil.isNotNullOrEmpty(course.getName())
						&& StringUtil.isNotNullOrEmpty(course.getTeacher())
						&& StringUtil.isNotNullOrEmpty(course.getResearchAssistant())) {
					if (db.addCourse(course)) {
						txtLessonCode.setText("");
						txtLessonName.setText("");
						cmbTeacher.setSelectedIndex(0);
						cmbResearchAssistant.setSelectedIndex(0);
						course = new Course();
						JOptionPane.showMessageDialog(lessonCreate, "Ders oluþturuldu.");
					} else {
						JOptionPane.showMessageDialog(lessonCreate, "Ders oluþturulamadý.");
					}
				} else {
					JOptionPane.showMessageDialog(lessonCreate, Constants.EMPTY_VALUE_ERROR);
				}
			}
		});

		JButton btnCancel = new JButton("\u0130ptal");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.ADMIN_MAIN_PANEL);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(lessonCreate);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(25)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblAratrmaGrevlisi)
										.addComponent(lblDersiVeren).addComponent(lblDersKodu).addComponent(lblDersTipi)
										.addComponent(lblDersAd))
								.addGap(18)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(cmbResearchAssistant, 0, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(cmbTeacher, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtLessonCode)
										.addComponent(cmbLessonType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtLessonName, GroupLayout.PREFERRED_SIZE, 435,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(btnKaydet)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(btnCancel))))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(295).addComponent(lblDersKayt)))
				.addContainerGap(86, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap().addComponent(lblDersKayt).addGap(90)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblDersAd).addComponent(
						txtLessonName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblDersTipi).addComponent(
						cmbLessonType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblDersKodu).addComponent(
						txtLessonCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblDersiVeren)
						.addComponent(cmbTeacher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblAratrmaGrevlisi)
						.addComponent(cmbResearchAssistant, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE).addGroup(gl_contentPane
						.createParallelGroup(Alignment.BASELINE).addComponent(btnKaydet).addComponent(btnCancel))
				.addGap(29)));
		lessonCreate.setLayout(gl_contentPane);
		panel.add(lessonCreate, Constants.PanelNames.LESSON_CREATE_PANEL);
	}
	
	
	private void addLessonModifyForm() {
		JPanel lessonModify = new JPanel();
		lessonModify.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblDersAd = new JLabel("Ders Ad\u0131");
		JTextField txtLessonName;
		JTextField txtLessonCode;
		txtLessonName = new JTextField();
		txtLessonName.setColumns(100);

		JLabel lblDersTipi = new JLabel("Ders Tipi");

		JComboBox cmbLessonType = new JComboBox();
		List<CourseType> types = db.getCourseTypes();
		for (CourseType type : types) {
			cmbLessonType.addItem(type.getName());
		}

		JLabel lblDersKodu = new JLabel("Ders Kodu");

		txtLessonCode = new JTextField();
		txtLessonCode.setColumns(10);

		JLabel lblDersiVeren = new JLabel("Dersi Veren");

		JComboBox cmbTeacher = new JComboBox();
		Map<String, String> staffList = db.getTeachingStaffList(Boolean.FALSE);
		for (Map.Entry<String, String> entry : staffList.entrySet()) {
			cmbTeacher.addItem(entry.getKey());
		}

		JLabel lblAratrmaGrevlisi = new JLabel("Ara\u015Ft\u0131rma G\u00F6revlisi");

		JComboBox cmbResearchAssistant = new JComboBox();
		Map<String, String> researchAssistantMap = db.getTeachingStaffList(Boolean.TRUE);
		for (Map.Entry<String, String> entry : researchAssistantMap.entrySet()) {
			cmbResearchAssistant.addItem(entry.getKey());
		}
		JLabel lblDersModify = new JLabel("Ders G\u00FCncelleme");
		lblDersModify.setFont(new Font("Tahoma", Font.PLAIN, 15));

		JButton btnModify = new JButton("G\u00FCncelle");
		btnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Course course = db.getCourseByCode(txtLessonCode.getText());
				course.setCode(txtLessonCode.getText());
				course.setName(txtLessonName.getText());
				course.setTeacher(staffList.get(cmbTeacher.getSelectedItem().toString()));
				course.setResearchAssistant(
						researchAssistantMap.get(cmbResearchAssistant.getSelectedItem().toString()));
				course.setType(getCourseType(types, cmbLessonType.getSelectedItem().toString()));
				if (course.getType() != null && StringUtil.isNotNullOrEmpty(course.getCode())
						&& StringUtil.isNotNullOrEmpty(course.getName())
						&& StringUtil.isNotNullOrEmpty(course.getTeacher())
						&& StringUtil.isNotNullOrEmpty(course.getResearchAssistant())
						) {
					if (db.modifyCourse(course)) {
						txtLessonCode.setText("");
						txtLessonName.setText("");
						cmbTeacher.setSelectedIndex(0);
						cmbResearchAssistant.setSelectedIndex(0);
						course = new Course();
						JOptionPane.showMessageDialog(lessonModify, "Ders güncellendi.");
					} else {
						JOptionPane.showMessageDialog(lessonModify, "Ders güncellenemedi.");
					}
				} else {
					JOptionPane.showMessageDialog(lessonModify, Constants.EMPTY_VALUE_ERROR);
				}
			}
		});

		JButton btnCancel = new JButton("\u0130ptal");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(Constants.PanelNames.ADMIN_MAIN_PANEL);
			}
		});
		
		txtLessonCode.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				String code = txtLessonCode.getText();
				Course course = db.getCourseByCode(code);
				if(course != null){
					txtLessonName.setText(course.getName());
					cmbLessonType.setSelectedItem(course.getType().getName());
					cmbResearchAssistant.setSelectedItem(course.getResearchAssistant());
					cmbTeacher.setSelectedItem(course.getTeacher());
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String code = txtLessonCode.getText();
				Course course = db.getCourseByCode(code);
				if(course != null){
					txtLessonName.setText(course.getName());
					cmbLessonType.setSelectedItem(course.getType().getName());
					cmbResearchAssistant.setSelectedItem(course.getResearchAssistant());
					cmbTeacher.setSelectedItem(course.getTeacher());
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String code = txtLessonCode.getText();
				Course course = db.getCourseByCode(code);
				if(course != null){
					txtLessonName.setText(course.getName());
					cmbLessonType.setSelectedItem(course.getType().getName());
					cmbResearchAssistant.setSelectedItem(course.getResearchAssistant());
					cmbTeacher.setSelectedItem(course.getTeacher());
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(lessonModify);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addGap(25)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblAratrmaGrevlisi)
										.addComponent(lblDersiVeren).addComponent(lblDersKodu).addComponent(lblDersTipi)
										.addComponent(lblDersAd))
								.addGap(18)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(cmbResearchAssistant, 0, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(cmbTeacher, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtLessonCode)
										.addComponent(cmbLessonType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(txtLessonName, GroupLayout.PREFERRED_SIZE, 435,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(btnModify)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(btnCancel))))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(295).addComponent(lblDersModify)))
				.addContainerGap(86, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup().addContainerGap().addComponent(lblDersModify).addGap(90)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblDersAd).addComponent(
						txtLessonName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblDersTipi).addComponent(
						cmbLessonType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblDersKodu).addComponent(
						txtLessonCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblDersiVeren)
						.addComponent(cmbTeacher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblAratrmaGrevlisi)
						.addComponent(cmbResearchAssistant, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 85, Short.MAX_VALUE).addGroup(gl_contentPane
						.createParallelGroup(Alignment.BASELINE).addComponent(btnModify).addComponent(btnCancel))
				.addGap(29)));
		lessonModify.setLayout(gl_contentPane);
		panel.add(lessonModify, Constants.PanelNames.LESSON_MODIFY_PANEL);
	}

	private void addChangePasswordPanel() {
		JPanel changePassword = new JPanel();
		changePassword.setBorder(new EmptyBorder(5, 5, 5, 5));
		JTextField txtCurrentPassword;
		JTextField txtNewPassword;
		JTextField txtNewPasswordRe;
		JLabel lblMevcutifre = new JLabel("Mevcut \u015Eifre");

		txtCurrentPassword = new JTextField();
		txtCurrentPassword.setColumns(100);

		JLabel lblYeniifre = new JLabel("Yeni \u015Eifre");

		txtNewPassword = new JTextField();
		txtNewPassword.setColumns(100);

		JLabel lblYeniifreTekrar = new JLabel("Yeni \u015Eifre Tekrar");

		txtNewPasswordRe = new JTextField();
		txtNewPasswordRe.setColumns(100);

		JButton btnBack = new JButton("Geri");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeLayout(lastScreen);
			}
		});

		JButton btnSave = new JButton("Kaydet");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String oldPass = txtCurrentPassword.getText();
				String newPass = txtNewPassword.getText();
				String newPassRe = txtNewPasswordRe.getText();
				if (StringUtil.isNotNullOrEmpty(oldPass) && StringUtil.isNotNullOrEmpty(newPass)
						&& StringUtil.isNotNullOrEmpty(newPassRe)) {
					if (newPass.equals(newPassRe)) {
						String response = db.changeUserPassword(loginUser.getId(), oldPass, newPass);
						JOptionPane.showMessageDialog(changePassword, response);
					} else {
						JOptionPane.showMessageDialog(changePassword, "Yeni þifreniz uyuþmuyor.");
					}
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(changePassword);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(lblMevcutifre)
								.addComponent(lblYeniifre).addComponent(lblYeniifreTekrar))
						.addGap(18)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(txtCurrentPassword, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING,
										gl_contentPane.createSequentialGroup().addComponent(btnSave)
												.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnBack))
								.addComponent(txtNewPasswordRe, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
								.addComponent(txtNewPassword, 0, 0, Short.MAX_VALUE))
						.addContainerGap(72, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(31)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblMevcutifre).addGap(31)
										.addComponent(lblYeniifre).addGap(31).addComponent(lblYeniifreTekrar))
								.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(txtCurrentPassword, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(31)
										.addComponent(txtNewPassword, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(31).addComponent(txtNewPasswordRe, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGap(62).addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnBack).addComponent(btnSave))
						.addContainerGap(168, Short.MAX_VALUE)));
		changePassword.setLayout(gl_contentPane);
		panel.add(changePassword, Constants.PanelNames.CHANGE_PASSWORD_PANEL);
	}

	private void addStudentMainForm() {
		JPanel studentMain = new JPanel();
		courseActionName = "DROP";
		studentMain.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnSifreGncelle = new JButton("\u015Eifre G\u00FCncelle");
		btnSifreGncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastScreen = Constants.PanelNames.STUDENT_MAIN_PANEL;
				changeLayout(Constants.PanelNames.CHANGE_PASSWORD_PANEL);
			}
		});
		
		JButton btnDersSe = new JButton("Ders Se\u00E7");
		btnDersSe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCourseListForm();
				courseListLastScreen = Constants.PanelNames.STUDENT_MAIN_PANEL;
				changeLayout(Constants.PanelNames.COURSE_LIST_PANEL);
			}
		});
		
		JButton btnDersleriGr = new JButton("Dersleri G\u00F6r");
		btnDersleriGr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addSelectedCourseListForm();
				courseListLastScreen = Constants.PanelNames.STUDENT_MAIN_PANEL;
				changeLayout(Constants.PanelNames.SELECTED_COURSE_LIST_PANEL);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(studentMain);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(56)
					.addComponent(btnSifreGncelle)
					.addGap(28)
					.addComponent(btnDersSe)
					.addGap(26)
					.addComponent(btnDersleriGr)
					.addContainerGap(294, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSifreGncelle)
						.addComponent(btnDersSe)
						.addComponent(btnDersleriGr))
					.addContainerGap(585, Short.MAX_VALUE))
		);
		studentMain.setLayout(gl_contentPane);
		panel.add(studentMain, Constants.PanelNames.STUDENT_MAIN_PANEL);
	}
	
	private void addTeachingStaffMainForm() {
		JPanel teachingStaffMain = new JPanel();
		teachingStaffMain.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton btnSifreGncelle = new JButton("\u015Eifre G\u00FCncelle");
		btnSifreGncelle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastScreen = Constants.PanelNames.TEACHING_STAFF_MAIN_PANEL;
				changeLayout(Constants.PanelNames.CHANGE_PASSWORD_PANEL);
			}
		});
		
		JButton btnDersleriGr = new JButton("Dersleri G\u00F6r");
		btnDersleriGr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addStudentListForm();
				courseListLastScreen = Constants.PanelNames.TEACHING_STAFF_MAIN_PANEL;
				changeLayout(Constants.PanelNames.SELECTED_STUDENT_LIST);
			}
		});
		
		JButton btnShowCounselorStudents = new JButton("Dan\u0131\u015Fmanlar\u0131 G\u00F6r");
		btnShowCounselorStudents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCounselorStudentsListForm();
				courseListLastScreen = Constants.PanelNames.TEACHING_STAFF_MAIN_PANEL;
				changeLayout(Constants.PanelNames.SHOW_COUNSELOR_STUDENTS);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(teachingStaffMain);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(56)
					.addComponent(btnSifreGncelle)
					.addGap(28)
					.addComponent(btnDersleriGr)
					.addGap(18)
					.addComponent(btnShowCounselorStudents)
					.addContainerGap(290, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSifreGncelle)
						.addComponent(btnDersleriGr)
						.addComponent(btnShowCounselorStudents))
					.addContainerGap(585, Short.MAX_VALUE))
		);
		teachingStaffMain.setLayout(gl_contentPane);
		panel.add(teachingStaffMain, Constants.PanelNames.TEACHING_STAFF_MAIN_PANEL);
	}

	private CourseType getCourseType(List<CourseType> list, String element) {
		if (list != null && !list.isEmpty()) {
			for (CourseType ct : list) {
				if (ct.getName().equals(element)) {
					return ct;
				}
			}
		}
		return null;
	}

	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (panel.getLayout());
		cl.show(panel, (String) evt.getItem());
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("StudentInformation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StudentInformation si = new StudentInformation();
		si.addComponentToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				db = new DBOperations();
				createAndShowGUI();
			}
		});
	}
}