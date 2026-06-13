package org.example.vtbproject.controller;


import org.example.vtbproject.exception.ResourceNotFoundException;
import org.example.vtbproject.model.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

    private final Map<Integer, Country> countryMap = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    public CountryController() {

        addNewCountry("Россия", "Москва");
        addNewCountry("Франция", "Париж");
    }

    @GetMapping
    public Collection<Country> getAllCountries() {
        return countryMap.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        return ResponseEntity.ok(findCountry(id));
    }

    @PostMapping
    public ResponseEntity<Country> addCountry(@RequestBody Country newCountry) {
        return ResponseEntity.ok(addNewCountry(newCountry.country(), newCountry.city()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable int id, @RequestBody Country updatedCountry) {
        /**if (!countryMap.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }*/
        findCountry(id);

        countryMap.put(id, updatedCountry);
        return ResponseEntity.ok(updatedCountry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id) {
        findCountry(id);

        countryMap.remove(id);
        return ResponseEntity.noContent().build();
    }



    private Country findCountry(int id) {
        if (!countryMap.containsKey(id)) {
            throw new ResourceNotFoundException("Страны с id " + id + " не найдено");
        }
        return countryMap.get(id);
    }

    private Country addNewCountry(String country, String city) {
        int newId = idGenerator.incrementAndGet();
        Country newCountry = new Country(country, city);
        countryMap.put(newId, newCountry);
        return newCountry;
    }







}
