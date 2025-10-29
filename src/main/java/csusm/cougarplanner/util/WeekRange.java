package csusm.cougarplanner.util;

import java.time.LocalDate;

/**
 * Represents a week range
 */
public record WeekRange(LocalDate startIncl, LocalDate endExcl) {}
