package recipeo.activity.results;

import recipeo.dynamodb.models.Category;

public class GetCategoryResult {
    private final Category category;

    private GetCategoryResult(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }


    @Override
    public String toString() {
        return "GetCategoryResult{" +
                "category=" + category +
                '}';
    }

    //CHECKSTYLE:OFF:Builder

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Category category;

        public Builder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public GetCategoryResult build() {
            return new GetCategoryResult(category);
        }
    }
}
