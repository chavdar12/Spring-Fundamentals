package bg.softuni.mobilele.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logger")
public class LoggerController {

    private final Logger LOGGER = LoggerFactory.getLogger(LoggerController.class);

    @GetMapping("/error")
    public String logError() {
        LOGGER.error("I'm logging an error.");
        return "OK";
    }

    @GetMapping("/warning")
    public String logWarning() {
        LOGGER.warn("I'm logging an warning.");
        return "OK";
    }

}
