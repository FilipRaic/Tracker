package test_container;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PostgresContainerExtension implements BeforeAllCallback {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) {
        synchronized (PostgresContainerExtension.class) {
            if (!started) {
                started = true;

                PostgresContainer.getInstance().start();
            }
        }
    }
}
