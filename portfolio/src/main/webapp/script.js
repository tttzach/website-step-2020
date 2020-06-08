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

// Hide all elements with class="containerTab", except for the one that matches the clickable grid column
function openTab(tabName) {
  const containerTabs = document.getElementsByClassName("containerTab");
  for (var containerTab of containerTabs) {
    containerTab.style.display = "none";
  }
  document.getElementById(tabName).style.display = "inline-block";
}

// Next/previous controls for slideshow under personal tab
var plusSlides = (function (offset) {
  var slideIndex = 1
  return function(offset) {
    slideIndex += offset;
    const slides = document.getElementsByClassName("slide");
    if (slideIndex > slides.length) {slideIndex = 1}
    if (slideIndex < 1) {slideIndex = slides.length}
    for (var slide of slides) {
      slide.style.display = "none";
    }
    slides[slideIndex-1].style.display = "block";
    return slideIndex;
  }
})();

// Fetches a hard-coded JSON string from the server and adds it as a greeting to the DOM
async function getGreeting() {
  const response = await fetch('/data');
  const json = await response.json();
  const greeting = jsonToHtml(json);
  document.getElementById('greeting-container').innerHTML = greeting;
}

function jsonToHtml(podmates) {
  var html = "<h1>Hello ";
  const lastIndex = podmates.length - 1;
  for (const [index, podmate] of podmates.entries()) {
    html += index == lastIndex ? " and "
            : index != 0 ? ", "
            : "";
    html += podmate;
  }
  html += "!</h1>";
  return html;
}

// Fetches comments from the server and adds it to the DOM
function getCommentForm() {
  fetch('/comment-form')
    .then(response => response.json())
    .then((comments) => {
      document.getElementById('comment-container').innerHTML = comments;
    });
}

// Fetches comments from the datastore and adds them to the DOM
function loadComments(maxInt) {
  const max = maxInt.toString();
  fetch('/list-comments?max=' + max)
    .then(response => response.json())
    .then(comments => {
      const commentListElement = document.getElementById('comments-list');
      commentListElement.innerHTML = "";
      comments.forEach(comment => {
        commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

// Creates an element that represents a comment
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const textElement = document.createElement('span');
  textElement.innerText = comment;

  commentElement.appendChild(textElement);
  return commentElement;
}

// Creates a terrain map with a marker and adds it to the page
function createMap() {
  const uWaterloo = {lat: 43.473, lng: -80.545};
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
  const contentString = '<div id="infowindow">'+
        '<div id="siteNotice">'+
        '</div>'+
        '<h1 id="firstHeading" class="firstHeading">University of Waterloo</h1>'+
        '<div id="bodyContent">'+
        '<p>The <b>University of Waterloo</b> (commonly referred to as <b>Waterloo</b>, <b>UW</b>, or '+
        '<b>UWaterloo</b>) is a public research university with a main campus in Waterloo, Ontario, '+
        'Canada. The main campus is on 404 hectares of land adjacent to Uptown Waterloo and Waterloo '+
        'Park. The university also operates three satellite campuses and four affiliated university '+
        'colleges.</p>'+
        '<p>Attribution: University of Waterloo, <a href="https://en.wikipedia.org/wiki/University_of_Waterloo">'+
        'https://en.wikipedia.org/wiki/University_of_Waterloo</a> '+
        '(last visited June 8, 2020).</p>'+
        '</div>'+
        '</div>';
  const infoWindow = new google.maps.InfoWindow({
    content: contentString
  });
  marker.addListener('click', function() {
    infoWindow.open(map, marker);
  });
}
