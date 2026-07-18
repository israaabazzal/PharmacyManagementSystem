package com.pharmacy.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String phone;
    private int roleId;
    private boolean isActive;
    
    // Constructor
    public User() {
    // No-argument constructor needed for DAO and forms
}

    public User(int userId, String username, String password, 
                String email, String phone, int roleId, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.roleId = roleId;
        this.isActive = isActive;
    }
    
    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getRoleId() { return roleId; }
    public boolean isActive() { return isActive; }
    
    // Setters
    public void setUserId(int userId) { 
        this.userId = userId; }
    
    public void setUsername(String username) { 
        this.username = username; }
    
    private String roleName;
    
    public String getRoleName() {
    return roleName;}

    public void setRoleName(String roleName) {
    this.roleName = roleName;}

    public void setPassword(String password) {
    this.password = password;}

    public void setEmail(String email) {
    this.email = email;}

    public void setPhone(String phone) {
    this.phone = phone;}

    public void setRoleId(int roleId) {
    this.roleId = roleId;}

    public void setActive(boolean isActive) {
    this.isActive = isActive;}

   
   
   
   
}