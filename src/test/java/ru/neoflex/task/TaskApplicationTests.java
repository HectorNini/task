package ru.neoflex.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VacationPayControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCalculateVacationPay() throws Exception {
		mockMvc.perform(get("/calculate/1200000/22.03.2024/25.03.2024")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("Отпускные (Без НДФЛ): 6825<br>Потребуется отпускных дней: 2"));
	}

	@Test
	void testCalculateVacationPaySameDate() throws Exception {
		mockMvc.perform(get("/calculate/1200000/22.03.2024/22.03.2024")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("Отпускные (Без НДФЛ): 3412<br>Потребуется отпускных дней: 1"));
	}

	@Test
	void testCalculateVacationPayEndEarlierThenStart() throws Exception {
		mockMvc.perform(get("/calculate/1200000/22.03.2024/20.03.2024")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("Отпускные (Без НДФЛ): 0<br>Потребуется отпускных дней: 0"));
	}

	@Test
	void testCalculateVacationPayZeroSalary() throws Exception {
		mockMvc.perform(get("/calculate/0/22.03.2024/25.03.2024")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("Отпускные (Без НДФЛ): 0<br>Потребуется отпускных дней: 2"));
	}


}