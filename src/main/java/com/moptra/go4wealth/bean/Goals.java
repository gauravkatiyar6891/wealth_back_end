package com.moptra.go4wealth.bean;
// Generated May 14, 2018 1:41:21 PM by Hibernate Tools 5.1.0.Alpha1

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Goals generated by hbm2java
 */
@Entity
@Table(name = "goals", catalog = "go4wealthdb")
public class Goals implements java.io.Serializable {

	private Integer goalId;
	private GoalBucket goalBucket;
	private String goalName;
	private Integer showToProfileType;
	private BigDecimal costOfGoal;
	private String goalIcon;
	private String goalType;
	private Integer duration;
	private String goalKey;
	private String status;

	public Goals() {
	}

	public Goals(GoalBucket goalBucket, BigDecimal costOfGoal) {
		this.goalBucket = goalBucket;
		this.costOfGoal = costOfGoal;
	}

	public Goals(GoalBucket goalBucket, String goalName, Integer showToProfileType, BigDecimal costOfGoal,
			String goalIcon, String goalKey, String status) {
		this.goalBucket = goalBucket;
		this.goalName = goalName;
		this.showToProfileType = showToProfileType;
		this.costOfGoal = costOfGoal;
		this.goalIcon = goalIcon;
		this.goalKey = goalKey;
		this.status = status;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "GOAL_ID", unique = true, nullable = false)
	public Integer getGoalId() {
		return this.goalId;
	}

	public void setGoalId(Integer goalId) {
		this.goalId = goalId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOAL_BUCKET_ID", nullable = false)
	public GoalBucket getGoalBucket() {
		return this.goalBucket;
	}

	public void setGoalBucket(GoalBucket goalBucket) {
		this.goalBucket = goalBucket;
	}

	@Column(name = "GOAL_NAME", length = 126)
	public String getGoalName() {
		return this.goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	@Column(name = "SHOW_TO_PROFILE_TYPE", length = 10)
	public Integer getShowToProfileType() {
		return this.showToProfileType;
	}

	public void setShowToProfileType(Integer showToProfileType) {
		this.showToProfileType = showToProfileType;
	}

	@Column(name = "COST_OF_GOAL", nullable = false, precision = 20, scale = 5)
	public BigDecimal getCostOfGoal() {
		return this.costOfGoal;
	}

	public void setCostOfGoal(BigDecimal costOfGoal) {
		this.costOfGoal = costOfGoal;
	}

	@Column(name = "GOAL_ICON", length = 126)
	public String getGoalIcon() {
		return goalIcon;
	}

	public void setGoalIcon(String goalIcon) {
		this.goalIcon = goalIcon;
	}

	@Column(name = "GOAL_TYPE",nullable = false, columnDefinition="char(3)")
	public String getGoalType() {
		return goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}

	@Column(name = "DURATION")
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Column(name = "GOAL_KEY", length=50)
	public String getGoalKey() {
		return goalKey;
	}

	public void setGoalKey(String goalKey) {
		this.goalKey = goalKey;
	}

	@Column(name = "STATUS", length=512)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}