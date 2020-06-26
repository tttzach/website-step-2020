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

// Next/previous controls for slideshow under personal tab
var plusSlides = (function () {
  var slideIndex = 1;
  return function(offset) {
    slideIndex += offset;
    const slides = document.getElementsByClassName("slide");
    if (slideIndex > slides.length) {
      slideIndex = 1;
    }
    if (slideIndex < 1) {
      slideIndex = slides.length;
    }
    for (var slide of slides) {
      slide.style.display = "none";
    }
    slides[slideIndex-1].style.display = "block";
    return slideIndex;
  };
})();
