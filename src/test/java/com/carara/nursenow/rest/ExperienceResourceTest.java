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


public class ExperienceResourceTest extends BaseIT {

    @Test
    @Sql("/data/experienceData.sql")
    void getAllExperiences_success() throws Exception {
        mockMvc.perform(get("/api/experiences")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1200)));
    }

    @Test
    @Sql("/data/experienceData.sql")
    void getAllExperiences_filtered() throws Exception {
        mockMvc.perform(get("/api/experiences?filter=1201")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1201)));
    }

    @Test
    @Sql("/data/experienceData.sql")
    void getExperience_success() throws Exception {
        mockMvc.perform(get("/api/experiences/1200")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Ut wisi enim."));
    }

    @Test
    void getExperience_notFound() throws Exception {
        mockMvc.perform(get("/api/experiences/1866")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createExperience_success() throws Exception {
        mockMvc.perform(post("/api/experiences")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/experienceDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, experienceRepository.count());
    }

    @Test
    void createExperience_missingField() throws Exception {
        mockMvc.perform(post("/api/experiences")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/experienceDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("company"));
    }

    @Test
    @Sql("/data/experienceData.sql")
    void updateExperience_success() throws Exception {
        mockMvc.perform(put("/api/experiences/1200")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/experienceDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Sed ut perspiciatis.", experienceRepository.findById(((long)1200)).get().getCompany());
        assertEquals(2, experienceRepository.count());
    }

    @Test
    @Sql("/data/experienceData.sql")
    void deleteExperience_success() throws Exception {
        mockMvc.perform(delete("/api/experiences/1200")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, experienceRepository.count());
    }

}
