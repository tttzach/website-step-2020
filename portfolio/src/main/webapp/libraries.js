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

function createMap() {
  const uWaterloo = { lat: 43.473, lng: -80.545 };
  const map = mapInit(uWaterloo);
  const marker = createMapMarker(map, uWaterloo);
  changeMapToTerrain(map);
  createMapInfoWindow(map, marker);
}

function mapInit(position) {
  const map = new google.maps.Map(
    document.getElementById('map'), {
    center: position,
    zoom: 16
  }
  );
  return map;
}

function createMapMarker(map, position) {
  const marker = new google.maps.Marker({
    map: map,
    position: position
  });
  return marker;
}

function changeMapToTerrain(map) {
  map.setMapTypeId('terrain');
}

function createMapInfoWindow(map, marker) {
  const contentString = '<div id="infowindow">' +
    '<div id="siteNotice">' +
    '</div>' +
    '<h1 id="firstHeading" class="firstHeading">University of Waterloo</h1>' +
    '<div id="bodyContent">' +
    '<p>The <b>University of Waterloo</b> (commonly referred to as <b>Waterloo</b>, <b>UW</b>, or ' +
    '<b>UWaterloo</b>) is a public research university with a main campus in Waterloo, Ontario, ' +
    'Canada. The main campus is on 404 hectares of land adjacent to Uptown Waterloo and Waterloo ' +
    'Park. The university also operates three satellite campuses and four affiliated university ' +
    'colleges.</p>' +
    '<p>Attribution: University of Waterloo, <a href="https://en.wikipedia.org/wiki/University_of_Waterloo">' +
    'https://en.wikipedia.org/wiki/University_of_Waterloo</a> ' +
    '(last visited June 8, 2020).</p>' +
    '</div>' +
    '</div>';
  const infoWindow = new google.maps.InfoWindow({
    content: contentString
  });
  marker.addListener('click', function () {
    infoWindow.open(map, marker);
  });
}

google.charts.load('current', { 'packages': ['corechart'] });
google.charts.setOnLoadCallback(drawPieChart);
google.charts.setOnLoadCallback(drawRegionsChart);
google.charts.setOnLoadCallback(drawCoronavirusChart);

function drawPieChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Animal');
  data.addColumn('number', 'Count');
  data.addRows([
    ['Lions', 10],
    ['Tigers', 5],
    ['Bears', 15]
  ]);

  const options = {
    'title': 'Zoo Animals',
    'width': 700,
    'height': 500
  };

  const chart = new google.visualization.PieChart(document.getElementById('pie-chart'));
  chart.draw(data, options);
}

function drawRegionsChart() {
  const data = google.visualization.arrayToDataTable([
    ['Country', 'Popularity'],
    ['Germany', 200],
    ['United States', 300],
    ['Brazil', 400],
    ['Canada', 500],
    ['France', 600],
    ['RU', 700]
  ]);

  const options = {
    'title': 'Sample Regions Chart',
    'width': 700,
    'height': 500
  };

  const chart = new google.visualization.GeoChart(document.getElementById('regions-chart'));
  chart.draw(data, options);
}

function drawCoronavirusChart() {
  fetch('/coronavirus-data').then(response => response.json())
    .then((coronavirusCases) => {
      const data = new google.visualization.DataTable();
      data.addColumn('string', 'Country');
      data.addColumn('number', 'Confirmed Cases');
      Object.keys(coronavirusCases).forEach((country) => {
        data.addRow([country, coronavirusCases[country]]);
      });

      const options = {
        'title': 'Coronavirus Cases',
        'width': 700,
        'height': 500,
        'colorAxis': { colors: ['lightcoral', 'darkred'] },
        'backgroundColor': 'lightblue',
        'datalessRegionColor': 'white',
        'defaultColor': 'lightcoral'
      };

      const chart = new google.visualization.GeoChart(document.getElementById('coronavirus-chart'));
      chart.draw(data, options);
    });
}

async function getLoginStatus() {
  const response = await fetch('/authentication');
  const json = await response.json();
  const userEmail = getEmail(json);
  const redirectUrl = getUrl(json);
  if (userEmail == 'N/A') {
    const html = loginHtml(redirectUrl);
    document.getElementById('login-status').innerHTML = html;
  } else {
    const html = logoutHtml(userEmail, redirectUrl);
    document.getElementById('login-status').innerHTML = html;
    document.getElementById('comments-form').style.display = "block";
  }
}

function getEmail(json) {
  return json.val0;
}

function getUrl(json) {
  return json.val1;
}

function loginHtml(loginUrl) {
  return "<p>Login <a href=\"" + loginUrl + "\">here</a>.";
}

function logoutHtml(userEmail, logoutUrl) {
  return "<p>Hi " + userEmail + "! Logout <a href=\"" + logoutUrl + "\">here</a>.";
}