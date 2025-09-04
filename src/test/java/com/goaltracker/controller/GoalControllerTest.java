package com.goaltracker.controller;

import com.goaltracker.dto.GoalDto;
import com.goaltracker.service.GoalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { GoalController.class })
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    @Test
    void listGoals_shouldRenderView() throws Exception {
        Mockito.when(goalService.getAllGoals()).thenReturn(List.of());
        mockMvc.perform(get("/goals"))
                .andExpect(status().isOk());
    }

    @Test
    void createGoal_shouldRedirectOnSuccess() throws Exception {
        GoalDto created = new GoalDto("T", "D", LocalDate.now(), LocalDate.now().plusDays(1));
        created.setId(1L);
        Mockito.when(goalService.createGoal(any(GoalDto.class))).thenReturn(created);

        mockMvc.perform(post("/goals")
                .param("title", "T")
                .param("description", "D")
                .param("startDate", LocalDate.now().toString())
                .param("endDate", LocalDate.now().plusDays(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/goals"));
    }
}
