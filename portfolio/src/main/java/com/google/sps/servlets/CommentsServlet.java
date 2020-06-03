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
import com.google.sps.data.MapsComment;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.*; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that stores and returns comments data */

@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        List<MapsComment> mapsComments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            double lat = (double) entity.getProperty("lat");
            double lng = (double) entity.getProperty("lng");
            String name = (String) entity.getProperty("name");
            String content = (String) entity.getProperty("content");
            MapsComment comment = new MapsComment(id, lat, lng, name, content);
            mapsComments.add(comment);
        }

        response.setContentType("application/json");
        String json = new Gson().toJson(mapsComments);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            double lat = Double.valueOf(request.getParameter("lat"));
            double lng = Double.valueOf(request.getParameter("lng"));
            String name = request.getParameter("fullname");
            String content = request.getParameter("comment");

            Entity commentEntity = new Entity("Comment");
            commentEntity.setProperty("lat", lat);
            commentEntity.setProperty("lng", lng);
            commentEntity.setProperty("name", name);
            commentEntity.setProperty("content", content);

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            datastore.put(commentEntity);
            response.sendRedirect("/index.html#game");
    }
}

