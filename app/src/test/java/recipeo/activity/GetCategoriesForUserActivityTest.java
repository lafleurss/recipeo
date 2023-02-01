package recipeo.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.activity.requests.GetCategoriesForUserRequest;
import recipeo.activity.requests.GetRecipeRequest;
import recipeo.activity.results.GetCategoriesForUserResult;
import recipeo.activity.results.GetRecipeResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class GetCategoriesForUserActivityTest {
    @Mock
    private CategoryDao categoryDao;

    private  GetCategoriesForUserActivity getCategoriesForUserActivity;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        getCategoriesForUserActivity = new GetCategoriesForUserActivity(categoryDao);
    }

    @Test
    public void handleRequest_savedCategoriesFound_returnsListOfCategoriesInResult() {
        // GIVEN
        String expectedCategoryName = "expectedName";
        String anotherExpectedCategoryName = "anotherExpectedName";
        String expectedUserId = "expectedUserId";


        Category category1 = new Category();
        category1.setCategoryName(expectedCategoryName);
        category1.setUserId(expectedUserId);

        Category category2 = new Category();
        category2.setCategoryName(anotherExpectedCategoryName);
        category2.setUserId(expectedUserId);



        when(categoryDao.getCategoriesForUser(expectedUserId)).thenReturn(List.of(category1,category2));

        GetCategoriesForUserRequest request = GetCategoriesForUserRequest.builder()
                .withUserId(expectedUserId)
                .build();

        // WHEN
        GetCategoriesForUserResult result = getCategoriesForUserActivity.handleRequest(request);

        // THEN
        assertEquals(List.of(category1, category2), result.getCategories());
    }
}
