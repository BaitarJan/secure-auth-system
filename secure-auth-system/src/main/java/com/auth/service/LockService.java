package com.auth.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LockService {

    public boolean isLocked(LocalDateTime lockUntil) {
        if (lockUntil == null) return false;

        return LocalDateTime.now().isBefore(lockUntil);
    }

    public LocalDateTime lockForMinutes(int minutes) {
        return LocalDateTime.now().plus(minutes, ChronoUnit.MINUTES);
    }
}

