package com.StoreManagementClient.Middlewares;

import com.StoreManagementClient.Models.Role;
import com.StoreManagementClient.Models.Status;
import com.StoreManagementClient.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Converter {

    public static List<User> convertToUsers(List<Map<String, Object>> usersMap) {
        if (usersMap == null)
            return null;

        List<User> users = new ArrayList<User>();
        for (Map<String, Object> userMap : usersMap) {
            User user = convertToUser(userMap);
            System.out.println(user != null);
            if (user != null)
                users.add(user);
        }
        
        return users;
    }

    public static User convertToUser(Map<String, Object> userMap) {
        if (userMap == null)
            return null;

        User user = new User();
        user.setId((String) userMap.get("id"));
        user.setEmail((String) userMap.get("email"));
        user.setUsername((String) userMap.get("username"));
        user.setPassword((String) userMap.get("password"));
        user.setStatus(Status.valueOf((String) userMap.get("status")));
        user.setRole(Role.valueOf((String) userMap.get("role")));
        user.setAvatar((String) userMap.get("avatar"));

        return user;
    }

}
