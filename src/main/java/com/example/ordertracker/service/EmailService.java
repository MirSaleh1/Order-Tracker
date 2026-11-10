package com.example.ordertracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOrderStatusUpdateEmail(String email, String newStatus, String orderNumber) {
        try {
            log.info("Email göndərilir: {} üçün sifariş {}", email, orderNumber);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Sifarişinizin Statusu Yeniləndi: " + orderNumber);
            message.setText("Hörmətli müştəri,\n\n" +
                    orderNumber + " nömrəli sifarişinizin yeni statusu: " + newStatus + ".\n\n" +
                    "Bizi seçdiyiniz üçün təşəkkür edirik!");
            mailSender.send(message);
            log.info("Email uğurla çatdırıldı: {}", email);
        } catch (Exception e) {
            log.error("Email göndərilərkən xəta baş verdi: {}", e.getMessage());
        }
    }
}
