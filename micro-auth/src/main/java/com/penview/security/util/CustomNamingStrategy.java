package com.penview.security.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
		return logicalName;
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
		return logicalName;
	}

	@Override
	public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
		return logicalName;
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
		return logicalName;
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment jdbcEnvironment) {
		return logicalName;
	}

}
