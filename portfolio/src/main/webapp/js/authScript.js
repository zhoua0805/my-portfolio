// Copyright 2020 Google LLC
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


//Get auththentication link
function authenticate() {
    fetch('/auth').then(response => response.json()).then(auth => {
        const navbarContent = document.getElementById("web-navbar");
        const navbarContentMobile = document.getElementById("mobile-demo");

        //If the user is logged in, show the comment form.
        if (auth.Loggedin) {
            navbarContent.innerHTML += '<li><a class="waves-light btn" href="'
                                        + auth.url +'">Log out</a> </li>';
            navbarContentMobile.innerHTML += '<li> <a class="waves-light btn" href="'
                                        + auth.url +'">Log out</a> </li>';

            const commentForm = document.getElementById("input-comments");
            commentForm.style.display = "block";
        }else{
            navbarContent.innerHTML += '<li> <a class="waves-light btn" href="'
                                        + auth.url +'">Log in</a> </li>';
            navbarContentMobile.innerHTML += '<li> <a class="waves-light btn" href="'
                                        + auth.url +'">Log in</a> </li>';
        }

    });
}

      
