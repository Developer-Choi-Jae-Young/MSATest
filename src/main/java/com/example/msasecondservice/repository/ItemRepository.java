package com.example.msasecondservice.repository;

import com.example.msasecondservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long userId);
}
