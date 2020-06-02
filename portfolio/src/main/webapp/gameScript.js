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


//fecth two truths and one lie from the server
function getFacts() {
    fetch('/two-truths-one-lie').then(response => response.json()).then((game) => {
        facts = game.twoTruthsOneLie;
        console.log(facts);
        const formElement = document.createElement('form');
        formElement.setAttribute("action", "/two-truths-one-lie");
        formElement.setAttribute("method", "POST");

        const gameContainer = document.getElementById('game-container');
        gameContainer.innerHTML = '';
        formElement.appendChild(
            createLabelElement(facts[0], "0"));
        formElement.appendChild(document.createElement('br'));
        formElement.appendChild(
            createLabelElement(facts[1], "1"));
        formElement.appendChild(document.createElement('br'));
        formElement.appendChild(
            createLabelElement(facts[2], "2"));
        formElement.appendChild(document.createElement('br'));
        formElement.appendChild(document.createElement('br'));
        formElement.innerHTML += "<button class=\"btn waves-effect waves-light\"  \
            type=\"submit\" name=\"action\">Submit </button>";
        
        gameContainer.appendChild(formElement);
        document.getElementById("game-result").innerText = game.result;
        
  });
}


function createLabelElement(text, id) {
    const labelElement = document.createElement('label');

    const inputElement = document.createElement('input');
    inputElement.setAttribute("type", "radio");
    inputElement.setAttribute("name", "group1");
    inputElement.setAttribute("value", text);
    inputElement.setAttribute("id", id);

    const spanElement = document.createElement('span');
    spanElement.innerText= text;
    spanElement.setAttribute("style", "color: #410219");

    labelElement.appendChild(inputElement);
    labelElement.appendChild(spanElement);
    return labelElement;
}
