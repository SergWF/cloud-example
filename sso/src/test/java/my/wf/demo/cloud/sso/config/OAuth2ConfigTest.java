package my.wf.demo.cloud.sso.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class OAuth2ConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<OAuth2AccessToken> jsonTokenData;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        // Possibly configure the mapper
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void shouldReturnToken() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "user1");
        params.add("password", "123");

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("mobile", "123"))
                .accept("application/json;charset=UTF-8"))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        OAuth2AccessToken tokenObject = this.jsonTokenData.parse(resultString).getObject();
        SoftAssertions.assertSoftly($-> {
            $.assertThat(tokenObject).isNotNull();
            $.assertThat(tokenObject.isExpired()).isFalse();
            $.assertThat(tokenObject.getValue()).isNotEmpty();
        });
    }

    @Test
    public void shouldCheckToken() throws Exception {
        OAuth2AccessToken oAuthToken = getOAuthToken();
        String tokenValue = oAuthToken.getValue();

        ResultActions result
                = mockMvc.perform(get("/oauth/check_token")
                .param("token", tokenValue)
                .with(httpBasic("mobile", "123"))
                .accept("application/json;charset=UTF-8"))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();

        Map map = new ObjectMapper().readValue(resultString, Map.class);
        SoftAssertions.assertSoftly($-> {
            $.assertThat(map.get("user_name")).isEqualTo("user1");
            $.assertThat(map.get("client_id")).isEqualTo("mobile");
        });
    }

    private OAuth2AccessToken getOAuthToken() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "user1");
        params.add("password", "123");

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic("mobile", "123"))
                .accept("application/json;charset=UTF-8"))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();
        return this.jsonTokenData.parse(resultString).getObject();
    }
}