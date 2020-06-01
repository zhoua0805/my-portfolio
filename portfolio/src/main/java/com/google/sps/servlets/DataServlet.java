// Copyright 2019 Google LLC
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

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.List;
import java.lang.*; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
//play a game of two truths and one lie
@WebServlet("/two-truths-one-lie")
public class DataServlet extends HttpServlet {

    private String[] truths = new String[] {"I play the piano.", 
                    "I don\'t like pineapples on pizza.",
                    "I have never been skydiving before.", 
                    "I am hungry.", 
                    "I visited my friend in Manchester last Christmas."};

    private String[] lies = new String[] {"My favourite subject in high school was Calculus.",  
                "I have two brothers.",
                "I was born in the year of the tiger."};

    private String[] twoTruthsOneLie = new String[3];
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // generate two truths and one lie
        int truth_1_index;
        int truth_2_index;
        int lie_index;
        String TheInnerHTML;

        truth_1_index = (int) (Math.random() * truths.length);
        do {
            truth_2_index = (int)(Math.random() * truths.length);
        }while (truth_1_index == truth_2_index);
        
        lie_index = (int)(Math.random() * lies.length);
        twoTruthsOneLie[0] = truths[truth_1_index];
        twoTruthsOneLie[1] = truths[truth_2_index];
        twoTruthsOneLie[2] = lies[lie_index];
        shuffle(twoTruthsOneLie); //randomize the order of the items in the list   
        TheInnerHTML ="<div>";
        for (int i = 0; i < twoTruthsOneLie.length; i++) {
            TheInnerHTML += "<label>" +
                                "<input type=\"radio\" name =\"group1\" id =" + String.valueOf(i) + 
                                " value=\""+ twoTruthsOneLie[i] + "\" onchange=\"printGameResult()\"/>" +
                                "<span style = \"color: #410219\">"+ twoTruthsOneLie[i] + "</span>" +
                            "</label><br>";
        }
        TheInnerHTML += "</div>";
		response.setContentType("text/html;");
        response.getWriter().println(TheInnerHTML);
  }

  // shuffle an array using the Durstenfeld shuffle algorithm 
    public void shuffle(String[] array) {
        int randomIndex;
    	String swap;
        for (int i = array.length - 1; i > 0; i--) {
            randomIndex = (int)(Math.random() * (i+1));
            swap = array[randomIndex];
            array[randomIndex] = array[i];
            array[i] = swap;
        }
    }
}

