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
    var bounds = new google.maps.LatLngBounds();
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: coordin_wat
    });

    var input = document.getElementById("search-input");
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    //reset bounds if places returned are out of view
    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });
    
    var searchMarker = new google.maps.Marker();

    //add a marker to the first result returned
    searchBox.addListener('places_changed', function() {
        var places = searchBox.getPlaces();
        console.log(places);
        if (places.length ==0 ) {
            return;
        }
        place = places[0]
        if (!place.geometry) {
            console.log("The place has no geometry!");
            return;
        }

        searchMarker.setMap(map);
        searchMarker.setTitle(place.name);
        searchMarker.setPosition(place.geometry.location);
     
        //set bounds (some places don't have viewport)
        if (place.geometry.viewport) {
            bounds.union(place.geometry.viewport);
        } else {
            bounds.extend(place.geometry.location);
        }
        map.fitBounds(bounds);

        //set form values to the position of the place
        document.getElementById("lat").value = searchMarker.getPosition().lat();
        document.getElementById("lng").value = searchMarker.getPosition().lng();
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
            var i = 0;
            
            var contentString = '<h5>' + comment.name + '</h5>' +
                        '<p>' + comment.content + '</p> </div>' +
                        '<button onclick=\"deleteMarker('+ comment.id +')\"> \
                            Delete </button>';
            var infowindow = new google.maps.InfoWindow({
                content: contentString
            });

            var marker = new google.maps.Marker({
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
    window.location.reload(true); 
}



