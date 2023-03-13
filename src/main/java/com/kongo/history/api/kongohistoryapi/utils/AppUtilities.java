package com.kongo.history.api.kongohistoryapi.utils;

import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.validation.BindingResult;

public class AppUtilities {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String convertDateToString(final Date date) {
        final var sdf = new SimpleDateFormat(AppUtilities.DATE_FORMAT);
        return sdf.format(date);
    }

    public static Date convertStringFormatToDate(final String dateString) throws DateTimeParseException {
        final var dateTimeFormatter = DateTimeFormatter.ofPattern(AppUtilities.DATE_FORMAT);
        final var localDate = LocalDate.parse(dateString, dateTimeFormatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void controlForm(BindingResult bindingResult) throws ValueDataException {
        if (bindingResult.hasErrors()) {
            throw new ValueDataException(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    AppConst._KEY_CODE_PARAMS_ERROR);
        }
    }

    public static boolean modifiableValue(final String valueData, final String formData) {
        try {
            return !formData.isEmpty();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Supplier<ValueDataException> supplyException(final String message, final String code) {
        return () -> new ValueDataException(message, code);
    }
}
