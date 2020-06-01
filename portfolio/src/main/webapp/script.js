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
    fetch('/two-truths-one-lie').then(response => response.json()).then((facts) => {
        console.log(facts);
        const divElement = document.createElement('div');
        const brElement = document.createElement('br');

        const gameContainer = document.getElementById('game-container');
        gameContainer.innerHTML = '';
        divElement.appendChild(
            createLabelElement(facts[0], "0"));
        divElement.appendChild(document.createElement('br'));
        divElement.appendChild(
            createLabelElement(facts[1], "1"));
        divElement.appendChild(document.createElement('br'));
        divElement.appendChild(
            createLabelElement(facts[2], "2"));
        divElement.appendChild(document.createElement('br'));

        gameContainer.appendChild(divElement);

        document.getElementById("game-result").innerText = '';
  });
}


function createLabelElement(text, id) {
    const labelElement = document.createElement('label');

    const inputElement = document.createElement('input');
    inputElement.setAttribute("type", "radio");
    inputElement.setAttribute("name", "group1");
    inputElement.setAttribute("value", text);
    inputElement.setAttribute("id", id);
    inputElement.setAttribute("onchange", "printGameResult()");

    const spanElement = document.createElement('span');
    spanElement.innerText= text;
    spanElement.setAttribute("style", "color: #410219");

    labelElement.appendChild(inputElement);
    labelElement.appendChild(spanElement);
    return labelElement;
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
