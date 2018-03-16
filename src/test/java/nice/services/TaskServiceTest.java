package nice.services;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

import nice.TodoApplication;
import nice.controllers.TasksController;
import nice.models.Task;
import nice.models.User;

/*
 * This Test file is used to test the services related to Task
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoApplication.class)
@ActiveProfiles("test")
public class TaskServiceTest {

    @InjectMocks
    TasksController controller;

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
    		mvc.perform(get("/tasks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        Task r1 = mockTask ("shouldCreateRetrieveDelete");
        byte[] r1Json = util.toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/tasks")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id")) because the 
                //id's will differ each time since records in db
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.desc", is(r1.getDesc())))
                .andExpect(jsonPath("$.status", is(r1.getStatus())))
                .andExpect(jsonPath("$.user.id",is((int)r1.getUser().getId())))
                .andReturn();
        long id = util.getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/tasks/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.desc", is(r1.getDesc())))
                .andExpect(jsonPath("$.status", is(r1.getStatus())))
                .andExpect(jsonPath("$.user.id",is((int)r1.getUser().getId())))
                .andExpect(jsonPath("$.user.userName",is(r1.getUser().getUserName())));
                

        //DELETE
        mvc.perform(delete("/tasks/" + id))
                .andExpect(status().isOk());

        //RETRIEVE should fail
        mvc.perform(get("/tasks/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //todo: you can test the 404 error body too.

    }

    @Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {
        Task r1 = mockTask("shouldCreateAndUpdate");
        byte[] r1Json = util.toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/tasks")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(r1.getId()))) we do not know which id would 
                //be created based on the number of records existing in db
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.desc", is(r1.getDesc())))
                .andExpect(jsonPath("$.status", is(r1.getStatus())))
                .andExpect(jsonPath("$.user.id",is((int)r1.getUser().getId())))
                .andReturn();
        long id = util.getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        Task r2 = mockTask("shouldCreateAndUpdate2");
        r2.setId(id);
        byte[] r2Json = util.toJson(r2);

        //UPDATE
        result = mvc.perform(put("/tasks/" + id)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //RETRIEVE updated
        mvc.perform(get("/tasks/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(r2.getName())))
                .andExpect(jsonPath("$.desc", is(r2.getDesc())))
                .andExpect(jsonPath("$.status", is(r2.getStatus())))
                .andExpect(jsonPath("$.user.id",is((int)r2.getUser().getId())))
                .andExpect(jsonPath("$.user.userName",is(r2.getUser().getUserName())));

        //DELETE
        mvc.perform(delete("/tasks/" + id))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getListWithSpecificationStatusNotStarted() throws Exception {
        mvc.perform(get("/tasks?search=status:not started").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void getListWithSpecificationStatuCompleted() throws Exception {
        mvc.perform(get("/tasks?search=status:completed").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void getListWithSpecificationStatusInProgress() throws Exception {
        mvc.perform(get("/tasks?search=status:in progress").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    /*
    ******************************
     */

    private Task mockTask(String prefix) {
        User u = new User();
        u.setId(1);// use an existing user one's
        u.setUserName("John Deo");
        Task r = new Task();
        r.setDesc(prefix + " test desc");
        r.setName(prefix);
        r.setStatus("Not Started");
        r.setUser(u);
        return r;
    }
   
}
