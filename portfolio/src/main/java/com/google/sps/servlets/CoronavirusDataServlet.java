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

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Returns coronavirus data as a JSON object, e.g. {"Afghanistan": 20917, "Albania": 1263}]
@WebServlet("/coronavirus-data")
public class CoronavirusDataServlet extends HttpServlet {

  private final LinkedHashMap<String, Integer> coronavirusCases = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/coronavirus-stats-by-country.csv"));
    // Ignore header row = "Country/Region,Confirmed,Active,..."
    if (scanner.hasNextLine()) {
      scanner.nextLine();
    }
    while (scanner.hasNextLine()) {
      // line = "Afghanistan,20917,369.0,..."
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      String country = String.valueOf(cells[0]);
      Integer confirmedCases = Integer.valueOf(cells[1]);
      coronavirusCases.put(country, confirmedCases);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(coronavirusCases);
    response.getWriter().println(json);
  }
  
}
