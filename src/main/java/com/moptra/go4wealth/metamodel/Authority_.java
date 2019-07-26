package com.moptra.go4wealth.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.Authority;
import com.moptra.go4wealth.security.UserAuthority;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Authority.class)
public abstract class Authority_ {

	public static volatile SingularAttribute<Authority, Integer> authorityId;
	public static volatile SetAttribute<Authority, UserAuthority> userAuthorities;
	public static volatile SingularAttribute<Authority, String> authorityType;

}

