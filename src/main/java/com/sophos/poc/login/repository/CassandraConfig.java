package com.sophos.poc.login.repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SuppressWarnings("deprecation")
@Configuration
@EnableCassandraRepositories("com.sophos.poc.login.repository")
public class CassandraConfig extends AbstractCassandraConfiguration{

    private static final String KEYSPACE_NAME = System.getenv("POC_CASSANDRA_KEYSPACE");
    private static final String CONTACT_POINTS = System.getenv("POC_CASSANDRA_HOST");
    private static final int PORT = Integer.parseInt(System.getenv("POC_CASSANDRA_PORT"));

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE_NAME;
    }

    @Override
    protected String getContactPoints() {
        return CONTACT_POINTS;
    }

    @Override
    protected int getPort() {
        return PORT;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.NONE;
    }

	@Bean
    public CassandraOperations operations() throws Exception {
        return new CassandraTemplate(session().getObject(), new MappingCassandraConverter(new BasicCassandraMappingContext()));
    }
}