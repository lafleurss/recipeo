package recipeo.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.activity.requests.GetCategoryRequest;
import recipeo.activity.results.GetCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetCategoryActivityTest {
    @Mock
    private CategoryDao categoryDao;

    private  GetCategoryActivity getCategoryActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        getCategoryActivity = new GetCategoryActivity(categoryDao);
    }

    @Test
    public void handleRequest_savedCategoryFound_returnsCategoryInResult() {
        // GIVEN
        String expectedCategoryName = "expectedName";
        String expectedCategoryDescription = "expectedDesc";
        String expectedUserId = "expectedUserId";


        Category category1 = new Category();
        category1.setCategoryName(expectedCategoryName);
        category1.setUserId(expectedUserId);
        category1.setCategoryDescription(expectedCategoryDescription);

        when(categoryDao.getCategory(expectedUserId, expectedCategoryName)).thenReturn(category1);

        GetCategoryRequest request = GetCategoryRequest.builder()
                .withCategoryName(expectedCategoryName)
                .withUserId(expectedUserId)
                .build();

        // WHEN
        GetCategoryResult result = getCategoryActivity.handleRequest(request);

        // THEN
        assertEquals(expectedCategoryDescription, result.getCategory().getCategoryDescription());
        assertEquals(expectedCategoryName, result.getCategory().getCategoryName());
    }

}
