package com.jonbake.report.util;

import com.jonbake.report.configuration.Configuration;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * Poolable data source for report service.
 *
 * @author jonmbake
 */
public class ReportDatasource {
    private final Optional<DataSource> dataSource;
    /**
     * Construct with configuration.
     *
     * @param configuration - app configuration
     */
    public ReportDatasource (final Configuration configuration) {
        Optional<String> optConnectionURI = configuration.getJDBCConnectionString();
        if (!optConnectionURI.isPresent()) {
            dataSource = Optional.empty();
            return;
        }
        String connectionURI = optConnectionURI.get();
        ConnectionFactory cf = new DriverManagerConnectionFactory(connectionURI, null);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(cf, new GenericObjectPool(),
                        null, null, true, false);
        GenericObjectPool connectionPool = new GenericObjectPool(poolableConnectionFactory);
        dataSource = Optional.of(new PoolingDataSource(connectionPool));
    }
    /**
     * Get the database connection to run the report.
     *
     * @return database connection
     * @throws SQLException -
     */
    public final Optional<Connection> getConnection () throws SQLException {
        if (!dataSource.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(dataSource.get().getConnection());
    }
}
