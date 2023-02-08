package account.auth;

import account.entities.LogAction;
import account.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private LogService logService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        String username = event.getAuthentication().getName();

        String requestPath = request.getServletPath();

        logService.makeLogEntry(LogAction.LOGIN_FAILED, username, requestPath, requestPath);

        userDetailsService.incrementFailedAttempts(username, requestPath);

    }

}
