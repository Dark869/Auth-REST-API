package com.dark869.auth_api.utils;

import java.util.UUID;

public interface INotification {
    void sendNotification(String to, String subject, UUID userId);
}
