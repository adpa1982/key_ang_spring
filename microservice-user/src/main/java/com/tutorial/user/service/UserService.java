package com.tutorial.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tutorial.user.entity.User;
import com.tutorial.user.feingclients.BikeFeignClient;
import com.tutorial.user.feingclients.CarFeignClient;
import com.tutorial.user.model.Bike;
import com.tutorial.user.model.Car;
import com.tutorial.user.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
    RestTemplate restTemplate;
	
	@Autowired
	CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;
	
	public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        User userNew = userRepository.save(user);
        return userNew;
    }
    
    public List<Car> getCars(Long userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt.getTokenValue());
        ResponseEntity<List> cars = restTemplate.exchange("http://car-service/car/byuser/" + userId, HttpMethod.GET, new HttpEntity<>(httpHeaders), List.class);
        return cars.getBody();
    }

    public List<Bike> getBikes(Long userId) {        
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt.getTokenValue());
        ResponseEntity<List> bikes = restTemplate.exchange("http://bike-service/bike/byuser/" + userId, HttpMethod.GET, new HttpEntity<>(httpHeaders), List.class);
        return bikes.getBody();
    }
    
    /*
     * Feing
     * */
    
    public Car saveCar(Long userId, Car car) {
        car.setUserId(userId);
        Car carNew = carFeignClient.save(car);
        return carNew;
    }

    public Bike saveBike(Long userId, Bike bike) {
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }
    
    public Map<String, Object> getUserAndVehicles(Long userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            result.put("Mensaje", "no existe el usuario");
            return result;
        }
        result.put("User", user);
        
        List<Car> cars = carFeignClient.getCars(userId);
        if(cars.isEmpty())
            result.put("Cars", "ese user no tiene coches");
        else
            result.put("Cars", cars);
        
        List<Bike> bikes = bikeFeignClient.getBikes(userId);
        if(bikes.isEmpty())
            result.put("Bikes", "ese user no tiene motos");
        else
            result.put("Bikes", bikes);
        return result;
    }
	

}
