package edu.pharmacy.controller;

import edu.pharmacy.model.dto.OrderFullInfoDTO;
import edu.pharmacy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderFullInfoDTO create(@RequestBody String orderJson) {
        return orderService.create(orderJson);
    }

    @GetMapping
    public List<OrderFullInfoDTO> getAll() {
        return orderService.getAll();
    }

    @GetMapping(value = "{id}")
    public OrderFullInfoDTO get(@PathVariable("id") long id) {
        return orderService.get(id);
    }


    @DeleteMapping(value = "{id}")
    public long delete(@PathVariable("id") long id) {
        return orderService.delete(id);
    }
}
