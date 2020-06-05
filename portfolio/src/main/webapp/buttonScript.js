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

function submitFilters() {
    initMap(); 
}

//add onclick function to the buttons
function selectFilters() {
    let filters = document.getElementsByClassName("filter-btn");
    for (let i = 0; i< filters.length; i++){
            filters[i].addEventListener("click", function(){
            if (this.className.includes(" active")){
                this.className = this.className.replace(" active", "");
            }else {
                this.className += " active";
            }  
        });   
    }
}
