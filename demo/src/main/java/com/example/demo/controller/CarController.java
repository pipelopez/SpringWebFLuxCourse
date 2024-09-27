package com.example.demo.controller;

import com.example.demo.controller.dto.CarDto;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/car")
public class CarController {

    private static final Logger log = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;

    @GetMapping("/{carId}")
    Mono<ResponseEntity<CarDto>> getCar(@PathVariable("carId") Integer carId) {
        return carService.getCar(carId)
                .map(car -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(car)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    Mono<ResponseEntity<Map<String, Object>>> createCar(@Valid @RequestBody Mono<CarDto> carDto) {

        Map<String, Object> respuesta = new HashMap<String, Object>();

        return carDto.flatMap(carService::createCar)
                .flatMap(car -> {
                    respuesta.put("car", car);
                    respuesta.put("timestamp", new Date());
                    respuesta.put("status", HttpStatus.CREATED.value());
                    return Mono.just(ResponseEntity.created(URI.create("/car/" + car.id()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(respuesta));
                })
                .onErrorResume(t -> Mono.just(t).cast(WebExchangeBindException.class)
                        .flatMap(e -> Mono.just(e.getFieldErrors()))
                        .flatMapMany(Flux::fromIterable)
                        .map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> {
                            respuesta.put("errors", list);
                            respuesta.put("timestamp", new Date());
                            respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                            return Mono.just(ResponseEntity.badRequest().body(respuesta));
                        }));
    }

    @PutMapping("/{carId}")
    Mono<ResponseEntity<CarDto>> updateCar(@PathVariable("carId") Integer carId, @RequestBody CarDto carDto) {
        return carService.getCar(carId).flatMap(car -> carService.updateCar(car.id(), carDto))
                .map(car -> ResponseEntity
                        .created(URI.create("/car/" + car.id()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(car)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{carId}")
    Mono<ResponseEntity<Void>> deleteCar(@PathVariable("carId") Integer carId) {
        return carService.getCar(carId).flatMap(car -> carService.deleteCar(car.id())
                        .then(Mono.just(ResponseEntity.noContent().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    Mono<ResponseEntity<Flux<CarDto>>> getAllCars() {
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(carService.getAllCars()));
    }
}
