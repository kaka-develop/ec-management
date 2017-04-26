package org.group2.webapp.web.mvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.group2.webapp.EcManagementApplication;
import org.group2.webapp.entity.Item;
import org.group2.webapp.service.ItemService;
import org.group2.webapp.web.mvc.ctrl.admin.ItemController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcManagementApplication.class)
public class ItemControllerTest {

    @Autowired
    private ItemService itemService;

    private MockMvc restCourseMockMvc;

    private final String ITEM_CODE = "AAAAAAAA";
    private final String ITEM_TITLE = "AAAAAAAA";

    private Item item;


    @Before
    public void setup() {
        ItemController itemResource = new ItemController(itemService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(itemResource).build();
    }

    @Before
    public void initTest() {
        item = new Item();
        item.setCrn(ITEM_CODE);
        item.setTitle(ITEM_TITLE);
    }

    public void createCourse() {
        itemService.create(item);
    }


    @Test
    @Transactional
    public void testShouldHaveViewForAllCourses() throws Exception {
        restCourseMockMvc.perform(get("/admin/item"))
                .andExpect(model().attributeExists("items"))
                .andExpect(view().name("admin/item/items"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldHaveViewForAddingCourse() throws Exception {
        restCourseMockMvc.perform(get("/admin/item/new"))
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("admin/item/add"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostAddingOneCourse() throws Exception {
        restCourseMockMvc.perform(post("/admin/item/new")
                .param("crn",ITEM_CODE)
                .param("title",ITEM_TITLE))
                .andExpect(view().name(ItemController.REDIRECT_INDEX));

        restCourseMockMvc.perform(post("/admin/item/new")
                .param("title",""))
                .andExpect(view().name("admin/item/add"));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForCourseDetail() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/item/detail/" + item.getCrn()))
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("admin/item/detail"))
                .andExpect(status().isOk());

        restCourseMockMvc.perform(get("/admin/item/detail/" + "BBBBBBBB"))
                .andExpect(view().name(ItemController.REDIRECT_INDEX));
    }


    @Test
    @Transactional
    public void testShouldHaveViewForEditingCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(get("/admin/item/edit/" + item.getCrn()))
                .andExpect(model().attributeExists("item"))
                .andExpect(view().name("admin/item/edit"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testShouldPostEditingOneCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(post("/admin/item/edit")
                .param("crn",ITEM_CODE)
                .param("title",ITEM_TITLE + ITEM_TITLE))
                .andExpect(view().name(ItemController.REDIRECT_INDEX));

        restCourseMockMvc.perform(post("/admin/item/edit")
                .param("title",""))
                .andExpect(view().name("admin/item/edit"));
    }

    @Test
    @Transactional
    public void testShouldPostDeletingOneCourse() throws Exception {
        createCourse();

        restCourseMockMvc.perform(post("/admin/item/delete/" + item.getCrn()))
                .andExpect(view().name("admin/item/items"));

        restCourseMockMvc.perform(post("/admin/item/delete/" + "BBBBBBB"))
                .andExpect(view().name("admin/item/items"));
    }

    @After
    public void after() {
        itemService.delete(item.getCrn());
    }

}
