package com.example.demo.model;



    public class SignupRequestDTO {

        private String email;
        private String name;
        private String password;

        // Default constructor
        public SignupRequestDTO() {
        }

        // Parameterized constructor
        public SignupRequestDTO(String email, String name, String password) {
            this.email = email;
            this.name = name;
            this.password = password;
        }

        // Getter and Setter methods
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


