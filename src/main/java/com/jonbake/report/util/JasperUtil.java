package com.jonbake.report.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Utilities related to Jasper Report running.
 *
 * @author jonmbake
 */
public final class JasperUtil {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    public static final Function<Map.Entry<String, List<String>>, Object> QUERY_TO_JASPER_PARAM = (p) -> {
        List<String> values = p.getValue();
        if (values.size() > 1) {
            return values;
        }
        String value = values.get(0);
        if (NumberUtils.isNumber(value)) {
            return NumberUtils.createNumber(value);
        }
        try {
            return SDF.parse(value);
        } catch (ParseException e) {
        }
        //either String or Collection
        return value;
    };
    /**
     * Private constructor -- this is a utility class.
     */
    private JasperUtil () {
    }
}
