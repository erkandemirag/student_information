package dto;

import java.math.BigDecimal;

public class CounselorDetail {
	private String userId;
	private String studentName;
	private String studentSurname;
	private String studentEmail;
	private String lessonId;
	private String lessonName;
	private BigDecimal midtermScore;
	private BigDecimal finalScore;

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentSurname() {
		return studentSurname;
	}

	public void setStudentSurname(String studentSurname) {
		this.studentSurname = studentSurname;
	}

	public String getStudentEmail() {
		return studentEmail;
	}

	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public BigDecimal getMidtermScore() {
		return midtermScore;
	}

	public void setMidtermScore(BigDecimal midtermScore) {
		this.midtermScore = midtermScore;
	}

	public BigDecimal getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(BigDecimal finalScore) {
		this.finalScore = finalScore;
	}
}
