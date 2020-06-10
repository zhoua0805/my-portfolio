// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns user login status and redirecturl */

@WebServlet("/auth")
public class LoginServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (userService.isUserLoggedIn()) {
            String logoutUrl = userService.createLogoutURL("/");
            String userId = userService.getCurrentUser().getUserId();
            LoginStatus loggedIn = new LoginStatus(true, logoutUrl, userId);

            String json = new Gson().toJson(loggedIn);
            response.setContentType("application/json");
            response.getWriter().println(json);
        } else {
            String loginUrl = userService.createLoginURL("/");
            LoginStatus loggedOut = new LoginStatus(false, loginUrl, null);

            String json = new Gson().toJson(loggedOut);
            response.setContentType("application/json");
            response.getWriter().println(json);
        }
    }
}


class LoginStatus {
    private final boolean Loggedin;
    private final String url; 
    private final String userId;

    public LoginStatus(boolean Loggedin, String url, String userId) {
        this.Loggedin = Loggedin; 
        this.url = url;
        this.userId = userId;
    }
}

