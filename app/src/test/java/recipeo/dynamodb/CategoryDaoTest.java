package recipeo.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.dynamodb.models.Category;
import recipeo.dynamodb.models.Recipe;
import recipeo.metrics.MetricsConstants;
import recipeo.metrics.MetricsPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CategoryDaoTest {
    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private QueryResultPage<Category> queryResultPage;
    @Mock
    private MetricsPublisher metricsPublisher;

    private CategoryDao categoryDao;
    @BeforeEach
    public void setup() {
        openMocks(this);
        categoryDao = new CategoryDao(dynamoDBMapper, metricsPublisher);
    }
    @Test
    public void getCategory_withCategoryNameUserId_callsMapperWithPartitionKeyAndRangeKey() {
        // GIVEN
        String userId = "userId";
        String categoryName = "categoryName";
        when(dynamoDBMapper.load(Category.class, userId, categoryName)).thenReturn(new Category());

        // WHEN
        Category category = categoryDao.getCategory(userId, categoryName);

        // THEN
        assertNotNull(category);
        verify(dynamoDBMapper).load(Category.class,userId, categoryName);
        verify(metricsPublisher).addCount(eq(MetricsConstants.GETCATEGORY_CATEGORYNOTFOUND_COUNT), anyDouble());

    }

    @Test
    public void getCategoriesForUser_withUserId_callsMapperWithPartitionKeyAndRangeKey() {
        // GIVEN
        String userId = "userId";
        Category catOne = new Category();
        catOne.setUserId(userId);
        catOne.setCategoryName("CatOne");

        Category catTwo = new Category();
        catOne.setUserId(userId);
        catOne.setCategoryName("CatTwo");

        queryResultPage.setResults(List.of(catOne, catTwo));
        when(dynamoDBMapper.queryPage(eq(Category.class), any())).thenReturn(queryResultPage);

        // WHEN
        List<Category> categoryList = categoryDao.getCategoriesForUser(userId);

        // THEN
        assertNotNull(categoryList);
        verify(dynamoDBMapper).queryPage(eq(Category.class), any());

    }

    @Test
    public void saveCategory_callsMapperWithCategory() {
        // GIVEN
        Category category = new Category();

        // WHEN
        Category result = categoryDao.saveCategory(category);

        // THEN
        verify(dynamoDBMapper).save(category);
        assertEquals(category, result);
    }

}
