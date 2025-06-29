package br.ufsm.csi.spring.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {

    private Integer id;
    private String title;
    private String description;
    private boolean status;
    private LocalDate date;
    private User user; // Agora apenas o objeto User
    private Category category;

    private String formattedDate;




    public Task() {}

    public Task(Integer id, String title, String description, boolean status, User user, Category category, LocalDate date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.date = date;
        this.user = user;
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getFormattedDate() {

        if (this.date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return this.date.format(formatter);
        }
        return "";
    }
    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }


}
