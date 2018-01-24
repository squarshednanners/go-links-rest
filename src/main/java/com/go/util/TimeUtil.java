package com.go.util;

import java.time.ZonedDateTime;

public class TimeUtil {

	public static final ZonedDateTime topOfHour() {
		return ZonedDateTime.now().withMinute(0).withSecond(0).withNano(0);
	}

	public static final ZonedDateTime topOfDay() {
		return topOfHour().withHour(0);
	}
}
