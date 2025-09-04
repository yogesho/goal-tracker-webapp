package com.goaltracker.controller;

import com.goaltracker.dto.GoalDto;
import com.goaltracker.service.GoalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { GoalApiController.class })
class GoalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    @Test
    void toggleGoalDay_shouldReturnCompletedJson() throws Exception {
        Mockito.when(goalService.toggleGoalDay(1L, LocalDate.parse("2024-01-10"))).thenReturn(true);

        mockMvc.perform(post("/api/goals/1/days/2024-01-10/toggle")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    void getProgress_shouldReturnProgressJson() throws Exception {
        GoalDto dto = new GoalDto();
        dto.setId(1L);
        dto.setProgressPercentage(75.0);
        dto.setCompletedDays(15);
        dto.setTotalDays(20);
        Mockito.when(goalService.getGoalById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/goals/1/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.progressPercentage", is(75.0)))
                .andExpect(jsonPath("$.completedDays", is(15)))
                .andExpect(jsonPath("$.totalDays", is(20)));
    }
}
