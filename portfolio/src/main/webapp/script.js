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

// Fetches stored comments from the server and adds it to the DOM
function getCommentForm() {
  fetch('/comment-form').then(response => response.json()).then((comments) => {
    document.getElementById('comment-container').innerHTML = comments;
  });
}
