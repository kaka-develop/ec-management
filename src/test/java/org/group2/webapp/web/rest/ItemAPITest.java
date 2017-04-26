package org.group2.webapp.web.rest;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Item;
import org.group2.webapp.repository.ItemRepository;
import org.group2.webapp.service.ItemService;
import org.group2.webapp.web.rest.admin.ItemAPI;
import org.group2.webapp.web.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ItemAPITest {

    private static final String ITEM_CODE = "AAAAAAAAAA";
    private static final String ITEM_TITLE = "AAAAAAAAAA";

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


    @Autowired
    private EntityManager em;

    private MockMvc restItemMockMvc;

    private Item item;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemAPI itemAPI = new ItemAPI(itemService);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemAPI).build();
    }

    public static Item createEntity(EntityManager em) {
        Item item = new Item();
        item.setCrn(ITEM_CODE);
        item.setTitle(ITEM_TITLE);
        return item;
    }

    @Before
    public void initTest() {
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void testShouldResponseAddedItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();


        restItemMockMvc.perform(post("/api/admin/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isOk());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemRepository.findOne(item.getCrn());
        assertThat(testItem.getTitle()).isEqualTo(ITEM_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseItemWithExistingCrn() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        Item existingItem = new Item();
        existingItem.setCrn(ITEM_CODE);

        restItemMockMvc.perform(post("/api/admin/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(existingItem)))
                .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void testShouldResponseItemIsInvalid() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        item.setTitle(null);

        restItemMockMvc.perform(post("/api/admin/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void testShouldResponseAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the itemList
        restItemMockMvc.perform(get("/api/admin/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].crn").value(hasItem(item.getCrn())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(ITEM_TITLE.toString())));
    }

    @Test
    @Transactional
    public void testShouldResponseOneItemByCrn() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/admin/items/{crn}", item.getCrn()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.crn").value(item.getCrn()))
                .andExpect(jsonPath("$.title").value(ITEM_TITLE.toString()));
    }

    @Test
    @Transactional
    public void testShouldResponseItemIsNotFound() throws Exception {
        restItemMockMvc.perform(get("/api/admin/items/{crn}", "BBBBBBBBB"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void testShouldResponseUpdatedItem() throws Exception {
        itemService.update(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findOne(item.getCrn());
        updatedItem
                .setTitle(ITEM_TITLE + ITEM_TITLE);

        restItemMockMvc.perform(put("/api/admin/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
                .andExpect(status().isOk());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem =itemRepository.findOne(item.getCrn());
        assertThat(testItem.getTitle()).isEqualTo(ITEM_TITLE + ITEM_TITLE);
    }

    @Test
    @Transactional
    public void testShouldResponseOkDeletingItemByCrn() throws Exception {
        itemService.create(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        restItemMockMvc.perform(delete("/api/admin/items/{crn}", item.getCrn())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
