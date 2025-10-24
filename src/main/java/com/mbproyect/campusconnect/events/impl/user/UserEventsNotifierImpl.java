package com.mbproyect.campusconnect.events.impl.user;

import com.mbproyect.campusconnect.events.contract.user.UserEventsNotifier;
import com.mbproyect.campusconnect.shared.service.MailService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserEventsNotifierImpl implements UserEventsNotifier {

    private final MailService mailService;

    public UserEventsNotifierImpl(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onUserRegisteredEvent(String email, String activationLink) {
        String body = String.format(
                "Hello,%n%n" +
                        "Welcome to %s! To activate your account, please click the link below:%n%n" +
                        "%s%n%n" +
                        "This link will expire in 24 hours for your security.%n" +
                        "If you didn’t create an account, please ignore this message.%n%n" +
                        "Best regards,%n" +
                        "The %s Support Team",
                "Campus connect", activationLink, "Campus connect"
        );

        mailService.sendEmail(email, "Account activation", body);

    }

    @Override
    public void onUserLoggedEvent(String email, String verificationCode) {
        String body = String.format(
                "Hello,\n\n" +
                        "Your verification code to access your account is: %s\n\n" +
                        "This code will expire in 10 minutes.\n" +
                        "If you didn’t request this code, please ignore this message.\n\n" +
                        "Best regards,\n" +
                        "The %s Support Team",
                verificationCode, "Campus connect"
        );
        mailService.sendEmail(email, "Account verification", body);
    }
}
