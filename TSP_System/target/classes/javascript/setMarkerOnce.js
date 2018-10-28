var marker;
    map.on('click', function (e) {
        if(marker != null){
            map.removeLayer(marker);
        }
        var lat = e.latlng.lat;
        var long = e.latlng.lng;
        marker = L.marker([lat, long]).addTo(map);
    });