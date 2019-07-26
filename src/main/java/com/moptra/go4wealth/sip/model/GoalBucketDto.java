package com.moptra.go4wealth.sip.model;

public class GoalBucketDto {

	private Integer goalBucketId;
	private String goalBucketName;
	
	public GoalBucketDto() {
		super();
	}
	
	public GoalBucketDto(Integer goalBucketId, String goalBucketName) {
		this.goalBucketId = goalBucketId;
		this.goalBucketName = goalBucketName;
	}
	
	public Integer getGoalBucketId() {
		return goalBucketId;
	}
	public void setGoalBucketId(Integer goalBucketId) {
		this.goalBucketId = goalBucketId;
	}
	public String getGoalBucketName() {
		return goalBucketName;
	}
	public void setGoalBucketName(String goalBucketName) {
		this.goalBucketName = goalBucketName;
	}
	
	
}
