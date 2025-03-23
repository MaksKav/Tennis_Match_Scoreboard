package com.maxkavun.service;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class MatchMonitor {

    private static final long CHECK_INTERVAL = 60 * 1000; // 1 min
    private final OngoingMatchService ongoingMatchService;
    private final ScheduledExecutorService scheduler;

    public MatchMonitor(OngoingMatchService ongoingMatchService) {
        this.ongoingMatchService = ongoingMatchService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startMonitoring() {
        log.debug("Starting Match Monitor");
        scheduler.scheduleAtFixedRate(this::checkMatches, 0, CHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    private void checkMatches() {
        log.debug("Checking Matches from Match Monitor");
        for (UUID matchId : OngoingMatchService.getOngoingMatches().keySet()) {
            ongoingMatchService.checkAndResetMatchIfNeeded(matchId);
        }
    }

    public void stopMonitoring() {
        log.debug("Stopping Match Monitor");
        scheduler.shutdown();
    }
}
