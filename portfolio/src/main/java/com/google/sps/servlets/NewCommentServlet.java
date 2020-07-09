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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet responsible for creating new comments.
@WebServlet("/new-comment")
public class NewCommentServlet extends HttpServlet {

  static DecimalFormat oneDecimalPlace = new DecimalFormat("0.0");

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    String comment = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();
    String score = getSentimentScore(comment);
    Entity commentEntity = createEntity(comment, timestamp, email, score);
    putEntity(commentEntity);
    response.sendRedirect("/index.html");
  }

  private Entity createEntity(String comment, long timestamp, String email, String score) {
    Entity entity = new Entity("Comment");
    entity.setProperty("comment", comment);
    entity.setProperty("timestamp", timestamp);
    entity.setProperty("email", email);
    entity.setProperty("score", score);
    return entity;
  }

  private void putEntity(Entity entity) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(entity);
  }

  private String getSentimentScore(String text) throws IOException {
    Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    try (LanguageServiceClient languageService = LanguageServiceClient.create()) {
      Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
      float score = sentiment.getScore();
      return oneDecimalPlace.format(score);
    }
  }

}
