package ru.neoflex.task.controller;

import lombok.SneakyThrows;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Validated
@RestController
public class VacationPayController {
    private static final double AVERAGE_DAYS_IN_MONTH = 29.3;
    private static final String URL_SOURCE = "https://isdayoff.ru/";
    private static final int HOLIDAY_CODE = 1;

    @GetMapping("/calculate/{salary}/{startDate}/{endDate}")
    public String calculateVacationPay(@PathVariable("salary") Integer yearSalary,
                                       @PathVariable("startDate") String startDateStr,
                                       @PathVariable("endDate") String endDateStr) {
        DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter URLFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(startDateStr, defaultFormatter);
        LocalDate end = LocalDate.parse(endDateStr, defaultFormatter);
        LocalDate currentDate = start;
        int workingDays = 0;
        int vacationAmount;
        String currentURL;

        while (!currentDate.isAfter(end)) {
            currentURL = URL_SOURCE + currentDate.format(URLFormatter);
            if (!isWeekend(currentDate) && !isHoliday(currentURL)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        vacationAmount = (int) (yearSalary / 12 / AVERAGE_DAYS_IN_MONTH * workingDays);
        return "Отпускные (Без НДФЛ): " + vacationAmount + "\nПотребуется отпускных дней: " + workingDays;
    }

    @SneakyThrows
    private boolean isHoliday(String currentURL) {
        boolean isHoliday = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(currentURL).openConnection().getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            int code = Integer.parseInt(response.toString().trim());
            isHoliday = code == HOLIDAY_CODE;
        }
        return isHoliday;
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}