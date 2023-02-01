package recipeo.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import recipeo.activity.requests.CreateCategoryRequest;
import recipeo.activity.requests.GetCategoryRequest;
import recipeo.activity.results.CreateCategoryResult;
import recipeo.activity.results.GetCategoryResult;
import recipeo.dynamodb.CategoryDao;
import recipeo.dynamodb.models.Category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class CreateCategoryActivityTest {

    private CreateCategoryActivity createCategoryActivity;

    @Mock
    private CategoryDao categoryDao;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        createCategoryActivity = new CreateCategoryActivity(categoryDao);
    }

    @Test
    public void handleRequest_addCategory_returnsCategoryInResult() {
        // GIVEN
        String newCategoryName = "expectedName";
        String newCategoryDescription = "expectedDesc";
        String userId = "expectedUserId";


        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .withCategoryName(newCategoryName)
                .withCategoryDescription(newCategoryDescription)
                .withUserId(userId)
                .build();

        System.out.println(request.toString());

        // WHEN
        CreateCategoryResult result = createCategoryActivity.handleRequest(request);

        // THEN
        verify(categoryDao).saveCategory(any(Category.class));

        //assertNotNull(result.getCategory().getCategoryName());
        assertEquals(newCategoryName, result.getCategory().getCategoryName());
        assertEquals(newCategoryDescription, result.getCategory().getCategoryDescription());
        assertEquals(userId, result.getCategory().getUserId());
    }


}
