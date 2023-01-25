package recipeo.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import recipeo.dynamodb.CategoryDao;

import javax.inject.Inject;

public class GetCategoryActivity {
    private final Logger log = LogManager.getLogger();
    private final CategoryDao categoryDao;

    @Inject
    public GetCategoryActivity(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }
}
