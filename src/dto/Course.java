package dto;

import java.math.BigDecimal;

public class Course {
	private int id;
	private String name;
	private String code;
	private CourseType type;
	private String teacher;
	private String teacherId;
	private String researchAssistant;
	private String researchAssistantId;
	private int seatsLeft;
	private BigDecimal midterm;
	private BigDecimal finalScore;
	private String studentName;
	private String studentId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public CourseType getType() {
		return type;
	}

	public void setType(CourseType type) {
		this.type = type;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getResearchAssistant() {
		return researchAssistant;
	}

	public void setResearchAssistant(String researchAssistant) {
		this.researchAssistant = researchAssistant;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getResearchAssistantId() {
		return researchAssistantId;
	}

	public void setResearchAssistantId(String researchAssistantId) {
		this.researchAssistantId = researchAssistantId;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public BigDecimal getMidterm() {
		return midterm;
	}

	public void setMidterm(BigDecimal midterm) {
		this.midterm = midterm;
	}

	public BigDecimal getFinalScore() {
		return finalScore;
	}

	public void setFinalScore(BigDecimal finalScore) {
		this.finalScore = finalScore;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}
