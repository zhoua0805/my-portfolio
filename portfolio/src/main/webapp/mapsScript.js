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


function initMap() {
    var coordin_wat = {lat: 43.4643, lng: -80.5204};
    var coordin_tor = {lat:43.6532, lng: -79.3832};
    var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 4,
    center: coordin_wat
    });

    var marker_wat = new google.maps.Marker({
    position: coordin_wat,
    map: map,
    title: 'Click me'
    });
    var marker_tor = new google.maps.Marker({
    position: coordin_tor,
    map: map,
    title: 'Click me'
    });
    addlocations(map);
}


//fecth comments from the server
function addlocations(map) {
    fetch('/comments').then(response => response.json()).then((comments) => {
        console.log(comments);
        comments.forEach((comment) => {
            var contentString = '<h5>' + comment.name + '</h5>' +
                                '<p>' + comment.content + '</p>';
            var infowindow = new google.maps.InfoWindow({
                content: contentString
            });

            var marker = new google.maps.Marker({
                position: {lat: comment.lat, lng: comment.lng},
                map: map,
            });
            marker.addListener('mouseover', function() {
                infowindow.open(map, marker);
            });
            marker.addListener('mouseout', function() {
                infowindow.close();
            });
        });
  });
}



