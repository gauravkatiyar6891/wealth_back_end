package com.moptra.go4wealth.metamodel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import com.moptra.go4wealth.bean.Commission;
import com.moptra.go4wealth.bean.User;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Commission.class)
public abstract class Commission_ {

	public static volatile SingularAttribute<Commission, Integer> commissionId;
	public static volatile SingularAttribute<Commission, Long> commissionPrice;
	public static volatile SingularAttribute<Commission, User> user;

}

