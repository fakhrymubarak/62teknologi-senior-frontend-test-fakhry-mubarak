package com.fakhry.businessapp.presentation.dashboard.model

data class FilterBusiness(
    val id: Int,
    val name: String,
    val value: String,
    var isActive: Boolean,
)

fun List<FilterBusiness>.toListString(): List<String> {
    return this.filter { it.isActive }.map { it.value }
}

/**
 * hot_and_new - popular businesses which recently joined Yelp
 * request_a_quote - businesses which actively reply to Request a Quote inquiries
 * reservation - businesses with Yelp Reservations bookings enabled on their profile page
 * waitlist_reservation - businesses with Yelp Wait List bookings enabled on their profile screen (iOS/Android)
 * deals - businesses offering Yelp Deals on their profile page
 * gender_neutral_restrooms - businesses which provide gender neutral restrooms
 * open_to_all - businesses which are Open To All
 * wheelchair_accessible - businesses which are Wheelchair Accessible
 * Premium Access Tier attributes:
 * liked_by_vegetarians - businesses which are liked by vegetarians
 * outdoor_seating - businesses with outdoor seating areas
 * parking_garage - businesses which itself has a garage or there is a parking garage nearby
 * parking_lot - businesses which have a parking lot
 * parking_street - businesses with street parking available nearby
 * parking_valet - businesses which offer a valet parking
 * parking_validated - businesses which can validate a parking ticket from an external parking
 * parking_bike - businesses with bike parking type
 * restaurants_delivery - restaurants which offer delivery service
 * restaurants_takeout - restaurants with take-out option
 * wifi_free - businesses with free WiFi
 * wifi_paid - businesses with paid WiFi
 *
 * */
fun getDefaultFilters() = listOf(
    FilterBusiness(0, "Nearest", "", false),
    FilterBusiness(1, "Hot and New", "hot_and_new", false),
    FilterBusiness(2, "Request Quote", "request_a_quote", false),
    FilterBusiness(3, "Reservation", "reservation", false),
    FilterBusiness(4, "Wait List", "waitlist_reservation", false),
    FilterBusiness(5, "Deals", "deals", false),
    FilterBusiness(6, "Neutral Restroom", "gender_neutral_restrooms", false),
    FilterBusiness(7, "Open To All", "open_to_all", false),
    FilterBusiness(8, "Wheelchair Accessible", "wheelchair_accessible", false),
    FilterBusiness(9, "Vegetarians", "liked_by_vegetarians", false),
    FilterBusiness(10, "Outdoor", "outdoor_seating", false),
    FilterBusiness(11, "Garage Parking", "parking_garage", false),
    FilterBusiness(12, "Parking Lot", "parking_lot", false),
    FilterBusiness(13, "Parking Street", "parking_street", false),
    FilterBusiness(14, "Parking Valet", "parking_valet", false),
    FilterBusiness(15, "Parking Validated", "parking_validated", false),
    FilterBusiness(16, "Parking Bike", "parking_bike", false),
    FilterBusiness(17, "Restaurant Delivery", "restaurants_delivery", false),
    FilterBusiness(18, "Restaurant Takeout", "restaurants_takeout", false),
    FilterBusiness(19, "Free Wifi", "wifi_free", false),
    FilterBusiness(20, "Paid Wifi", "wifi_paid", false),
)