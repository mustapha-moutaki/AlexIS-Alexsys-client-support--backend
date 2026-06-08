package com.alexsysSolutions.alexsis.service;

public interface IAiService {

    /**
     * Send a message to AI and get response
     *
     * @param message user question
     * @return AI response
     */
    String ask(String message);
}