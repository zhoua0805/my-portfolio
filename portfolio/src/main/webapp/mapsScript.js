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

// This function has code that is referenced from the GoogleMaps Platform
// Documentation: https://developers.google.com/maps/documentation/javascript/tutorial


function initMap(){
    const COORDINATES_WATERLOO = {lat: 43.4643, lng: -80.5204};
    const COORDINATES_TORONTO = {lat:43.6532, lng: -79.3832};

    let map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: COORDINATES_WATERLOO
    });

    const MARKER_WATERLOO = new google.maps.Marker({
        position: COORDINATES_WATERLOO,
        map: map,
        title: 'Waterloo'
    });
    const MARKER_TORONTO = new google.maps.Marker({
        position: COORDINATES_TORONTO,
        map: map,
        title: 'Toronto'
    });
    let displayBounds = new google.maps.LatLngBounds();
    displayBounds.extend(COORDINATES_WATERLOO);
    displayBounds.extend(COORDINATES_TORONTO);
    
    let input = document.getElementById("search-input");
    let searchBox = new google.maps.places.SearchBox(input);

    //reset bounds if places returned are out of view
    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });
    
    let searchMarker = new google.maps.Marker();
    //add a marker to the first result returned
    searchBox.addListener('places_changed', function() {
        let places = searchBox.getPlaces();
        let searchBounds = new google.maps.LatLngBounds();
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
            searchBounds.union(place.geometry.viewport);
        } else {
            searchBounds.extend(place.geometry.location);
        }
        map.fitBounds(searchBounds);

        //set form values to the position of the place
        document.getElementById("lat").value = searchMarker.getPosition().lat();
        document.getElementById("lng").value = searchMarker.getPosition().lng();
    });
    addlocations(map, displayBounds);
}


//fetch comments from the server
function addlocations(map, bounds) {
    fetch('/comments?options='+ getFilterOptions()).then(response => response.json()).then((comments) => {
        console.log(comments);
        comments.forEach((comment) => {
            let i = 0;
            let contentString = '<h5>' + comment.name + '</h5>' +
                        '<p>' + comment.content + '</p> </div>' +
                        '<button onclick=\"deleteMarker('+ comment.id +')\"> \
                            Delete </button>';
            let infowindow = new google.maps.InfoWindow({
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
            
            bounds.extend(marker.getPosition()); 
            map.fitBounds(bounds);
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

function getFilterOptions(){
    options= '';
    let selected = document.getElementsByClassName('active');
    for (let i = 0; i< selected.length; i++){
        options += selected[i].name + ' ';
    }
    return options;
}


