package org.group2.webapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Item;
import org.group2.webapp.repository.ItemRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
@Transactional
public class ItemServiceTest {

    private final Logger log = LoggerFactory.getLogger(ItemServiceTest.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


    private final String ITEM_CODE = "AAAAAAAA";
    private final String ITEM_TITLE = "AAAAAAAA";

    private Item item;

    @Before
    public void before() {
        item = new Item();
        item.setCrn(ITEM_CODE);
        item.setTitle(ITEM_TITLE);
        item = itemService.create(item);
        log.debug("done create item");
    }

    @Test
    public void testShouldHaveItem() {
        assertThat(!itemRepository.findAll().isEmpty());
        assertThat(!itemService.findAll().isEmpty());
    }

    @Test
    public void testShouldHaveOneItemByID() {
        assertThat(itemRepository.findOne(item.getCrn())!= null);
        assertThat(itemService.findOne(item.getCrn())!= null);

        assertThat(itemRepository.findOne("BBBBBBB")== null);
        assertThat(itemService.findOne( "BBBBBBB")== null);
    }

    @After
    public void after() {
        itemService.delete(item.getCrn());
        log.debug("done delete item");
    }
}
