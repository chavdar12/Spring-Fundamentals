package bg.softuni.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CookieController {

    private static final String LANG_COOKIE_NAME = "lang";

    @GetMapping("/cookies")
    public String cookies(@CookieValue(name = LANG_COOKIE_NAME, defaultValue = "en") String langCookieValue, Model model) {
        model.addAttribute("lang", langCookieValue);
        return "cookies";
    }


    @PostMapping("/cookies")
    public String submitCookies(@RequestParam(name = "lang") String langFromHTMLForm, HttpServletResponse response) {

        Cookie langCookie = new Cookie(LANG_COOKIE_NAME, langFromHTMLForm);
        response.addCookie(langCookie);

        System.out.println("Preferred user language is: " + langFromHTMLForm);
        return "redirect:/cookies";
    }
}
