package com.moptra.go4wealth.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.Role;
import com.moptra.go4wealth.bean.UserRole;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Role.class)
public abstract class Role_ {

	public static volatile SetAttribute<Role, UserRole> userRoles;
	public static volatile SingularAttribute<Role, Integer> roleId;
	public static volatile SingularAttribute<Role, String> roleName;

}

