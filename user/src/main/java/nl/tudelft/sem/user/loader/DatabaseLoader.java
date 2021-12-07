package nl.tudelft.sem.user.loader;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements ApplicationRunner {
    /**
     * Callback used to run the bean.
     *
     * @param args incoming application arguments
     * @throws Exception on error
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Can create user entities for testing.
        // For more information read:
        // https://github.com/bephrem1/Spring-REST-API/blob/master/src/main/java/com/benyamephrem/core/DatabaseLoader.java.
    }
}
