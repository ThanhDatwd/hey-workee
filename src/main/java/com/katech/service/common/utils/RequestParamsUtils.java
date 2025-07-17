package com.katech.service.common.utils;

import com.katech.service.common.constants.ErrorMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RequestParamsUtils {
    public static void validatePageParameters(Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageNumber < 0) {
            throw new IllegalArgumentException(ErrorMessage.PAGE_NUMBER_MUST_BE_POSITIVE);
        }
        if (pageSize != null && pageSize < 1) {
            throw new IllegalArgumentException(ErrorMessage.PAGE_SIZE_MUST_BE_GREATER_THAN_ZERO);
        }
    }

    public static void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null) {
            LocalDateTime from = fromDate.atStartOfDay();
            LocalDateTime to = toDate.atTime(LocalTime.MAX);
            if (from.isAfter(to)) {
                throw new IllegalArgumentException(ErrorMessage.FROM_DATE_MUST_BE_BEFORE_TO_DATE);
            }
        }
    }
}
