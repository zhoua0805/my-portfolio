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
    const COORDINATES_WATERLOO = {lat: 43.4643, lng: -80.5204};
    const COORDINATES_TORONTO = {lat:43.6532, lng: -79.3832};
    let map = new google.maps.Map(document.getElementById('map'), {
    zoom: 4,
    center: COORDINATES_WATERLOO
    });

    const MARKER_WATERLOO = new google.maps.Marker({
    position: COORDINATES_WATERLOO,
    map: map,
    title: 'Waterloo'
    });
    let MARKER_TORONTO = new google.maps.Marker({
    position: COORDINATES_TORONTO,
    map: map,
    title: 'Toronto'
    });
    addlocations(map);
}


//fetch comments from the server
function addlocations(map) {
    fetch('/comments').then(response => response.json()).then((comments) => {
        console.log(comments);
        comments.forEach((comment) => {
            var i = 0;
            
            var contentString = '<h5>' + comment.name + '</h5>' +
                        '<p>' + comment.content + '</p> </div>' +
                        '<button onclick=\"deleteMarker('+ comment.id +')\"> \
                            Delete </button>';
            var infowindow = new google.maps.InfoWindow({
                content: contentString
            });

            let marker = new google.maps.Marker({
                position: {lat: comment.lat, lng: comment.lng},
                map: map,
            });
            marker.addListener('click', function() {
                if (i%2 === 0) {
                    infowindow.open(map, marker);
                }else{
                    infowindow.close();
                }
                i++;
            });
        });
  });
}

async function deleteMarker(id) {
    const params = new URLSearchParams();
    params.append("id", id);

    //delete the comment from datastore
    await fetch('/delete-comment', {method: 'POST', body: params});
    initMap();
}



