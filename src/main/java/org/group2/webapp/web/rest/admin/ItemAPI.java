package org.group2.webapp.web.rest.admin;


import org.group2.webapp.entity.Item;
import org.group2.webapp.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ItemAPI {

    private final Logger log = LoggerFactory.getLogger(ItemAPI.class);

    private static final String ENTITY_NAME = "item";

    private final ItemService itemService;

    public ItemAPI(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to save Item : {}", item);
        if (item.getCrn() == null || itemService.findOne(item.getCrn())!= null) {
            return ResponseEntity.badRequest().build();
        }
        Item result = itemService.create(item);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/items")
    public ResponseEntity<Item> updateItem(@Valid @RequestBody Item item) throws URISyntaxException {
        log.debug("REST request to update Item : {}", item);
        if (item.getCrn() == null) {
            return createItem(item);
        }
        Item result = itemService.update(item);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/items")
    public List<Item> getAllItems() {
        log.debug("REST request to get all Items");
        return itemService.findAll();
    }


    @GetMapping("/items/{code}")
    public ResponseEntity<Item> getItem(@PathVariable String code) {
        log.debug("REST request to get Item : {}", code);
        Item item = itemService.findOne(code);
        if(item == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{code}")
    public ResponseEntity<Void> deleteItem(@PathVariable String code) {
        log.debug("REST request to delete Item : {}", code);
        itemService.delete(code);
        return ResponseEntity.ok().build();
    }

}
