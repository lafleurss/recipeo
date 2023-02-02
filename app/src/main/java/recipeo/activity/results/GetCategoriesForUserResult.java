package recipeo.activity.results;

import recipeo.dynamodb.models.Category;

import java.util.List;

public class GetCategoriesForUserResult {
    private final List<Category> categories;

    private GetCategoriesForUserResult(List<Category> categories) {
        this.categories = List.copyOf(categories);
    }

    public List<Category> getCategories() {
        return List.copyOf(categories);
    }

    @Override
    public String toString() {
        return "GetCategoriesForUserResult{" +
                "categories=" + categories +
                '}';
    }

    //CHECKSTYLE:OFF:Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Category> categoryList;

        public Builder withCategories(List<Category> categoryList) {
            this.categoryList = List.copyOf(categoryList);
            return this;
        }

        public GetCategoriesForUserResult build() {
            return new GetCategoriesForUserResult(categoryList);
        }
    }
}
