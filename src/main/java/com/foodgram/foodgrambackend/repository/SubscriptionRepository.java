package com.foodgram.foodgrambackend.repository;

import com.foodgram.foodgrambackend.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Page<Subscription> findBySubscriberId(Long id, Pageable pageable);

    boolean existsBySubscriberIdAndAuthorId(Long subscriberId, Long authorId);

    Optional<Subscription> findBySubscriberIdAndAuthorId(Long subscriberId, Long authorId);
}
