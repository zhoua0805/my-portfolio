
function initMap() {
    var coordin_wat = {lat: 43.4643, lng: -80.5204};
    var coordin_tor = {lat:43.6532, lng: -79.3832};
    var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 8,
    center: coordin_wat
    });

    var marker_wat = new google.maps.Marker({
    position: coordin_wat,
    map: map,
    title: 'I\'m here'
    });
    var marker_tor = new google.maps.Marker({
    position: coordin_tor,
    map: map,
    title: 'I\'m here'
    });
}
