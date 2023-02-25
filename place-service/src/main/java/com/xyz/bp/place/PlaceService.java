package com.xyz.bp.place;

import io.reactivex.rxjava3.core.Single;

import java.util.List;

public interface PlaceService {
    Single<List<Place>> listCities(String searchStr);

    Single<Place> createCity(Place place);
}
