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
import org.springframework.test.context.jdbc.Sql;


public class ServiceResourceTest extends BaseIT {

    @Test
    @Sql("/data/serviceData.sql")
    void getAllServices_success() throws Exception {
        mockMvc.perform(get("/api/services")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1300)));
    }

    @Test
    @Sql("/data/serviceData.sql")
    void getAllServices_filtered() throws Exception {
        mockMvc.perform(get("/api/services?filter=1301")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1301)));
    }

    @Test
    @Sql("/data/serviceData.sql")
    void getService_success() throws Exception {
        mockMvc.perform(get("/api/services/1300")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Duis autem vel."));
    }

    @Test
    void getService_notFound() throws Exception {
        mockMvc.perform(get("/api/services/1966")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createService_success() throws Exception {
        mockMvc.perform(post("/api/services")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/serviceDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, serviceRepository.count());
    }

    @Test
    void createService_missingField() throws Exception {
        mockMvc.perform(post("/api/services")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/serviceDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
    }

    @Test
    @Sql("/data/serviceData.sql")
    void updateService_success() throws Exception {
        mockMvc.perform(put("/api/services/1300")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/serviceDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Nam liber tempor.", serviceRepository.findById(((long)1300)).get().getName());
        assertEquals(2, serviceRepository.count());
    }

    @Test
    @Sql("/data/serviceData.sql")
    void deleteService_success() throws Exception {
        mockMvc.perform(delete("/api/services/1300")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, serviceRepository.count());
    }

}
