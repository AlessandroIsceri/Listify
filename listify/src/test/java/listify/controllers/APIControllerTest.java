package listify.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import listify.services.ListifyService;

//@ExtendWith(SpringExtension.class)

@SpringBootTest(classes = APIController.class)
@AutoConfigureMockMvc
class APIControllerTest {

	@MockBean
    private ListifyService listifyService;

	@MockBean
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mvc;
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Test
	void loginTest() throws Exception {
		
	    when(listifyService.login(anyString(), anyString())).thenReturn("aless");

		String url = "/API/login";
		
		String requestJson = "{"+
                "\"email\":\"aless@gmail.com\","+
                "\"password\":\"password\""+
        "}";
		
		mvc.perform(post(url)
					.contentType(MediaType.APPLICATION_JSON)
					.content(requestJson)
					.characterEncoding("UTF-8"))
					//.content(requestJson.getBytes()))
					.andDo(print())
					.andExpect(status().isOk());
	}
}