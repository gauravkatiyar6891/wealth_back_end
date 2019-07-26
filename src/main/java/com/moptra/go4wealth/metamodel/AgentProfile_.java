package com.moptra.go4wealth.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.AgentProfile;
import com.moptra.go4wealth.bean.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AgentProfile.class)
public abstract class AgentProfile_ {

	public static volatile SingularAttribute<AgentProfile, String> aadhaarNumber;
	public static volatile SingularAttribute<AgentProfile, Integer> field1;
	public static volatile SingularAttribute<AgentProfile, Integer> agentProfileId;
	public static volatile SingularAttribute<AgentProfile, Date> createdTimestamp;
	public static volatile SingularAttribute<AgentProfile, String> arnCode;
	public static volatile SingularAttribute<AgentProfile, String> panNumber;
	public static volatile SingularAttribute<AgentProfile, User> user;
	public static volatile SingularAttribute<AgentProfile, String> field2;
	public static volatile SingularAttribute<AgentProfile, Date> updatedDateTime;

}

