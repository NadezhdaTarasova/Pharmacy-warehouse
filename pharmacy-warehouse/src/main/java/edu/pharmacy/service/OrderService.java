package edu.pharmacy.service;

import edu.pharmacy.dao.*;
import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.OrderFullInfoDTO;
import edu.pharmacy.model.dto.OrderItemDTO;
import edu.pharmacy.model.dto.OrderToCreateDTO;
import edu.pharmacy.model.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService extends AbstractService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final PharmacyRepository pharmacyRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PharmacyService pharmacyService;
    private final ProductService productService;
    private final EmployeeService employeeService;

    @SneakyThrows
    public OrderFullInfoDTO create(String orderToCreateJson) {
        OrderToCreateDTO orderToCreateDTO = objectMapper.readValue(orderToCreateJson, OrderToCreateDTO.class);
        long pharmacyId = orderToCreateDTO.getPharmacyId();
        pharmacyService.checkExistence(pharmacyId);
        Pharmacy pharmacy = pharmacyRepository.getById(pharmacyId);
        long employeeId = orderToCreateDTO.getEmployeeId();
        employeeService.checkExistence(employeeId);
        Employee employee = employeeRepository.getById(employeeId);
        Order order = new Order();
        order.setPharmacy(pharmacy);
        order.setDate(LocalDate.now());
        order.setTime(LocalTime.now());
        Order createdOrder = orderRepository.save(order);
        List<OrderItemDTO> orderItemDTOS = orderToCreateDTO.getProductsAndAmounts();
        saveOrdersProducts(orderItemDTOS, createdOrder, employee);
        saveWarehousesProducts(orderItemDTOS, pharmacy);
        return new OrderFullInfoDTO();
    }

    private void transferMoneyForOrder(Pharmacy pharmacy, List<OrderItemDTO> orderItemDTOS) {
        if (pharmacy.getAccount() == null) {
            throw new BusinessLogicException("It is impossible to create an order because the pharmacy does not have a account");
        }
        for (OrderItemDTO orderItem : orderItemDTOS) {
            long productId = orderItem.getProductId();
            productService.checkExistence(productId);
            Product product = productRepository.getById(productId);
            Supplier supplier = product.getSupplier();
            if (supplier.getAccount() == null) {
                throw new BusinessLogicException("It is impossible to create an order because the supplier does not have a account");
            }
            int amount = orderItem.getAmount();
            if (amount < 1) throw new BusinessLogicException("Incorrect amount of product");
            double price = product.getPrice();
            double fullSum = price * amount;
            Account pharmacyAccount = pharmacy.getAccount();
            double pharmacyAccountBalance = pharmacyAccount.getBalance();
            Account supplierAccount = supplier.getAccount();
            double supplierAccountBalance = supplierAccount.getBalance();
            if (pharmacyAccountBalance < fullSum) {
                throw new BusinessLogicException("It is impossible to create an order because the pharmacy does not have enough funds to pay for the order");
            }
            pharmacyAccount.setBalance(pharmacyAccountBalance - fullSum);
            supplierAccount.setBalance(supplierAccountBalance + fullSum);
            pharmacyRepository.save(pharmacy);
            supplierRepository.save(supplier);
        }
    }

    private void saveOrdersProducts(List<OrderItemDTO> orderItemDTOS, Order order, Employee employee) {
        for (OrderItemDTO orderItem : orderItemDTOS) {
            OrderProduct orderProduct = new OrderProduct();
            long productId = orderItem.getProductId();
            productService.checkExistence(productId);
            Product product = productRepository.getById(productId);
            orderProduct.setProduct(product);
            int amount = orderItem.getAmount();
            if (amount < 1) throw new BusinessLogicException("Incorrect amount of product");
            orderProduct.setAmount(amount);
            orderProduct.setOrder(order);
            orderProduct.setEmployee(employee);
            orderProductRepository.save(orderProduct);
        }
    }

    private void saveWarehousesProducts(List<OrderItemDTO> orderItemDTOS, Pharmacy pharmacy) {
        if (pharmacy.getWarehouse() == null)
            throw new BusinessLogicException("It is impossible to create an order because the pharmacy does not have a warehouse");
        Warehouse warehouse = warehouseRepository.getByPharmacyId(pharmacy.getId());
        for (OrderItemDTO orderItem : orderItemDTOS) {
            WarehouseProduct warehouseProduct = new WarehouseProduct();
            long productId = orderItem.getProductId();
            productService.checkExistence(productId);
            Product product = productRepository.getById(productId);
            warehouseProduct.setProduct(product);
            int amount = orderItem.getAmount();
            if (amount < 1) throw new BusinessLogicException("Incorrect amount of product");
            warehouseProduct.setAmount(amount);
            warehouseProduct.setWarehouse(warehouse);
            warehouseProductRepository.save(warehouseProduct);
        }
    }

    private void saveWarehouseProductList(List<OrderItemDTO> orderItemDTOS, Order order, Employee employee) {
        for (OrderItemDTO orderItem : orderItemDTOS) {
            OrderProduct orderProduct = new OrderProduct();
            long productId = orderItem.getProductId();
            productService.checkExistence(productId);
            Product product = productRepository.getById(productId);
            orderProduct.setProduct(product);
            int amount = orderItem.getAmount();
            if (amount < 1) throw new BusinessLogicException("Incorrect amount of product");
            orderProduct.setAmount(amount);
            orderProduct.setOrder(order);
            orderProduct.setEmployee(employee);
            orderProductRepository.save(orderProduct);
        }
    }

    public List<OrderFullInfoDTO> getAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::mapFromOrderToOrderFullInfoDTO).toList();
    }

    public OrderFullInfoDTO get(long id) {
        checkExistence(id);
        Order order = orderRepository.getById(id);
        return mapFromOrderToOrderFullInfoDTO(order);
    }

    public long delete(long id) {
        checkExistence(id);
        orderRepository.deleteById(id);
        return id;
    }

    public void checkExistence(long id) {
        if (!orderRepository.existsById(id)) {
            throw new BusinessLogicException("Order with this id is not found");
        }
    }

    private OrderFullInfoDTO mapFromOrderToOrderFullInfoDTO(Order order) {
        OrderFullInfoDTO orderFullInfoDTO = modelMapper.map(order, OrderFullInfoDTO.class);
        Pharmacy pharmacy = order.getPharmacy();
        orderFullInfoDTO.setPharmacyId(pharmacy.getId());
        orderFullInfoDTO.setPharmacyName(pharmacy.getName());
        return orderFullInfoDTO;
    }
}
