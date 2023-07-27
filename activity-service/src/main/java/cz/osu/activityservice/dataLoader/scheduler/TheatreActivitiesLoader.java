package cz.osu.activityservice.dataLoader.scheduler;

import cz.osu.activityservice.service.TheatreActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TheatreActivitiesLoader {
    private final TheatreActivityService theatreActivityService;

    @Scheduled(cron = "0 0 6 * * ?",zone = "Europe/Prague" )
    @SchedulerLock(name = "loadActivity", lockAtMostFor = "PT2H30M",lockAtLeastFor ="PT2H30M" )
    public void loadActivity() {
        LockAssert.assertLocked();

        log.info("Start loading new theatre activities");

        this.theatreActivityService.saveAllNewActivities();

        log.info("End of loading new theatre activities");
    }
}