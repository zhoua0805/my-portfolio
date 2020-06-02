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

import com.google.sps.data.Game;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.List;
import java.lang.*; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles the game two truths and one lie */

@WebServlet("/two-truths-one-lie")
public class GameServlet extends HttpServlet {

    private Game game = new Game();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        game.generateFacts(); 
        String json = new Gson().toJson(game);
        response.setContentType("application/json;");
        response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String selected = request.getParameter("fact");
        if (selected == null) {
            game.gameInvalid();
        }else if (Arrays.asList(game.getLies()).contains(selected)) {
            game.gameWin();
        }else{
            game.gameLose();
        }
        response.sendRedirect("/index.html#game");
  }
}

