package com.samsunghack.apps.apis;

import java.util.ArrayList;

public class GooglePlacesData {
	private GooglePlaces mGooglePlaces;
	private ArrayList<GooglePlaces> mGooglePlacesDataList;

	public GooglePlacesData() {
		super();
		mGooglePlacesDataList = new ArrayList<GooglePlaces>();
	}

	public ArrayList<GooglePlaces> getFeatuersData() {
		return mGooglePlacesDataList;
	}

	public void addGooglePlacesData(String name, String vicinity, String type,
			String lattitude, String longitude, String icon, String reference) {
		mGooglePlaces = new GooglePlaces(name, vicinity, type, lattitude,
				longitude, icon, reference);

		mGooglePlacesDataList.add(mGooglePlaces);
	}

	public class GooglePlaces {
		String name;
		String vicinity;
		String type;
		String lattitude;
		String longitude;
		String icon;
		String reference;

		public GooglePlaces(String name, String vicinity, String type,
				String lattitude, String longitude, String icon,
				String reference) {
			super();
			this.name = name;
			this.vicinity = vicinity;
			this.type = type;
			this.lattitude = lattitude;
			this.longitude = longitude;
			this.icon = icon;
			this.reference = reference;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVicinity() {
			return vicinity;
		}

		public void setVicinity(String vicinity) {
			this.vicinity = vicinity;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getLattitude() {
			return lattitude;
		}

		public void setLattitude(String lattitude) {
			this.lattitude = lattitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

	}

}
