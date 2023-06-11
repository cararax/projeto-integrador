package com.carara.nursenow.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.carara.nursenow.config.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


public class UsersResourceTest extends BaseIT {

    @Test
    void getAllUserss_success() throws Exception {
        mockMvc.perform(get("/api/userss")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1000)));
    }

    @Test
    void getAllUserss_filtered() throws Exception {
        mockMvc.perform(get("/api/userss?filter=1001")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1001)));
    }

    @Test
    void getUsers_success() throws Exception {
        mockMvc.perform(get("/api/userss/1000")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Duis autem vel."));
    }

    @Test
    void getUsers_notFound() throws Exception {
        mockMvc.perform(get("/api/userss/1666")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createUsers_success() throws Exception {
        mockMvc.perform(post("/api/userss")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/usersDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(3, usersRepository.count());
    }

    @Test
    void createUsers_missingField() throws Exception {
        mockMvc.perform(post("/api/userss")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/usersDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("firstname"));
    }

    @Test
    void updateUsers_success() throws Exception {
        mockMvc.perform(put("/api/userss/1000")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/usersDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Nam liber tempor.", usersRepository.findById(((long)1000)).get().getFirstname());
        assertEquals(2, usersRepository.count());
    }

    @Test
    void deleteUsers_success() throws Exception {
        mockMvc.perform(delete("/api/userss/1000")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, usersRepository.count());
    }

}
