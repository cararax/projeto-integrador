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


public class BookingResourceTest extends BaseIT {

    @Test
    @Sql("/data/bookingData.sql")
    void getAllBookings_success() throws Exception {
        mockMvc.perform(get("/api/bookings")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[0].id").value(((long)1400)));
    }

    @Test
    @Sql("/data/bookingData.sql")
    void getAllBookings_filtered() throws Exception {
        mockMvc.perform(get("/api/bookings?filter=1401")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].id").value(((long)1401)));
    }

    @Test
    @Sql("/data/bookingData.sql")
    void getBooking_success() throws Exception {
        mockMvc.perform(get("/api/bookings/1400")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking_notFound() throws Exception {
        mockMvc.perform(get("/api/bookings/2066")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.exception").value("NotFoundException"));
    }

    @Test
    void createBooking_success() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/bookingDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, bookingRepository.count());
    }

    @Test
    void createBooking_missingField() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/bookingDTORequest_missingField.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("startDateTime"));
    }

    @Test
    @Sql("/data/bookingData.sql")
    void updateBooking_success() throws Exception {
        mockMvc.perform(put("/api/bookings/1400")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(readResource("/requests/bookingDTORequest.json"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(2, bookingRepository.count());
    }

    @Test
    @Sql("/data/bookingData.sql")
    void deleteBooking_success() throws Exception {
        mockMvc.perform(delete("/api/bookings/1400")
                        .session(authenticatedSession())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertEquals(1, bookingRepository.count());
    }

}
