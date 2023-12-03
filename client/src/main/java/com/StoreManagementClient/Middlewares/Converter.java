package com.StoreManagementClient.Middlewares;

import com.StoreManagementClient.Models.Role;
import com.StoreManagementClient.Models.Status;
import com.StoreManagementClient.Models.User;

import java.util.Map;

public class Converter {

    public static User convertToUser(Map<String, Object> userMap) {
        if (userMap == null) {
            return null;
        }

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
