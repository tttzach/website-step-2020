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

import com.opencsv.CSVReaderHeaderAware;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Returns coronavirus data as a JSON object, e.g. {"Afghanistan": 20917, "Albania": 1263}]
@WebServlet("/coronavirus-data")
public class CoronavirusDataServlet extends HttpServlet {

  private final Map<String, Integer> coronavirusCases = new HashMap<>();

  @Override
  public void init() {
    String filePath = "../../src/main/webapp/WEB-INF/coronavirus-stats-by-country.csv";
    readCsv(filePath);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(coronavirusCases);
    response.getWriter().println(json);
  }

  private void readCsv(String filePath) {
    try {
      CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new FileReader(filePath));
      Map<String, String> line;
      while ((line = csvReader.readMap()) != null) {
        String country = line.get("Country/Region");
        Integer confirmedCases = Integer.valueOf(line.get("Confirmed"));
        coronavirusCases.put(country, confirmedCases);
      }
      csvReader.close();
    } catch (IOException exception) {
      System.out.println("File not found.");
    }
  }

}
