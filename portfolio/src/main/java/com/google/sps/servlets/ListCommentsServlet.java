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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet responsible for listing comments.
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    PreparedQuery results = prepareQuery();
    int max = getMax(request);
    List<String> comments = getCommentsToDisplay(results, max);
    JsonUtil.sendJson(response, comments);
  }

  private PreparedQuery prepareQuery() {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.prepare(query);
  }

  private int getMax(HttpServletRequest request) {
    // queryString = "max=...";
    String queryString = request.getQueryString();
    String maxString = queryString.split("=")[1];
    return Integer.parseInt(maxString);
  }

  private List<String> getCommentsToDisplay(PreparedQuery results, int max) {
    List<String> comments = new ArrayList<>();
    Iterator<Entity> iterator = results.asIterator();
    for (int i = 0; (i < max) && (iterator.hasNext()); ++i) {
      Entity entity = iterator.next();
      String email = (String) entity.getProperty("email");
      String comment = (String) entity.getProperty("comment");
      comments.add(email + ": " + comment);
    }
    return comments;
  }
  
}
