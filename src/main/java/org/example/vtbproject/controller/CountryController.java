package org.example.vtbproject.controller;


import org.example.vtbproject.exception.ResourceNotFoundException;
import org.example.vtbproject.model.ActivateConfig;
import org.example.vtbproject.model.Country;
import org.example.vtbproject.model.DelayConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

    private final Map<Integer, Country> countryMap = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final DelayConfig delayConfig;
    private final ActivateConfig activateConfig;

    public CountryController(DelayConfig delayConfig, ActivateConfig activateConfig) {
        this.delayConfig = delayConfig;
        this.activateConfig = activateConfig;

        addNewCountry("Россия", "Москва");
        addNewCountry("Франция", "Париж");
    }


    private void simulateDelay() {
        int delay = delayConfig.getResponseDealy();
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void simulateActivate() {
        if (activateConfig.isActivated()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Сервер временно недоступен. Технические работы.");
        }
    }


    @GetMapping
    public Collection<Country> getAllCountries() {
        simulateActivate();
        simulateDelay();

        return countryMap.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable int id) {
        simulateActivate();
        simulateDelay();

        return ResponseEntity.ok(findCountry(id));
    }

    @PostMapping
    public ResponseEntity<Country> addCountry(@RequestBody Country newCountry) {
        simulateActivate();
        simulateDelay();

        return ResponseEntity.ok(addNewCountry(newCountry.country(), newCountry.city()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable int id, @RequestBody Country updatedCountry) {
        simulateActivate();
        simulateDelay();
        /*if (!countryMap.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }*/
        findCountry(id);

        countryMap.put(id, updatedCountry);
        return ResponseEntity.ok(updatedCountry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable int id) {
        simulateActivate();
        simulateDelay();

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