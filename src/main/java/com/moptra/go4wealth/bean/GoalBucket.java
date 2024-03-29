package com.moptra.go4wealth.bean;
// Generated May 14, 2018 1:41:21 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * GoalBucket generated by hbm2java
 */
@Entity
@Table(name = "goal_bucket", catalog = "go4wealthdb")
public class GoalBucket implements java.io.Serializable {

	private Integer goalBucketId;
	private String goalBucketName;
	private String goalBucketCode;
	private Set<Goals> goalses = new HashSet<Goals>(0);

	public GoalBucket() {
	}

	public GoalBucket(String goalBucketName, String goalBucketCode, Set<Goals> goalses) {
		this.goalBucketName = goalBucketName;
		this.goalBucketCode = goalBucketCode;
		this.goalses = goalses;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "GOAL_BUCKET_ID", unique = true, nullable = false)
	public Integer getGoalBucketId() {
		return this.goalBucketId;
	}

	public void setGoalBucketId(Integer goalBucketId) {
		this.goalBucketId = goalBucketId;
	}

	@Column(name = "GOAL_BUCKET_NAME", length = 126)
	public String getGoalBucketName() {
		return this.goalBucketName;
	}

	public void setGoalBucketName(String goalBucketName) {
		this.goalBucketName = goalBucketName;
	}

	@Column(name = "GOAL_BUCKET_CODE", length = 5)
	public String getGoalBucketCode() {
		return this.goalBucketCode;
	}

	public void setGoalBucketCode(String goalBucketCode) {
		this.goalBucketCode = goalBucketCode;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goalBucket")
	public Set<Goals> getGoalses() {
		return this.goalses;
	}

	public void setGoalses(Set<Goals> goalses) {
		this.goalses = goalses;
	}

}
