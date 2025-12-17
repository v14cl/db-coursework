package com.librarymanager.model;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@FilterDef(name = "softDeleteFilter", defaultCondition = "deleted = false", parameters = @ParamDef(name = "deleted", type = Boolean.class))
public class HibernateFilters {}
