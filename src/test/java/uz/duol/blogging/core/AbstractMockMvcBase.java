package uz.duol.blogging.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(initializers = {DBContainerInitializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
public class AbstractMockMvcBase {

  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired private ObjectMapper objectMapper;
  protected MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }
}
