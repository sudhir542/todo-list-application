package nice.services;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import nice.TodoApplication;
import nice.controllers.UsersController;
import nice.models.User;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    UsersController controller;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;
    
    UtilServiceForTests util = new UtilServiceForTests();

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    //@Test
    public void shouldHaveSomeDB() throws Exception {//based on
        //data.sql that we have injected along with this 
    		mvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        User r1 = mockUser ("shouldCreateRetrieveDelete");
        byte[] r1Json = util.toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/users")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", is(r1.getUserName())))
                .andReturn();
        long id = util.getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/users/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.userName", is(r1.getUserName())));
                
       
        //DELETE
        mvc.perform(delete("/users/" + id))
                .andExpect(status().isOk());

        //RETRIEVE should fail
        mvc.perform(get("/users/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //todo: you can test the 404 error body too.

    }
    
    /*
     ******************************
      */

     private User mockUser(String prefix) {
         User u = new User();
         u.setUserName(prefix);
         return u;
     }
    
}