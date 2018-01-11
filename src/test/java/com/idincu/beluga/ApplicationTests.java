package com.example.auth;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "spring.config.location=classpath:/google-oauth.yml")
public class ApplicationTests {

    @LocalServerPort
    private int port;

	@Before
	public void setUp() throws Exception {
	    RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
	}

	@Test
	public void call_index_html_with_root_path() throws Exception {
		given()
				.when()
					.get("/")
				.then()
					.statusCode(200)
					.contentType("text/html")
					.body(containsString("Google Login"));
	}

	@Test
    public void when_access_login_check_if_google_loginform_appear() throws Exception {
	    given()
                .when()
                    .redirects().follow(false)
                    .get("/login")
                .then()
                    .statusCode(302)
                    .header("Location", containsString("https://accounts.google.com/o/oauth2/auth"));
    }

}
