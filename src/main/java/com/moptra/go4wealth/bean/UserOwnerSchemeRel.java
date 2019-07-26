package com.moptra.go4wealth.bean;
// Generated May 14, 2018 1:41:21 PM by Hibernate Tools 5.1.0.Alpha1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * UserOwnerSchemeRel generated by hbm2java
 */
@Entity
@Table(name = "user_owner_scheme_rel", catalog = "go4wealthdb")
public class UserOwnerSchemeRel implements java.io.Serializable {

	private UserOwnerSchemeRelId id;

	public UserOwnerSchemeRel() {
	}

	public UserOwnerSchemeRel(UserOwnerSchemeRelId id) {
		this.id = id;
	}

	@EmbeddedId

	@AttributeOverrides({ @AttributeOverride(name = "userId", column = @Column(name = "USER_ID", nullable = false)),
			@AttributeOverride(name = "ownerId", column = @Column(name = "OWNER_ID", nullable = false)),
			@AttributeOverride(name = "scheme", column = @Column(name = "SCHEME", length = 126)),
			@AttributeOverride(name = "folioNo", column = @Column(name = "FOLIO_NO", length = 126)) })
	public UserOwnerSchemeRelId getId() {
		return this.id;
	}

	public void setId(UserOwnerSchemeRelId id) {
		this.id = id;
	}

}
