package nice.services;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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
import nice.controllers.TodoListController;
import nice.models.TodoList;
import nice.models.Task;

/*
 * This Test file is used to test the services related to TodoList
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoApplication.class)
@ActiveProfiles("test")
public class TodoListServiceTest {

    @InjectMocks
    TodoListController controller;

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
    		mvc.perform(get("/todolists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        TodoList r1 = mockTodoList ("shouldCreateRetrieveDelete");
        byte[] r1Json = util.toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/todolists")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id")) because the 
                //id's will differ each time since records in db
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.tasks[0].id",is((int)r1.getTasks().get(0).getId())))
                .andReturn();
        long id = util.getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/todolists/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.tasks[0].id",is((int)r1.getTasks().get(0).getId())));
       
        //DELETE
        mvc.perform(delete("/todolists/" + id))
                .andExpect(status().isOk());

        //RETRIEVE should fail
        mvc.perform(get("/todolists/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //todo: you can test the 404 error body too.

    }

    @Test
    public void shouldCreateAndDeleteAndRetreive() throws Exception {
        TodoList r1 = mockTodoList("shouldCreateAndDelete");
        byte[] r1Json = util.toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/todolists")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.id", is(r1.getId()))) we do not know which id would 
                //be created based on the number of records existing in db
                .andExpect(jsonPath("$.name", is(r1.getName())))
                .andExpect(jsonPath("$.tasks[0].id",is((int)r1.getTasks().get(0).getId())))
                .andReturn();
        long id = util.getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //DELETE
        mvc.perform(delete("/todolists/" + id))
                .andExpect(status().isOk());
        
        //RETRIEVE should fail
        mvc.perform(get("/todolists/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());


    }


    /*
    ******************************
     */

    private TodoList mockTodoList(String prefix) {
    		List<Task> tasks = new ArrayList<Task>();
        Task t = new Task();
        t.setId(1);// use an existing task to add
        tasks.add(t);
        TodoList todoList = new TodoList();
        todoList.setName(prefix);
        todoList.setTasks(tasks);
        return todoList;
    }
   
}

