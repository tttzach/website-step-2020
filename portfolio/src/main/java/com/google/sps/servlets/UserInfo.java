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

public class UserInfo {

  private String email;
  private String loginOrLogoutUrl;
  private boolean loggedIn;

  UserInfo(String email, String loginOrLogoutUrl, boolean loggedIn) {
    this.email = email;
    this.loginOrLogoutUrl = loginOrLogoutUrl;
    this.loggedIn = loggedIn;
  }

  public String getEmail(){
    return email;
  }

  public String getLoginOrLogoutUrl(){
    return loginOrLogoutUrl;
  }

  public boolean isLoggedIn() {
    return loggedIn;
  }

}
