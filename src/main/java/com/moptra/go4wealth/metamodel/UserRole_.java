package com.moptra.go4wealth.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.Role;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserRole;
import com.moptra.go4wealth.bean.UserRoleId;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserRole.class)
public abstract class UserRole_ {

	public static volatile SingularAttribute<UserRole, Role> role;
	public static volatile SingularAttribute<UserRole, UserRoleId> id;
	public static volatile SingularAttribute<UserRole, User> user;

}

