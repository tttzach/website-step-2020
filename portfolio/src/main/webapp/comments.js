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

// Fetches comments from the server and adds it to the DOM
function getCommentForm() {
  fetch('/comment-form')
    .then(response => response.json())
    .then((comments) => {
      document.getElementById('comment-container').innerHTML = comments;
    });
}

// Fetches comments from the datastore and adds them to the DOM
function loadComments(maxInt, language) {
  const max = maxInt.toString();
  fetch('/list-comments?max=' + max + '&language=' + language)
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
