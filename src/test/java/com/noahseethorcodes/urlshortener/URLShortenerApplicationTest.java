package com.noahseethorcodes.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noahseethorcodes.urlshortener.errors.ErrorMessage;
import com.noahseethorcodes.urlshortener.errors.KeyNotFoundException;
import com.noahseethorcodes.urlshortener.models.URLobj;
import com.noahseethorcodes.urlshortener.repositories.URLRepository;
import com.noahseethorcodes.urlshortener.services.URLService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
public class URLShortenerApplicationTest {
    @Value("${example_valid_url}")
    String example_valid_url;
    @Value("${example_invalid_url}")
    String example_invalid_url;
    @Value("${invalid_key}")
    String invalid_key;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private URLService service;
    @Autowired
    private URLRepository repository;

    @Test
    void contextLoads() {

    }

    @Test
    public void whenPostValidURL_thenCreateURL() throws Exception {
        String app_path = "/addURL";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);

        mockMvc.perform(post(app_path).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(test_url)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.targetURL", is(example_valid_url)));

        Long new_id = 1L;
        URLobj added_url = repository.findById(new_id)
                .orElseThrow(() -> new RuntimeException("unsuccessful creation"));
        assertEquals(example_valid_url, added_url.getTargetURL());
    }

    @Test
    public void whenPostInvalidURL_thenThrowBadRequest() throws Exception {
        String app_path = "/addURL";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_invalid_url);

        mockMvc.perform(post(app_path).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(test_url)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid URL provided")));
    }

    @Test
    public void whenPeekValidKey_thenReturnTargetURL() throws Exception {
        String app_path = "/peek_{url_key}";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);

        String expected_response;
        expected_response = String.format("URL behind key {%s} is '%s'",
                test_url.getKey(),
                test_url.getTargetURL());

        mockMvc.perform(get(app_path, test_url.getKey()))
                .andExpect(status().isOk())
                .andExpect(content().string(expected_response));
    }

    private void whenInvalidKey_thenThrowKeyNotFound(String app_path) throws Exception {
        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);

        KeyNotFoundException ex = new KeyNotFoundException(invalid_key);
        ErrorMessage expected_error_response = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex);

        mockMvc.perform(get(app_path, invalid_key))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(expected_error_response.getMessage())));
    }

    private void whenInactiveKey_thenThrowKeyNotFound(String app_path) throws Exception {
        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);
        test_url.setActive(false);
        repository.save(test_url);

        KeyNotFoundException ex = new KeyNotFoundException(test_url.getKey());
        ErrorMessage expected_error_response = new ErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex);

        mockMvc.perform(get(app_path, test_url.getKey()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(expected_error_response.getMessage())));
    }

    @Test
    public void whenPeekInvalidKey_thenThrowNotFound() throws Exception {
        String app_path = "/peek_{url_key}";
        whenInvalidKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenPeekInactiveKey_thenThrowKeyNotFound() throws Exception {
        String app_path = "/peek_{url_key}";
        whenInactiveKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenGotoValidKey_thenForwardToTargetURL() throws Exception {
        String app_path = "/goto_{url_key}";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);

        mockMvc.perform(get(app_path, test_url.getKey()))
                .andExpect(status().isMovedPermanently())
                .andExpect(redirectedUrl(test_url.getTargetURL()));
    }

    @Test
    public void whenGotoInvalidKey_thenThrowKeyNotFound() throws Exception {
        String app_path = "/goto_{url_key}";
        whenInvalidKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenGotoInactiveKey_thenThrowKeyNotFound() throws Exception {
        String app_path = "/goto_{url_key}";
        whenInactiveKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenGetAdminKey_thenReadURLAdminInfo() throws Exception {
        String app_path = "/admin/{admin_key}";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);

        mockMvc.perform(get(app_path, test_url.getAdminKey()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(test_url)));
    }

    @Test
    public void whenGetInvalidAdminKey_thenThrowKeyNotFound() throws Exception {
        String app_path = "/admin/{admin_key}";
        whenInvalidKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenGetInactiveAdminKey_thenThrowKeyNotFound() throws Exception {
        String app_path = "/admin/{admin_key}";
        whenInactiveKey_thenThrowKeyNotFound(app_path);
    }

    @Test
    public void whenDelByAdminKey_thenURLNotActive() throws Exception {
        String app_path = "/admin/{admin_key}";

        URLobj test_url = new URLobj();
        test_url.setTargetURL(example_valid_url);
        test_url = service.addURL(test_url);
        String admin_key = test_url.getAdminKey();

        String expected_response;
        expected_response = String.format("Successfully deleted shortened URl for '%s'"
                , test_url.getTargetURL());

        mockMvc.perform(delete(app_path, admin_key))
                .andExpect(status().isOk())
                .andExpect(content().string(expected_response));

        URLobj post_del_url = repository.findByAdminKey(admin_key)
                .orElseThrow(() -> new KeyNotFoundException(admin_key));

        assertEquals(false, post_del_url.isActive());
    }
}
