package com.maxkavun.listener;

import com.maxkavun.service.MatchMonitor;
import com.maxkavun.service.OngoingMatchService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class MatchLifecycleListener implements ServletContextListener {
    private OngoingMatchService matchService;
    private MatchMonitor matchMonitor;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        matchService = OngoingMatchService.getInstance();
        matchMonitor = new MatchMonitor(matchService);

        matchMonitor.startMonitoring();
        log.debug("Match Monitor started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        matchMonitor.stopMonitoring();
        log.debug("Match Monitor stopped");
    }
}
