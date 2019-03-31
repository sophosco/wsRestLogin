package com.sophos.poc.login.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SuppressWarnings("deprecation")
@Configuration
@EnableCassandraRepositories("com.sophos.poc.login.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

	private static final String USERNAME = System.getenv("POC_CASSANDRA_USERNAME");
	private static final String PASSWORD = System.getenv("POC_CASSANDRA_PASSWORD");
	private static final String KEYSPACE_NAME = System.getenv("POC_CASSANDRA_KEYSPACE");
	private static final String CONTACT_POINTS = System.getenv("POC_CASSANDRA_HOST");
	private static final int PORT = Integer.parseInt(System.getenv("POC_CASSANDRA_PORT"));

		@Bean
	    public CassandraClusterFactoryBean cluster() {
	        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
	        cluster.setUsername(USERNAME);
	        cluster.setPassword(PASSWORD);
	        cluster.setContactPoints(CONTACT_POINTS);
	        cluster.setPort(PORT);
	        return cluster;
	    }
	    @Bean
	    public CassandraMappingContext mappingContext() {
	        return new BasicCassandraMappingContext();
	    }
	    
	    @Bean
	    public CassandraConverter converter() {
	        return new MappingCassandraConverter(mappingContext());
	    }
	    
	    @Bean
	    public CassandraSessionFactoryBean session() {
	        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
	        session.setCluster(cluster().getObject());
	        session.setKeyspaceName(KEYSPACE_NAME);
	        session.setConverter(converter());
	        session.setSchemaAction(SchemaAction.NONE);
	        return session;
	    }
	    
	    
		@Override
		protected String getKeyspaceName() {
			return KEYSPACE_NAME;
		}

}