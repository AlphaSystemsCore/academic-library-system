package com.sys.dao;

public class AdminDao {
    public void addAdmin(
        String firstName, 
        String lastName, 
        String email,
        String identificationNumber, 
        String phoneNumber,
        int roleId, 
        String password){
            String sql = """
                    INSERT INTO admins (first_name, last_name, email, identificationNumber,phone_number)
                    """;
        };
}
