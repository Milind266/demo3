package com.example.demo.model;

public class ErrorDTO {


        private String message;

        // Constructors
        public ErrorDTO() {
        }

        public ErrorDTO(String message) {
            this.message = message;
        }

        // Getters and Setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


