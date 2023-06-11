package com.carara.nursenow.config;

import com.carara.nursenow.NursenowApplication;
import com.carara.nursenow.repos.BookingRepository;
import com.carara.nursenow.repos.CityRepository;
import com.carara.nursenow.repos.ExperienceRepository;
import com.carara.nursenow.repos.ServiceRepository;
import com.carara.nursenow.repos.UsersRepository;
import java.nio.charset.Charset;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.PostgreSQLContainer;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
        classes = NursenowApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/usersData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @ServiceConnection
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15.3");
    private static MockHttpSession authenticatedSession = null;

    static {
        postgreSQLContainer.withReuse(true)
                .start();
    }

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    public CityRepository cityRepository;

    @Autowired
    public ExperienceRepository experienceRepository;

    @Autowired
    public ServiceRepository serviceRepository;

    @Autowired
    public BookingRepository bookingRepository;

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), Charset.forName("UTF-8"));
    }

    public MockHttpSession authenticatedSession() throws Exception {
        if (authenticatedSession == null) {
            final MvcResult mvcResult = mockMvc.perform(
                    SecurityMockMvcRequestBuilders.formLogin().user("email", "bootify").password("password", "Bootify!")).andReturn();
            authenticatedSession = ((MockHttpSession)mvcResult.getRequest().getSession(false));
        }
        return authenticatedSession;
    }

}
