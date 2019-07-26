package com.moptra.go4wealth.metamodel;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.AgentProfile;
import com.moptra.go4wealth.bean.Commission;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.security.UserAuthority;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	//public static volatile SingularAttribute<User, String> owner;
	public static volatile SingularAttribute<User, AgentProfile> agentProfile;
	public static volatile SingularAttribute<User, String> lastName;
	//public static volatile SetAttribute<User, Address> addresses;
	public static volatile SingularAttribute<User, String> gender;
	public static volatile SingularAttribute<User, Integer> mobileNumber;
	public static volatile SingularAttribute<User, Date> createdTimestamp;
	public static volatile SingularAttribute<User, Date> dateOfBirth;
	public static volatile SingularAttribute<User, String> registerType;
	public static volatile SingularAttribute<User, Integer> userId;
	//public static volatile SingularAttribute<User, UserProfile> userProfile;
	public static volatile SingularAttribute<User, Date> updatedDateTime;
	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SetAttribute<User, UserRole> userRoles;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Integer> field1;
	public static volatile SetAttribute<User, Commission> commissions;
	public static volatile SetAttribute<User, UserAuthority> userAuthorities;
	public static volatile SingularAttribute<User, String> field3;
	public static volatile SingularAttribute<User, String> field2;
	public static volatile SingularAttribute<User, String> username;
	public static volatile SingularAttribute<User, String> field4;

}

