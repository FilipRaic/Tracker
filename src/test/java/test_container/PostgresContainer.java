package test_container;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

    private static final String IMAGE = "postgres:17.5-alpine";

    private static final PostgresContainer INSTANCE = new PostgresContainer(IMAGE);

    private PostgresContainer(String dockerImageName) {
        super(dockerImageName);
    }

    public static PostgresContainer getInstance() {
        return INSTANCE;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DATASOURCE_URL", INSTANCE.getJdbcUrl());
        System.setProperty("DB_USERNAME", INSTANCE.getUsername());
        System.setProperty("DB_PASSWORD", INSTANCE.getPassword());
    }

    @Override
    public void stop() {
        // Do nothing, JVM handles shut down
    }
}
