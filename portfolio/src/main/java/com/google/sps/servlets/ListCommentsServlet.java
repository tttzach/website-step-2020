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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
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
    String language = getLanguage(request);
    List<String> comments = getCommentsToDisplay(results, max, language);
    JsonUtil.sendJson(response, comments);
  }

  private PreparedQuery prepareQuery() {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.prepare(query);
  }

  private int getMax(HttpServletRequest request) {
    // queryString = "max=...&lang=..."
    String queryString = request.getQueryString();
    // maxString = "max=..."
    String maxString = queryString.split("&")[0];
    String max = maxString.split("=")[1];
    return Integer.parseInt(max);
  }

  private String getLanguage(HttpServletRequest request) {
    // queryString = "max=...&language=..."
    String queryString = request.getQueryString();
    // languageString = "language=..."
    String languageString = queryString.split("&")[1];
    return languageString.split("=")[1];
  }

  private List<String> getCommentsToDisplay(PreparedQuery results, int max, String language) {
    List<String> comments = new ArrayList<>();
    List<Entity> entities = results.asList(FetchOptions.Builder.withLimit(max));
    for (Entity entity : entities) {
      String email = (String) entity.getProperty("email");
      String comment = (String) entity.getProperty("comment");
      String translatedComment = getTranslation(comment, language);
      comments.add(email + ": " + translatedComment);
    }
    return comments;
  }

  private String getTranslation(String originalText, String languageCode) {
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    Translation translation = translate.translate(
      originalText, 
      Translate.TranslateOption.targetLanguage(languageCode)
    );
    return translation.getTranslatedText();
  }
  
}
