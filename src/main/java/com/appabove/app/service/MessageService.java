package com.appabove.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;

@Service
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    public String get(String key, Object... args) {
        String pattern = messageSource.getMessage(key, null, Locale.getDefault());
        return MessageFormat.format(pattern, args);
    }
}
