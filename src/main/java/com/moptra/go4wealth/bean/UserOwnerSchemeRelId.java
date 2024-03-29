package com.moptra.go4wealth.bean;
// Generated May 14, 2018 1:41:21 PM by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * UserOwnerSchemeRelId generated by hbm2java
 */
@Embeddable
public class UserOwnerSchemeRelId implements java.io.Serializable {

	private int userId;
	private int ownerId;
	private String scheme;
	private String folioNo;

	public UserOwnerSchemeRelId() {
	}

	public UserOwnerSchemeRelId(int userId, int ownerId) {
		this.userId = userId;
		this.ownerId = ownerId;
	}

	public UserOwnerSchemeRelId(int userId, int ownerId, String scheme, String folioNo) {
		this.userId = userId;
		this.ownerId = ownerId;
		this.scheme = scheme;
		this.folioNo = folioNo;
	}

	@Column(name = "USER_ID", nullable = false)
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Column(name = "OWNER_ID", nullable = false)
	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "SCHEME", length = 126)
	public String getScheme() {
		return this.scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@Column(name = "FOLIO_NO", length = 126)
	public String getFolioNo() {
		return this.folioNo;
	}

	public void setFolioNo(String folioNo) {
		this.folioNo = folioNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserOwnerSchemeRelId))
			return false;
		UserOwnerSchemeRelId castOther = (UserOwnerSchemeRelId) other;

		return (this.getUserId() == castOther.getUserId()) && (this.getOwnerId() == castOther.getOwnerId())
				&& ((this.getScheme() == castOther.getScheme()) || (this.getScheme() != null
						&& castOther.getScheme() != null && this.getScheme().equals(castOther.getScheme())))
				&& ((this.getFolioNo() == castOther.getFolioNo()) || (this.getFolioNo() != null
						&& castOther.getFolioNo() != null && this.getFolioNo().equals(castOther.getFolioNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getUserId();
		result = 37 * result + this.getOwnerId();
		result = 37 * result + (getScheme() == null ? 0 : this.getScheme().hashCode());
		result = 37 * result + (getFolioNo() == null ? 0 : this.getFolioNo().hashCode());
		return result;
	}

}
