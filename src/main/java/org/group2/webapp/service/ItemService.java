package org.group2.webapp.service;

import org.group2.webapp.entity.Item;
import org.group2.webapp.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    private final ItemRepository courseRepository;

    public ItemService(ItemRepository courseRepository) {
        this.courseRepository = courseRepository;
    }


    @Transactional(readOnly = true)
    public List<Item> findAll() {
        log.debug("Request to get all  Courses");
        List<Item> result = courseRepository.findAll();

        return result;
    }

    @Transactional(readOnly = true)
    public Item findOne(String code) {
        log.debug("Request to get  Course : {}", code);
        if (code == null)
            return null;
        Item course = courseRepository.findOne(code);
        return course;
    }

    public void delete(String code) {
        log.debug("Request to delete  Course : {}", code);
        if (code == null || courseRepository.findOne(code) == null)
            return;
        courseRepository.delete(code);
    }

    public Item create(Item course) {
        log.debug("Request to create  course: {}", course);
        return courseRepository.save(course);
    }

    public Item update(Item course) {
        log.debug("Request to update  course: {}", course.getCrn());
        if (course.getCrn() == null)
            return null;
        return courseRepository.save(course);
    }
}
