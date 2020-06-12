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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
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
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;


/** Servlet that stores and returns comments data */

@WebServlet("/comments")
public class CommentsServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Filter> filters = new ArrayList<>();
        String[] filterOptions = request.getParameterMap().get("option");
        String languageCode = request.getParameterMap().get("language")[0];
        Query query = new Query("Comment"); //No filters if nothing is selected;

        //Add filters if users select one or more filtering options.
        if(filterOptions != null){ 
            if (filterOptions.length == 1){  
                query = new Query("Comment").setFilter
                    (new FilterPredicate("category", FilterOperator.EQUAL, filterOptions[0]));
            }else {  
                //If multiple filters are applied, use compositefilter
                //(CompositeFilter can only accept more than one filter.)
                for (String option: filterOptions) {
                    filters.add (new FilterPredicate("category", FilterOperator.EQUAL, option));
                }
                CompositeFilter optionsFilter = CompositeFilterOperator.or(filters);
                query = new Query("Comment").setFilter(optionsFilter);
            }
        }
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        Translate translate = TranslateOptions.getDefaultInstance().getService();

        List<MapsComment> mapsComments = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            long id = entity.getKey().getId();
            double lat = (double) entity.getProperty("lat");
            double lng = (double) entity.getProperty("lng");
            String name = (String) entity.getProperty("name");
            String category = (String) entity.getProperty("category");
            String content = "";
            if (languageCode.equals("original")) {
                content = (String) entity.getProperty("content");
            }else {
                content = translate.translate((String) entity.getProperty("content"), 
                            Translate.TranslateOption.targetLanguage(languageCode))
                            .getTranslatedText();
            }
            String userId = (String) entity.getProperty("userId");
            MapsComment comment = new MapsComment(id, lat, lng, name, category, content, userId);
            mapsComments.add(comment);
        }

        response.setContentType("application/json");
        String json = new Gson().toJson(mapsComments);
        response.getWriter().println(json);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        if (!userService.isUserLoggedIn()) {
            response.sendRedirect("/");
            return;
        }
        String userId = userService.getCurrentUser().getUserId();
        double lat = Double.valueOf(request.getParameter("lat"));
        double lng = Double.valueOf(request.getParameter("lng"));
        String name = request.getParameter("fullname");
        String category = request.getParameter("category");
        String content = request.getParameter("comment");

        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("userId", userId);
        commentEntity.setProperty("lat", lat);
        commentEntity.setProperty("lng", lng);
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("category", category);
        commentEntity.setProperty("content", content);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
        response.sendRedirect("/index.html#game");
    }
}

