package com.mapboxweather.kamleshsahu.mapboxdemo.Interface;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

/**
 * Created by k on 4/30/2019.
 */

public interface myListener {

    void OnDragUpHeadLineChange();
    void MakerLayerIdListCreated(Boolean created);
    Boolean makerLayerIdListCreadted();
    void updateSelectedRoute(int id);
    DirectionsResponse getDirectionResp();
    int getSelectedRoute();
    String[] getLineLayerIds();

}
