package bg.softuni.mobilele.config;

import bg.softuni.mobilele.service.OfferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final MobileleMethodSecurityExpressionHandler mobileleMethodSecurityExpressionHandler;

    public SecurityConfiguration(MobileleMethodSecurityExpressionHandler mobileleMethodSecurityExpressionHandler) {
        this.mobileleMethodSecurityExpressionHandler = mobileleMethodSecurityExpressionHandler;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return mobileleMethodSecurityExpressionHandler;
    }

    @Bean
    public MobileleMethodSecurityExpressionHandler createExpressionHandler(OfferService offerService) {
        return new MobileleMethodSecurityExpressionHandler(offerService);
    }
}

