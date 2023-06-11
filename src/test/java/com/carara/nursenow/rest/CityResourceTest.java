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


public class CityResourceTest extends BaseIT {

    @Test
    @Sql("/data/cityData.sql")
    void getAllCitys_success() throws Exception {
        mockMvc.perform(get("/api/citys")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1100)));
    }

    @Test
    @Sql("/data/cityData.sql")
    void getAllCitys_filtered() throws Exception {
        mockMvc.perform(get("/api/citys?filter=1101")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1101)));
    }

    @Test
    @Sql("/data/cityData.sql")
    void getCity_success() throws Exception {
        mockMvc.perform(get("/api/citys/1100")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Duis autem vel."));
    }

    @Test
    void getCity_notFound() throws Exception {
        mockMvc.perform(get("/api/citys/1766")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createCity_success() throws Exception {
        mockMvc.perform(post("/api/citys")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cityDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, cityRepository.count());
    }

    @Test
    void createCity_missingField() throws Exception {
        mockMvc.perform(post("/api/citys")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cityDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
    }

    @Test
    @Sql("/data/cityData.sql")
    void updateCity_success() throws Exception {
        mockMvc.perform(put("/api/citys/1100")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/cityDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals("Nam liber tempor.", cityRepository.findById(((long)1100)).get().getName());
        assertEquals(2, cityRepository.count());
    }

    @Test
    @Sql("/data/cityData.sql")
    void deleteCity_success() throws Exception {
        mockMvc.perform(delete("/api/citys/1100")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, cityRepository.count());
    }

}
