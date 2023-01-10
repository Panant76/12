package by.vitstep.organizer.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Getter
@Setter
@ConfigurationProperties(prefix = "project")
@Component
public class ProjectConfiguration {
    private String FormatDate;
    private String FormatDateTime;
    private Security security;
    private Business business;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Security{
        private String secretKey;
        private Integer tokenLifetimeHours;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Business{
        private Integer archivationPeriodDays;
    }

}
