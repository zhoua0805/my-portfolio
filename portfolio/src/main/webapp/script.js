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


//fecth two truths and one lie from the server
function getFacts() {
  fetch('/two-truths-one-lie').then(response => response.text()).then((TheInnerHTML) => {
    document.getElementById('game-container').innerHTML = TheInnerHTML;
    document.getElementById("game-result").innerText = ''
  });
}

const truths = ['I play the piano.', 
                    'I don\'t like pineapples on pizza.',
                    'I have never been skydiving before.', 
                    'I am hungry.', 
                    'I visited my friend in Manchester last Christmas.', ]; 
const lies = ['My favourite subject in high school was Calculus.',  
                'I have two brothers.',
                'I was born in the year of the tiger.']; 


function printGameResult() {
    //get selected item
    var result = '';
    if (document.getElementById('0').checked) {
        result = document.getElementById('0').value;
    }else if (document.getElementById('1').checked) {
        result = document.getElementById('1').value;
    }else  {
        result = document.getElementById('2').value;
    }
    console.log(result);

    //check and print result
    var TheInnerHTML = ''
    if (lies.includes(result)) {
        TheInnerHTML = 'Yay! You were right :)';
    }else {
        TheInnerHTML = 'Try again :)';
    }
    document.getElementById("game-result").innerText = TheInnerHTML;
}
