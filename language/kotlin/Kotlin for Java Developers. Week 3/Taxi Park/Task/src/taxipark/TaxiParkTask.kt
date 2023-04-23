package taxipark

import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.filter { driver -> this.trips.all { trip -> !trip.driver.name.equals(driver.name) } }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers.filter { passenger -> this.trips.count { trip -> trip.passengers.any { tripPassenger -> passenger.name.equals(tripPassenger.name) } } >= minTrips }.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.allPassengers.filter { passenger -> this.trips.count { trip -> trip.passengers.any { tripPassenger -> passenger.name.equals(tripPassenger.name) } && trip.driver.name.equals(driver.name) } > 1 }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            val trips = this.trips.filter { trip -> trip.passengers.any { tripPassenger -> passenger.name.equals(tripPassenger.name) } }.partition {
                if (it.discount == null) {
                    false
                } else {
                    it.discount > 0
                }
            }
            trips.first.size > trips.second.size
        }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (this.trips.isEmpty()) return null
    val max = (0..50).maxBy { duration -> this.trips.count { it.duration in duration*10..duration*10+9 } }
    if (max == null) return null
    return IntRange(max * 10, max * 10 + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val map = this.allDrivers.map { driver -> driver.name to this.trips.filter { it.driver.name == driver.name }.map { it.cost }.sum() }.sortedByDescending { it.second }
    val sumSuccessful = map.take((map.size * 0.2).roundToInt()).map { it.second }.sum()
    val allSum = map.map { it.second }.sum()
    return sumSuccessful / allSum >= 0.8 - 0.00001
}