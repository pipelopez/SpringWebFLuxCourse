package com.example.demo.service.impl;


import com.example.demo.controller.dto.CarDto;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.entity.CarEntity;
import com.example.demo.service.CarService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

	@Autowired
    private CarRepository carRepository;

    @Override
    public Mono<CarDto> getCar(Integer carId) {
        return carRepository.findById(carId)
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<CarDto> createCar(CarDto carDto) {
    	CarEntity carEnt = new CarEntity(carDto.id(), carDto.brand(), carDto.kilowatt());
        return carRepository.save(carEnt)
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<CarDto> updateCar(Integer carId, CarDto carDto) {
    	CarEntity carEnt = new CarEntity(carDto.id(), carDto.brand(), carDto.kilowatt());
        return carRepository.save(carEnt)
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<Void> deleteCar(Integer carId) {
        return carRepository.deleteById(carId);
    }

    @Override
    public Flux<CarDto> getAllCars() {
        return carRepository.findAll()
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }
}
