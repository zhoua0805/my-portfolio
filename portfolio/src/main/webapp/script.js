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


 // generate the project contents when the project.html document is loaded
window.onload = function generateProjectContent() {
      const projects = {
        "Accessible Mouse Design": ["Designed and prototyped a foot mouse with a pedal and a control base",
                                    "Created functional code using processing and java to satisfy all basic mouse functions including clicking, scrolling, and navigation",
                                    "Focused on accessibility and user experience",
                                    "Project won first place in the University of Toronto Designathon League"],
    
        "PASS Device": ["Designed an enhancement to the **Personal Alarm Safety System (PASS)** used by Toronto firefighters",
                                    "Created a device that prevents false alarms triggered by the existing system with a button embedded in the firefighters' gloves, which signals that they are conscious",
                                    "New system envisioned to use infraed signals to communicate between button and device --prototype was built using an Arduino and a motor that when triggered, causes rotatable arm to deactivate PASS alarm",
                                    "Conducted research with input from Toronto Fire Department to ensure safety standards were met"],
        "ESEC Website": ["Created and maintained a multi-page website for the Engineering Science Education Conference using WordPress and custom html/css",
                                    "Analyzed previous legacy code and utilized it as boilerplate infrastructure while updating source-code as required",
                                    "Applied concepts of responsive web design in order to make page accessible and user-freindly across all devices"], 
        "Matboard Construction Bridge Project": ["Constructed a ~1.5m long bridge using matboard only",
                                    "Applied knowledge of stress, shear, and buckling to optimize bridge load",
                                    "Acheived maximum load of 800N before failure"]                             
        };
      var TheInnerHTML ="";

      for (project in projects) {
          TheInnerHTML += "<h5>"+project+"</h5>";
          TheInnerHTML += "<ul>";
          for (i = 0; i < projects[project].length; i++) {
              TheInnerHTML += "<li style=\"list-style-type:circle\">"+projects[project][i]+"</li>";
          }
          TheInnerHTML += "</ul>";
    }
    document.getElementById("projectContent").innerHTML = TheInnerHTML;
}

//play a game of two truths and one lie
function playGame() {
    const truths = ['I play the piano.', 
                    'I don\'t like pineapples on pizza.',
                    'I have never been skydiving before.', 
                    'I am hungry.', 
                    'I visited my friend in Manchester last Christmas.', ]; 
    const lies = ['My favourite subject in high school was Calculus.',  
                    'I have two brothers.',
                    'I was born in the year of the tiger.']; 


    // generate two truths and one lie
    truth_1_index = Math.floor(Math.random() * truths.length);
    do {
        truth_2_index = Math.floor(Math.random() * truths.length);
    }while (truth_1_index === truth_2_index);
    
    lie_index = Math.floor(Math.random() * lies.length);
    twoTruthsOneLie = [truths[truth_1_index], truths[truth_2_index], lies[lie_index]];
    twoTruthsOneLie.sort() //sort the list so that order is randomized
    console.log (twoTruthsOneLie);

    // Add it to the page.
    var TheInnerHTML ="<form action=\"#\">";
    for (i = 0; i < twoTruthsOneLie.length; i++) {
        TheInnerHTML += "<label><input type=\"radio\" name =\"group1\" /><span class = \"black-text\">"+ twoTruthsOneLie[i] + "</span></label><br>";
    }
    TheInnerHTML += "</form>";
    document.getElementById("game-container").innerHTML = TheInnerHTML;
}
