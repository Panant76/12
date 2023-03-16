package by.vitstep.organizer.web;

import by.vitstep.organizer.client.WeatherClient;
import by.vitstep.organizer.model.dto.weather.Forecast;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final WeatherClient client;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/hello")
    public String hello(String name, HttpServletResponse response) throws TemplateException, IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        final ClassTemplateLoader loader = new ClassTemplateLoader(TestController.class, "/ftl");
        cfg.setTemplateLoader(loader);
        cfg.setDefaultEncoding("UTF-8");

        //модель данных
        Map<String, Object> root = new HashMap<>();
        root.put("name", "Anton");
        root.put("currentDate", LocalDateTime.now());

        //шаблон
        Template tmp = cfg.getTemplate("test.ftl");
        // обработка шаблона и модели данных
// вывод в консоль
        Writer w = new StringWriter();
        tmp.process(root, w);
        return w.toString();

    }
    @GetMapping("/echo")
    public String echo(@RequestParam String string, @RequestHeader(HttpHeaders.USER_AGENT) String userAgent){
        if (StringUtils.containsAnyIgnoreCase(userAgent,"PostmanRuntime")){
            return "Postman is not supported";
        }
        return string;
    }
    @GetMapping("/forecast")
    public ResponseEntity<Forecast> forecast(@RequestParam String city){
        return ResponseEntity.ok(client.getForecat(city));
    }
}
