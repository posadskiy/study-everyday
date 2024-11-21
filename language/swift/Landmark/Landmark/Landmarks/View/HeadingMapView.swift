//
//  MapView.swift
//  Landmark
//
//  Created by Dmitrii on 15.11.2022.
//

import SwiftUI
import MapKit

struct HeadingMapView: View {
    var coordinate: CLLocationCoordinate2D
    @State private var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 46.0359, longitude: 14.5008), span: MKCoordinateSpan(latitudeDelta: 0.1, longitudeDelta: 0.1))

    var body: some View {
        Map(coordinateRegion: $region)
            .onAppear {
                setRegion(coordinate)
            }
    }
    
    private func setRegion(_ coordinate: CLLocationCoordinate2D) {
        region = MKCoordinateRegion(
            center: coordinate,
            span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2)
        )
    }
}

struct HeadingMapView_Previews: PreviewProvider {
    static var previews: some View {
        HeadingMapView(coordinate: CLLocationCoordinate2D(latitude: 46.0359, longitude: 14.5008))
    }
}
