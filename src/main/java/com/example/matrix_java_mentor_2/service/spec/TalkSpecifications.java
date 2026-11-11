package com.example.matrix_java_mentor_2.service.spec;

import com.example.matrix_java_mentor_2.entity.TalkEntity;
import org.springframework.data.jpa.domain.Specification;

public final class TalkSpecifications {
    private TalkSpecifications() {}

    public static Specification<TalkEntity> titleContains(String title) {
        if (title == null || title.isBlank()) {return null;}
        String pattern = "%" + title.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), pattern);
    }

    public static Specification<TalkEntity> inRoom(String room) {
        if (room == null || room.isBlank()) {return null;}
        String value = room.trim().toLowerCase();
        return (root, query, cb) -> cb.equal(cb.lower(root.get("room")), value);
    }

    public static Specification<TalkEntity> minDuration(Integer min) {
        if (min == null) {return null;}
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("durationMinutes"), min);
    }

    public static Specification<TalkEntity> maxDuration(Integer max) {
        if (max == null) {return null;}
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("durationMinutes"), max);
    }
}
