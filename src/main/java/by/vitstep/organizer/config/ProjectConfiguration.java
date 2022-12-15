package by.vitstep.organizer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "project")
@Component
public class ProjectConfiguration {
    private String FormatDate;
    private String FormatDateTime;
}
