package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dto.Counselor;
import dto.CounselorDetail;
import dto.Course;
import dto.CourseType;
import user.Admin;
import user.ResearchAssistant;
import user.Student;
import user.TeachingStaff;
import user.User;
import util.Enums;

public class DBOperations {

	public Boolean login(String userID, String password) {
		Boolean isValidUser = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM users WHERE userId = ? and password = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userID);
			st.setString(2, password);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				isValidUser = Boolean.TRUE;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isValidUser;
	}

	public Map<String, String> getUserTypes() {
		Map<String, String> types = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM user_types";
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			types = new LinkedHashMap<>();
			while (rs.next()) {
				types.put(rs.getString("NAME"), rs.getString("ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

	public List<CourseType> getCourseTypes() {
		List<CourseType> courseTypes = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM lesson_types";
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			courseTypes = new ArrayList<>();
			while (rs.next()) {
				CourseType ct = new CourseType();
				ct.setId(rs.getInt("ID"));
				ct.setName(rs.getString("NAME"));
				ct.setPassGrade(rs.getInt("PASS_GRADE"));
				courseTypes.add(ct);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseTypes;
	}

	public CourseType getCourseType(int type) {
		CourseType courseType = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM lesson_types WHERE ID = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, type);
			ResultSet rs = st.executeQuery();
			courseType = new CourseType();
			while (rs.next()) {
				courseType.setId(rs.getInt("ID"));
				courseType.setName(rs.getString("NAME"));
				courseType.setPassGrade(rs.getInt("PASS_GRADE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courseType;
	}

	public List<Course> getCourseList(String type, String userId) {
		List<Course> courses = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT L.*,(SELECT NAME FROM USERS WHERE USERID = L.TEACHER) AS TEACHER_NAME, (SELECT NAME FROM USERS WHERE USERID = L.RESEARCH_ASSISTANT) AS ASSISTANT_NAME"
					+ " FROM LESSONS L WHERE L.LESSON_TYPE " + type
					+ " AND ID NOT IN (SELECT LESSONID FROM USER_LESSON_REL WHERE USERID = ?) ";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			courses = new ArrayList<>();
			while (rs.next()) {
				Course c = new Course();
				c.setId(rs.getInt("ID"));
				c.setName(rs.getString("NAME"));
				c.setCode(rs.getString("COURSE_CODE"));
				c.setType(getCourseType(rs.getInt("LESSON_TYPE")));
				c.setResearchAssistant(rs.getString("ASSISTANT_NAME"));
				c.setResearchAssistantId(rs.getString("RESEARCH_ASSISTANT"));
				c.setTeacher(rs.getString("TEACHER_NAME"));
				c.setTeacherId(rs.getString("TEACHER"));
				c.setSeatsLeft(rs.getInt("SEATS_LEFT"));
				courses.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courses;
	}

	public List<Course> getSelectedCourseList(String type, String userId) {
		List<Course> courses = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT L.*, (SELECT NAME FROM USERS WHERE USERID = L.TEACHER) AS TEACHER_NAME, (SELECT NAME FROM USERS WHERE USERID = L.RESEARCH_ASSISTANT) AS ASSISTANT_NAME,ULR.MIDTERM, ULR.FINAL "
					+ "FROM user_lesson_rel ulr, users u, LESSONS L WHERE u.USERID = ULR.userId AND ULR.LESSONID = L.ID AND U.USERID=?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			courses = new ArrayList<>();
			while (rs.next()) {
				Course c = new Course();
				c.setId(rs.getInt("ID"));
				c.setName(rs.getString("NAME"));
				c.setCode(rs.getString("COURSE_CODE"));
				c.setType(getCourseType(rs.getInt("LESSON_TYPE")));
				c.setResearchAssistant(rs.getString("ASSISTANT_NAME"));
				c.setResearchAssistantId(rs.getString("RESEARCH_ASSISTANT"));
				c.setTeacher(rs.getString("TEACHER_NAME"));
				c.setTeacherId(rs.getString("TEACHER"));
				c.setSeatsLeft(rs.getInt("SEATS_LEFT"));
				c.setMidterm(rs.getBigDecimal("MIDTERM"));
				c.setFinalScore(rs.getBigDecimal("FINAL"));
				courses.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courses;
	}

	public List<Course> getStudentCourseList(String userId) {
		List<Course> courses = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT L.*, (SELECT NAME FROM USERS WHERE USERID = ULR.userId) AS STUDENT_NAME,ULR.userId STUDENT_ID, (SELECT NAME FROM USERS WHERE USERID = L.RESEARCH_ASSISTANT) AS ASSISTANT_NAME,ULR.MIDTERM, ULR.FINAL "
					+ "FROM user_lesson_rel ulr, users u, LESSONS L WHERE u.USERID = ULR.userId AND ULR.LESSONID = L.ID AND L.TEACHER=?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			courses = new ArrayList<>();
			while (rs.next()) {
				Course c = new Course();
				c.setId(rs.getInt("ID"));
				c.setName(rs.getString("NAME"));
				c.setCode(rs.getString("COURSE_CODE"));
				c.setType(getCourseType(rs.getInt("LESSON_TYPE")));
				c.setResearchAssistant(rs.getString("ASSISTANT_NAME"));
				c.setResearchAssistantId(rs.getString("RESEARCH_ASSISTANT"));
				c.setTeacherId(rs.getString("TEACHER"));
				c.setSeatsLeft(rs.getInt("SEATS_LEFT"));
				c.setMidterm(rs.getBigDecimal("MIDTERM"));
				c.setFinalScore(rs.getBigDecimal("FINAL"));
				c.setStudentId(rs.getString("STUDENT_ID"));
				c.setStudentName(rs.getString("STUDENT_NAME"));
				courses.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return courses;
	}
	
	public List<Counselor> getCounselorStudentsList(String userId) {
		List<Counselor> counselorList = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "select U.name AS AD,U.surname,U.userId,U.email from users u where u.userId = (select ulr.userid from user_lesson_rel ulr where ulr.userId = "
					+ "(SELECT studentId FROM student_mentor_rel WHERE mentorId = ?) group by ulr.userid) ";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			counselorList = new ArrayList<>();
			while (rs.next()) {
				Counselor c = new Counselor();
				c.setName(rs.getString("AD"));
				c.setSurname(rs.getString("SURNAME"));
				c.setEmail(rs.getString("EMAIL"));
				c.setUserId(rs.getString("USERID"));
				counselorList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counselorList;
	}
	
	public List<CounselorDetail> getCounselorStudentDetailList(String mentorId,String studentId) {
		List<CounselorDetail> counselorDetailList = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "select ulr.*,(SELECT NAME FROM USERS U WHERE U.USERID = ulr.userId  ) as STUDENT_NAME,(SELECT SURNAME FROM USERS U WHERE U.USERID = ulr.userId  ) as STUDENT_SURNAME,"
					+ "(SELECT email FROM USERS U WHERE U.USERID = ulr.userId  ) as STUDENT_EMAIL,(SELECT NAME FROM LESSONS LE WHERE LE.ID = ulr.LESSONID ) AS LESSON_NAME from user_lesson_rel ulr where ulr.userId = "
					+ "(SELECT studentId FROM student_mentor_rel WHERE mentorId = ?) and ulr.userId = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, mentorId);
			st.setString(2, studentId);
			ResultSet rs = st.executeQuery();
			counselorDetailList = new ArrayList<>();
			while (rs.next()) {
				CounselorDetail c = new CounselorDetail();
				c.setStudentName(rs.getString("STUDENT_NAME"));
				c.setStudentSurname(rs.getString("STUDENT_SURNAME"));
				c.setStudentEmail(rs.getString("STUDENT_EMAIL"));
				c.setUserId(rs.getString("USERID"));
				c.setLessonId(rs.getString("LESSONID"));
				c.setLessonName(rs.getString("LESSON_NAME"));
				c.setMidtermScore(rs.getBigDecimal("MIDTERM"));
				c.setFinalScore(rs.getBigDecimal("FINAL"));
				counselorDetailList.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counselorDetailList;
	}

	public Course getCourse(int id) {
		Course c = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM lessons WHERE ID = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			c = new Course();
			while (rs.next()) {
				c.setId(rs.getInt("ID"));
				c.setName(rs.getString("NAME"));
				c.setCode(rs.getString("COURSE_CODE"));
				c.setType(getCourseType(rs.getInt("LESSON_TYPE")));
				c.setResearchAssistant(rs.getString("ASSISTANT_NAME"));
				c.setResearchAssistantId(rs.getString("RESEARCH_ASSISTANT"));
				c.setTeacher(rs.getString("TEACHER_NAME"));
				c.setTeacherId(rs.getString("TEACHER"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public Course getCourseByCode(String code) {
		Course c = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT L.*,(SELECT NAME FROM USERS WHERE USERID = L.TEACHER) AS TEACHER_NAME, (SELECT NAME FROM USERS WHERE USERID = L.RESEARCH_ASSISTANT) AS ASSISTANT_NAME FROM lessons L  WHERE COURSE_CODE = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, code);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				c = new Course();
				c.setId(rs.getInt("ID"));
				c.setName(rs.getString("NAME"));
				c.setCode(rs.getString("COURSE_CODE"));
				c.setType(getCourseType(rs.getInt("LESSON_TYPE")));
				c.setResearchAssistant(rs.getString("ASSISTANT_NAME"));
				c.setResearchAssistantId(rs.getString("RESEARCH_ASSISTANT"));
				c.setTeacher(rs.getString("TEACHER_NAME"));
				c.setTeacherId(rs.getString("TEACHER"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	public Map<String, String> getTeachingStaffList(Boolean isResearchAssistant) {
		Map<String, String> staffList = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM users WHERE title ";
			if (isResearchAssistant) {
				query = query + " = 5";
			} else {
				query = query + "IN (2,3,4)";
			}
			PreparedStatement st = conn.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			staffList = new HashMap<>();
			while (rs.next()) {
				staffList.put(rs.getString("NAME") + " " + rs.getString("SURNAME"), rs.getString("USERID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staffList;
	}

	public Boolean createUser(User user, String userId, String password) {
		Boolean isUserCreated = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "INSERT INTO USERS(NAME,MIDNAME,SURNAME,TITLE,USERID,PASSWORD,GENDER,EMAIL,ADDRESS) VALUES(?,?,?,?,?,?,?,?,?)";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, user.getName());
			st.setString(2, user.getMidname());
			st.setString(3, user.getSurname());
			st.setString(4, user.getTitle());
			st.setString(5, userId);
			st.setString(6, password);
			st.setString(7, user.getGender());
			st.setString(8, user.getEmail());
			st.setString(9, user.getAddress());
			int rs = st.executeUpdate();
			if (rs == 1) {
				isUserCreated = Boolean.TRUE;
				if(user.getTitle().equals("6") || user.getTitle().equals("7") || user.getTitle().equals("8")){
					query = "INSERT INTO student_mentor_rel(STUDENTID,MENTORID) VALUES(?,?)";
					st = conn.prepareStatement(query);
					st.setString(1, userId);
					st.setString(2, user.getMentorId());
					rs = st.executeUpdate();
					if (rs == 1) {
						System.out.println(userId + " mentor assigned");
					}
				}
				System.out.println(userId + " User created");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserCreated;
	}

	public String changeUserPassword(String userId, String oldPassword, String newPassword) {
		String response = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT COUNT(*) FROM USERS WHERE USERID = ? AND PASSWORD = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, userId);
			st.setString(2, oldPassword);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				query = "UPDATE USERS SET PASSWORD = ? WHERE USERID = ?";
				st = conn.prepareStatement(query);
				st.setString(1, newPassword);
				st.setString(2, userId);
				int isUpdated = st.executeUpdate();
				if (isUpdated == 1) {
					response = "Þifre baþarýyla güncellendi.";
				} else {
					response = "Þifre güncellenemedi.";
				}
			} else {
				response = "Eski þifrenizi kontrol ediniz.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

	public Boolean modifyUser(User user) {
		Boolean isUserCreated = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "UPDATE USERS SET NAME = ?,MIDNAME = ?,SURNAME = ?,TITLE = ?,GENDER = ?,EMAIL = ?,ADDRESS = ? WHERE USERID=?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, user.getName());
			st.setString(2, user.getMidname());
			st.setString(3, user.getSurname());
			st.setString(4, user.getTitle());
			st.setString(5, user.getGender());
			st.setString(6, user.getEmail());
			st.setString(7, user.getAddress());
			st.setString(8, user.getId());
			int rs = st.executeUpdate();
			if (rs == 1) {
				isUserCreated = Boolean.TRUE;
				System.out.println(user.getId() + " User updated");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isUserCreated;
	}

	public User getUserInfo(String id) {
		User user = null;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "SELECT * FROM users U WHERE userId = ? ";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					String userType = rs.getString("TITLE");
					if (userType.equals(Enums.Users.ADMIN.getValue())) {
						user = new Admin();
					} else if (userType.equals(Enums.Users.PROF.getValue())
							|| userType.equals(Enums.Users.DOC.getValue())
							|| userType.equals(Enums.Users.Y_DOC.getValue())) {
						user = new TeachingStaff();
					} else if (userType.equals(Enums.Users.ARASTIRMA_GOREVLISI.getValue())) {
						user = new ResearchAssistant();
					} else if (userType.equals(Enums.Users.TEZLI_Y_L.getValue())
							|| userType.equals(Enums.Users.TEZSIZ_Y_L.getValue())
							|| userType.equals(Enums.Users.DOKTORA.getValue())) {
						user = new Student();
					}
					user.setId(rs.getString("USERID"));
					user.setAddress(rs.getString("ADDRESS"));
					user.setEmail(rs.getString("EMAIL"));
					user.setGender(rs.getString("GENDER"));
					user.setMidname(rs.getString("MIDNAME"));
					user.setName(rs.getString("NAME"));
					user.setSurname(rs.getString("SURNAME"));
					user.setTitle(userType);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public Boolean addCourse(Course course) {
		Boolean isCourseCreated = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "INSERT INTO lessons(NAME,LESSON_TYPE,COURSE_CODE,TEACHER,RESEARCH_ASSISTANT,SEATS_LEFT) VALUES(?,?,?,?,?,?)";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, course.getName());
			st.setInt(2, course.getType().getId());
			st.setString(3, course.getCode());
			st.setString(4, course.getTeacher());
			st.setString(5, course.getResearchAssistant());
			st.setInt(6, 20);
			int rs = st.executeUpdate();
			if (rs == 1) {
				isCourseCreated = Boolean.TRUE;
				System.out.println(course.getName() + " Course created");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isCourseCreated;
	}
	
	public Boolean modifyCourse(Course course) {
		Boolean isCourseCreated = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "UPDATE lessons SET NAME = ? ,LESSON_TYPE = ?,COURSE_CODE = ?,TEACHER = ?,RESEARCH_ASSISTANT = ? WHERE ID = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, course.getName());
			st.setInt(2, course.getType().getId());
			st.setString(3, course.getCode());
			st.setString(4, course.getTeacher());
			st.setString(5, course.getResearchAssistant());
			st.setInt(6, course.getId());
			int rs = st.executeUpdate();
			if (rs == 1) {
				isCourseCreated = Boolean.TRUE;
				System.out.println(course.getName() + " Course updated");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isCourseCreated;
	}

	public void deleteCourse(int courseID) {

	}

	public Boolean assignCourse(Course course, String userId) {
		Boolean isAssigned = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "UPDATE lessons SET SEATS_LEFT = SEATS_LEFT - 1 WHERE ID = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, course.getId());
			int rs = st.executeUpdate();
			if (rs == 1) {
				query = "INSERT INTO USER_LESSON_REL(USERID,LESSONID,MIDTERM,FINAL) VALUES(?,?,?,?)";
				st = conn.prepareStatement(query);
				st.setString(1, userId);
				st.setInt(2, course.getId());
				st.setBigDecimal(3, BigDecimal.ZERO);
				st.setBigDecimal(4, BigDecimal.ZERO);
				rs = st.executeUpdate();
				if (rs == 1) {
					isAssigned = Boolean.TRUE;
					System.out.println(course.getName() + " kayit olundu");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isAssigned;
	}

	public Boolean dropCourse(Course course, String userId) {
		Boolean isDropped = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "UPDATE lessons SET SEATS_LEFT = SEATS_LEFT + 1 WHERE ID = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setInt(1, course.getId());
			int rs = st.executeUpdate();
			if (rs == 1) {
				query = "DELETE FROM USER_LESSON_REL WHERE USERID = ? AND LESSONID = ?";
				st = conn.prepareStatement(query);
				st.setString(1, userId);
				st.setInt(2, course.getId());
				rs = st.executeUpdate();
				if (rs == 1) {
					isDropped = Boolean.TRUE;
					System.out.println(course.getName() + " kayit silindi");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isDropped;
	}

	public Boolean modifyUserScore(String studentId, int courseId, BigDecimal midterm, BigDecimal finalScore) {
		Boolean isModified = Boolean.FALSE;
		try {
			Connection conn = ConnectToDb.getInstance();
			String query = "UPDATE USER_LESSON_REL SET MIDTERM = ? ,FINAL = ? WHERE USERID = ? AND LESSONID= ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setBigDecimal(1, midterm);
			st.setBigDecimal(2, finalScore);
			st.setString(3, studentId);
			st.setInt(4, courseId);
			int rs = st.executeUpdate();
			if (rs == 1) {
				isModified = Boolean.TRUE;
				System.out.println(courseId + " kayit guncellendi");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isModified;
	}

	public void modifyCourse() {

	}

}
