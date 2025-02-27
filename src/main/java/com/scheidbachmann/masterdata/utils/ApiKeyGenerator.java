/**
 * Created By Amine Barguellil
 * Date : 5/16/2024
 * Time : 11:29 AM
 * Project Name : master-data
 */


package com.scheidbachmann.masterdata.utils;

import java.security.SecureRandom;

public class ApiKeyGenerator {


    public static String generateApiKey() {
        // Define the length of the API key
        int keyLength = 32; // You can adjust the length as needed

        // Characters to be used in generating the API key
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_$+/*=%:!.";

        // Create a SecureRandom object for generating random numbers
        SecureRandom random = new SecureRandom();

        // StringBuilder to hold the generated API key
        StringBuilder apiKeyBuilder = new StringBuilder(keyLength);

        // Generate random characters and append them to the apiKeyBuilder
        for (int i = 0; i < keyLength; i++) {
            apiKeyBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }

        // Convert the StringBuilder to a string and return the API key
        return apiKeyBuilder.toString();
    }

}

