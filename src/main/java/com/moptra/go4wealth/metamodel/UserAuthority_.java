package com.moptra.go4wealth.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.Authority;
import com.moptra.go4wealth.bean.User;
import com.moptra.go4wealth.bean.UserAuthorityId;
import com.moptra.go4wealth.security.UserAuthority;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserAuthority.class)
public abstract class UserAuthority_ {

	public static volatile SingularAttribute<UserAuthority, Authority> authority;
	public static volatile SingularAttribute<UserAuthority, UserAuthorityId> id;
	public static volatile SingularAttribute<UserAuthority, User> user;

}

