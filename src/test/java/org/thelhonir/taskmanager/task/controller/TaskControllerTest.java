package org.thelhonir.taskmanager.task.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thelhonir.taskmanager.status.Status;
import org.thelhonir.taskmanager.task.controller.dto.TaskDTO;
import org.thelhonir.taskmanager.task.model.Task;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @BeforeEach
        private void cleanup() throws Exception {
                this.mockMvc.perform(MockMvcRequestBuilders.delete(TaskController.ENDPOINT));
        }

        @Test
        public void newTaskshouldReturn200WithDescription() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task)))).andExpect(status().isOk());

        }

        @Test
        public void newTaskshouldReturn400WithoutDescription() throws Exception {
                TaskDTO task = TaskDTO.builder().build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))))
                                .andExpect(status().isBadRequest());

        }

        @Test
        public void updateTaskshouldReturn200() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task)))).andReturn();

                Task taskCreated = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<Task>() {
                });

                TaskDTO updatedTask = TaskDTO.builder().status(Status.DONE).build();

                this.mockMvc.perform(MockMvcRequestBuilders
                                .put(TaskController.ENDPOINT_SINGLE + "/" + taskCreated.getId())
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((updatedTask))))
                                .andExpect(status().isOk());

        }

        @Test
        public void updateTaskshouldReturn404WhenIDnotExists() throws Exception {
                // Not testing wrong ID's since Spring takes care of bad requests.

                TaskDTO task = TaskDTO.builder().build();

                this.mockMvc.perform(MockMvcRequestBuilders.put(TaskController.ENDPOINT_SINGLE + "/2020202")
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))))
                                .andExpect(status().isNotFound());

        }

        @Test
        public void getTaskshouldReturn200() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))));

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))));

                MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(TaskController.ENDPOINT))
                                .andExpect(status().isOk()).andReturn();

                List<Task> resultList = mapFromJson(result.getResponse().getContentAsString(),
                                new TypeReference<List<Task>>() {
                                });

                assertTrue(resultList.size() == 2);
        }

        @Test
        public void getTaskshouldReturn200onFilter() throws Exception {
                TaskDTO todoTask = TaskDTO.builder().description("New test task").status(Status.TODO).build();
                TaskDTO ipTask = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((todoTask))));

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((ipTask))));

                MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(TaskController.ENDPOINT + "/TODO"))
                                .andExpect(status().isOk()).andReturn();

                List<Task> resultList = mapFromJson(result.getResponse().getContentAsString(),
                                new TypeReference<List<Task>>() {
                                });

                assertTrue(resultList.size() == 1);
        }

        @Test
        public void getTaskWithIDshouldReturn200() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                MvcResult newTaskResult = this.mockMvc.perform(MockMvcRequestBuilders
                                .post(TaskController.ENDPOINT_SINGLE).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task)))).andReturn();

                Task taskCreated = mapFromJson(newTaskResult.getResponse().getContentAsString(),
                                new TypeReference<Task>() {
                                });

                MvcResult getTaskResult = this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .get(TaskController.ENDPOINT_SINGLE + "/" + taskCreated.getId()))
                                .andExpect(status().isOk()).andReturn();

                Task taskRetrieved = mapFromJson(getTaskResult.getResponse().getContentAsString(),
                                new TypeReference<Task>() {
                                });

                assertEquals(taskRetrieved, taskCreated);
        }

        @Test
        public void getTaskshouldReturn404WhenIDnotExists() throws Exception {
                // Not testing wrong ID's since Spring takes care of bad requests.

                this.mockMvc.perform(MockMvcRequestBuilders.get(TaskController.ENDPOINT_SINGLE + "/2020202"))
                                .andExpect(status().isNotFound());

        }

        @Test
        public void deleteTaskShouldReturn200() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))));

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))));

                MvcResult deleteResult = this.mockMvc.perform(MockMvcRequestBuilders.delete(TaskController.ENDPOINT))
                                .andExpect(status().isOk()).andReturn();

                assertTrue(deleteResult.getResponse().getContentAsString().equals("List deleted"));

                MvcResult afterDeleteResult = this.mockMvc.perform(MockMvcRequestBuilders.get(TaskController.ENDPOINT))
                                .andExpect(status().isOk()).andReturn();

                List<Task> afterDeleteResultList = mapFromJson(afterDeleteResult.getResponse().getContentAsString(),
                                new TypeReference<List<Task>>() {
                                });

                assertTrue(afterDeleteResultList.isEmpty());

        }

        @Test
        public void deleteTaskWithIdShouldReturn200() throws Exception {
                TaskDTO task = TaskDTO.builder().description("New test task").status(Status.IN_PROGRESS).build();

                this.mockMvc.perform(MockMvcRequestBuilders.post(TaskController.ENDPOINT_SINGLE)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task))));

                MvcResult newTaskResult = this.mockMvc.perform(MockMvcRequestBuilders
                                .post(TaskController.ENDPOINT_SINGLE).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString((task)))).andReturn();

                Task taskCreated = mapFromJson(newTaskResult.getResponse().getContentAsString(),
                                new TypeReference<Task>() {
                                });

                MvcResult deleteResult = this.mockMvc
                                .perform(MockMvcRequestBuilders
                                                .delete(TaskController.ENDPOINT_SINGLE + "/" + taskCreated.getId()))
                                .andExpect(status().isOk()).andReturn();

                assertTrue(deleteResult.getResponse().getContentAsString().equals(String.valueOf(taskCreated.getId())));

                MvcResult afterDeleteResult = this.mockMvc.perform(MockMvcRequestBuilders.get(TaskController.ENDPOINT))
                                .andExpect(status().isOk()).andReturn();

                List<Task> afterDeleteResultList = mapFromJson(afterDeleteResult.getResponse().getContentAsString(),
                                new TypeReference<List<Task>>() {
                                });

                assertTrue(afterDeleteResultList.size() == 1);

        }

        @Test
        public void deleteTaskshouldReturn404WhenIDnotExists() throws Exception {
                // Not testing wrong ID's since Spring takes care of bad requests.

                this.mockMvc.perform(MockMvcRequestBuilders.delete(TaskController.ENDPOINT_SINGLE + "/2020202"))
                                .andExpect(status().isNotFound());

        }

        private <T> T mapFromJson(String jsonString, TypeReference<T> type)
                        throws JsonMappingException, JsonProcessingException {
                return new ObjectMapper().readValue(jsonString, type);
        }

}