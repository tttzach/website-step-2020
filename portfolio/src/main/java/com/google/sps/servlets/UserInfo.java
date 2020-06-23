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

/**
 * Stores user information for authentication purposes.
 */
public class UserInfo {

  private String email;
  private String loginUrl;
  private String logoutUrl;
  private boolean loggedIn;

  /**
   * Store relevant logout information when user is logged in
   * @param email
   * @param logoutUrl
   */
  public void loggedIn(String email, String logoutUrl) {
    this.email = email;
    this.logoutUrl = logoutUrl;
    this.loggedIn = true;
  }

  /**
   * Store relevant login information when user is logged out
   * @param loginUrl
   */
  public void loggedOut(String loginUrl) {
    this.loginUrl = loginUrl;
    this.loggedIn = false;
  }

  /**
   * @return user email
   */
  public String getEmail(){
    return email;
  }

  /**
   * @return url to log user in
   */
  public String getLoginUrl(){
    return loginUrl;
  }

  /**
   * @return url to log user out
   */
  public String getLogoutUrl(){
    return logoutUrl;
  }

  /**
   * @return user login status
   */
  public boolean isLoggedIn() {
    return loggedIn;
  }

}
