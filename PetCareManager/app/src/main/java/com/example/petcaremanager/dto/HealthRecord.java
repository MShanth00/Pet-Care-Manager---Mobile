package com.example.petcaremanager.dto;

public class HealthRecord {
        private int id; // Unique ID for the health record
        private int petId; // ID of the pet this record belongs to
        private String date; // Date of the health record
        private double weight; // Weight of the pet
        private double temperature; // Temperature of the pet
        private String notes; // Additional health notes

        public HealthRecord(int id, int petId, String date, double weight, double temperature, String notes) {
            this.id = id;
            this.petId = petId;
            this.date = date;
            this.weight = weight;
            this.temperature = temperature;
            this.notes = notes;
        }

        // Getter and Setter methods
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPetId() {
            return petId;
        }

        public void setPetId(int petId) {
            this.petId = petId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
}
