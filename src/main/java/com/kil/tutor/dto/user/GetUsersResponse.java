package com.kil.tutor.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class GetUsersResponse {
    private List<UserInfo> users;

    GetUsersResponse(List<UserInfo> users) {
        this.users = users;
    }

    public static UserInfosBuilder builder() {
        return new UserInfosBuilder();
    }

    public static class UserInfosBuilder {
        private List<UserInfo> users;

        UserInfosBuilder() {
        }

        public UserInfosBuilder users(List<UserInfo> users) {
            this.users = users;
            return this;
        }

        public GetUsersResponse build() {
            return new GetUsersResponse(users);
        }

        public String toString() {
            return "UserInfos.UserInfosBuilder(users=" + this.users + ")";
        }
    }
}
